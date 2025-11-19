// src/main/java/com/restapi/consumer/EmailNotificationConsumer.java
package com.restapi.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.restapi.dto.BookingEvent;
import com.restapi.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
public class EmailNotificationConsumer {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private InvoiceService invoiceService;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationConsumer.class);

    @KafkaListener(
            topics = "booking-events",
            groupId = "booking-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleBookingEvent(BookingEvent event) {
        log.info("Received Kafka event: {}", event);

        try {
            // Generate PDF from orderId
            byte[] pdfBytes = invoiceService.generateTicketPdf(event.getOrderId());

            // Send email
            sendEmailWithAttachment(event, pdfBytes);
        } catch (Exception e) {
            log.error("Kafka: Failed to send email", e);
        }
    }

    private void sendEmailWithAttachment(BookingEvent event, byte[] pdfBytes) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(event.getEmail());
        helper.setFrom(fromEmail);
        helper.setSubject("Booking Confirmed - Invoice #" + event.getInvoiceNumber());
        helper.setText(
                "Hi,\n\nYour booking for '" + event.getEventName() +
                        "' is confirmed!\nTotal: $" + event.getTotalAmount() +
                        "\n\nPlease find your attached ticket.\n\nBest regards,\nEventor Team"
        );

        helper.addAttachment("ticket.pdf", new ByteArrayResource(pdfBytes));

        mailSender.send(mimeMessage);
        log.info("Kafka: Email sent successfully to {}", event.getEmail());
    }
}
