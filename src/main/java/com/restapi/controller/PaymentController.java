package com.restapi.controller;



import com.restapi.service.PaymentService;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/EventRegistration/API/User/Payment")
@CrossOrigin(origins = "http://localhost:3000")  // For React
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-intent")
    public ResponseEntity<?> createIntent(@RequestBody Map<String, Object> req) {
        System.out.println("create-intent request: " + req); // DEBUG

        Object amountObj = req.get("amount");
        if (amountObj == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Missing 'amount' in request"));
        }

        double amount;
        try {
            amount = Double.parseDouble(amountObj.toString().trim());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid 'amount' – must be a number"));
        }

        if (amount <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Amount must be positive"));
        }

        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) (amount * 100)) // Stripe uses cents
                    .setCurrency("sgd")
                    .addPaymentMethodType("card")
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);
            System.out.println("Returning client_secret: " + intent.getClientSecret());
            return ResponseEntity.ok(Map.of("client_secret", intent.getClientSecret()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

//    @PostMapping("/process-payment")
//    public ResponseEntity<?> processPayment(@RequestBody PaymentRequest request) {
//        try {
//            Long orderId = request.getOrderId();
//            String paymentIntentId = request.getPaymentIntentId();
//
//            Invoice invoice = paymentService.processPayment(orderId, paymentIntentId);
//            return ResponseEntity.ok(invoice);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest()
//                    .body(Map.of("error", e.getMessage()));
//        }
//    }
@PostMapping("/finalize")
public ResponseEntity<?> finalizePayment(@RequestBody Map<String, Object> req) {
    Long orderId = Long.valueOf(req.get("orderId").toString());
    String paymentIntentId = (String) req.get("paymentIntentId");

    try {
        paymentService.finalizeOrder(orderId, paymentIntentId);
        return ResponseEntity.ok(Map.of("status", "PAID"));
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}
}