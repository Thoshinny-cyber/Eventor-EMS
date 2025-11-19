// src/main/java/com/restapi/service/EmailService.java
package com.restapi.service;

import com.restapi.dto.BookingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private InvoiceService invoiceService;

    public void sendInvoiceEmail(BookingEvent event) throws Exception {
        byte[] pdfBytes = invoiceService.generateTicketPdf(event.getOrderId());

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(event.getEmail());
        helper.setSubject("Your Ticket - " + event.getInvoiceNumber());
        helper.setText("""
            Hi,

            Thank you for booking!

            Event: %s
            Total: $%.2f
            Invoice: %s

            Your ticket is attached.

            See you there!
            """.formatted(event.getEventName(), event.getTotalAmount(), event.getInvoiceNumber()));

        helper.addAttachment("ticket-" + event.getInvoiceNumber() + ".pdf",
                new ByteArrayResource(pdfBytes));

        mailSender.send(message);
        System.out.println("Email sent to: " + event.getEmail());
    }
}