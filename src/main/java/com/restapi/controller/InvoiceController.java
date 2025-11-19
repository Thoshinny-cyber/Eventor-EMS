package com.restapi.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.restapi.model.Invoice;
import com.restapi.response.common.APIResponse;
import com.restapi.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("EventRegistration/API/User/Invoice")
@PreAuthorize("hasRole('ROLE_USER')")
public class InvoiceController {

    @Autowired
    private APIResponse apiResponse;

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<APIResponse> getInvoiceByOrderId(@PathVariable Long orderId) {
        Invoice invoice = invoiceService.getInvoiceByOrderId(orderId)
                .orElse(null);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(invoice);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<APIResponse> getInvoicesByUserId(@PathVariable Long userId) {
        List<Invoice> invoices = invoiceService.getInvoicesByUserId(userId);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(invoices);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/number/{invoiceNumber}")
    public ResponseEntity<APIResponse> getInvoiceByNumber(@PathVariable String invoiceNumber) {
        Invoice invoice = invoiceService.getInvoiceByInvoiceNumber(invoiceNumber)
                .orElse(null);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(invoice);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @GetMapping("/download/ticket/{orderId}")
    public ResponseEntity<byte[]> downloadTicketPdf(@PathVariable Long orderId) {
        try {
            byte[] pdfBytes = invoiceService.generateTicketPdf(orderId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "ticket-" + orderId + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}