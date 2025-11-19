package com.restapi.service;

import com.restapi.dto.BookingEvent;
import com.restapi.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.restapi.model.Invoice;
import com.restapi.model.Order;               // your entity
import com.restapi.repository.InvoiceRepository;
import com.restapi.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
//import org.apache.juli.logging.DirectJDKLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private KafkaTemplate<String, BookingEvent> kafkaTemplate;

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    /* --------------------------------------------------------------
       CREATE STRIPE PAYMENT INTENT (SGD)
       -------------------------------------------------------------- */
    public Map<String, Object> createStripePaymentIntent(Long orderId, Double amount)
            throws StripeException {

        Order userOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Stripe.apiKey = stripeSecretKey;

        // ---- Mandate data (required for some payment methods) ----
        PaymentIntentCreateParams.MandateData.CustomerAcceptance.Online online =
                PaymentIntentCreateParams.MandateData.CustomerAcceptance.Online.builder()
                        .setIpAddress("127.0.0.1")      // replace with real IP in prod
                        .setUserAgent("Event‑Reg‑App")
                        .build();

        PaymentIntentCreateParams.MandateData.CustomerAcceptance acceptance =
                PaymentIntentCreateParams.MandateData.CustomerAcceptance.builder()
                        .setType(PaymentIntentCreateParams.MandateData.CustomerAcceptance.Type.ONLINE)
                        .setOnline(online)
                        .build();

        PaymentIntentCreateParams.MandateData mandateData =
                PaymentIntentCreateParams.MandateData.builder()
                        .setCustomerAcceptance(acceptance)
                        .build();

        // ---- Build the PaymentIntent ----
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (amount * 100))      // SGD 12.00 → 1200 cents
                .setCurrency("sgd")
                .setMandateData(mandateData)           // **only method available**
                .addPaymentMethodType("card")          // we are using card
                .build();

        PaymentIntent pi = PaymentIntent.create(params);

        Map<String, Object> response = new HashMap<>();
        response.put("client_secret", pi.getClientSecret());
        response.put("id", pi.getId());
        response.put("amount", pi.getAmount());
        response.put("currency", pi.getCurrency());
        response.put("status", pi.getStatus());

        return response;
    }

    /* --------------------------------------------------------------
       PROCESS PAYMENT (after successful Stripe payment)
       -------------------------------------------------------------- */
// Remove the old 4-parameter version
// public Invoice processPayment(Long orderId, String razorpayOrderId, String razorpayPaymentId, String razorpaySignature)

    // NEW: Only 2 parameters for Stripe
//    public Invoice processPayment(Long orderId, String paymentIntentId) {
//        Order userOrder = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        try {
//            Stripe.apiKey = stripeSecretKey;
//            PaymentIntent pi = PaymentIntent.retrieve(paymentIntentId);
//
//            if ("succeeded".equals(pi.getStatus())) {
//                userOrder.setPaymentStatus("PAID");
//                userOrder.setPaymentIntentId(paymentIntentId);  // Save Stripe ID
//                orderRepository.save(userOrder);
//
//                Invoice invoice = invoiceService.generateInvoice(
//                        userOrder, paymentIntentId, paymentIntentId);
//                invoiceRepository.save(invoice);
//                return invoice;
//            } else {
//                userOrder.setPaymentStatus("FAILED");
//                orderRepository.save(userOrder);
//                throw new RuntimeException("Payment not succeeded: " + pi.getStatus());
//            }
//        } catch (StripeException e) {
//            userOrder.setPaymentStatus("FAILED");
//            orderRepository.save(userOrder);
//            throw new RuntimeException("Stripe error: " + e.getMessage());
//        }
//    }
    public Invoice finalizeOrder(Long orderId, String paymentIntentId) throws StripeException {
        // 1️⃣ Retrieve the order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 2️⃣ Confirm Stripe payment
        Stripe.apiKey = stripeSecretKey;
        PaymentIntent pi = PaymentIntent.retrieve(paymentIntentId);

        if (!"succeeded".equals(pi.getStatus())) {
            throw new RuntimeException("Payment not succeeded");
        }

        // 3️⃣ Update order status
        order.setPaymentStatus("PAID");
        order.setPaymentIntentId(paymentIntentId);
        orderRepository.save(order);

        // 4️⃣ Generate invoice with all required fields
        double totalAmount = order.getEvent().getPrice() * order.getCount();
        Invoice invoice = new Invoice();
        invoice.setOrder(order);
        invoice.setInvoiceNumber(invoiceService.generateInvoiceNumber());
        invoice.setInvoiceDetails(invoiceService.generateInvoiceDetails(order, totalAmount, paymentIntentId));
        invoice.setPaymentMethod("Stripe");          // Required column
        invoice.setPaymentStatus("PAID");            // Required column
        invoice.setTotalAmount(totalAmount);         // Required column
        invoice = invoiceRepository.save(invoice);   // Persist invoice

        // 5️⃣ Send Kafka event
        BookingEvent event = new BookingEvent();
        event.setOrderId(order.getId());
        event.setInvoiceId(invoice.getId());
        event.setEmail(order.getUsers().getEmail());
        event.setEventName(order.getEvent().getName());
        event.setTotalAmount(invoice.getTotalAmount());
        event.setInvoiceNumber(invoice.getInvoiceNumber());

        //kafkaTemplate.send("booking-events", "order-" + order.getId(), event);

        log.info("Sending Kafka event: {}", event);
        kafkaTemplate.send("booking-events", "order-" + order.getId(), event)
                .addCallback(success -> log.info("Kafka send success: {}", success), failure -> log.error("Kafka send failed", failure));
        return invoice;
    }

}