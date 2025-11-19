package com.restapi.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.restapi.model.Invoice;
import com.restapi.model.Order;
import com.restapi.model.Seat;
import com.restapi.repository.InvoiceRepository;
import com.restapi.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.awt.SystemColor.text;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Invoice generateInvoice(Order order, String paymentIntentId) {
        Optional<Invoice> existingInvoice = invoiceRepository.findByOrder(order);
        if (existingInvoice.isPresent()) {
            return existingInvoice.get();
        }

        Invoice invoice = new Invoice();
        invoice.setOrder(order);
        invoice.setInvoiceNumber(generateInvoiceNumber());

        double totalAmount = order.getEvent().getPrice() * order.getCount();
        invoice.setInvoiceDetails(generateInvoiceDetails(order, totalAmount, paymentIntentId));

        return invoiceRepository.save(invoice);
    }

    protected String generateInvoiceNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "INV-" + timestamp + "-" + random;
    }

    protected String generateInvoiceDetails(Order order, double totalAmount, String paymentIntentId) {
        StringBuilder details = new StringBuilder();
        details.append("Event: ").append(order.getEvent().getName()).append("\n");
        details.append("Venue: ").append(order.getEvent().getVenue()).append("\n");
        details.append("Date: ").append(order.getEvent().getDate()).append("\n");
        details.append("Tickets: ").append(order.getCount()).append("\n");
        details.append("Price per Ticket: S$").append(order.getEvent().getPrice()).append("\n");
        details.append("Total Amount: S$").append(totalAmount).append("\n");
        details.append("Customer: ").append(order.getUsers().getName()).append("\n");
        details.append("Email: ").append(order.getUsers().getEmail()).append("\n");
        details.append("Payment Intent: ").append(paymentIntentId).append("\n");
        details.append("Payment Method: Stripe\n");
        details.append("Status: PAID\n");
        return details.toString();
    }

    // PDF TICKET USING iText 5
    @Transactional
    public byte[] generateTicketPdf(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        double totalAmount = order.getEvent().getPrice() * order.getCount();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, baos);
        document.open();

        // Fonts
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);

        // Title - Centered
        Paragraph title = new Paragraph("EVENT TICKET", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Event Info
        document.add(new Paragraph("Event: " + order.getEvent().getName(), normalFont));
        document.add(new Paragraph("Date: " + order.getEvent().getDate(), normalFont));
        document.add(new Paragraph("Venue: " + order.getEvent().getVenue(), normalFont));
        document.add(new Paragraph("")); // Spacer

        // Table
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(80);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        // Helper method (must be inside the method or as a private method)
        java.util.function.BiFunction<String, Font, PdfPCell> leftCell = (text, font) -> {
            PdfPCell cell = new PdfPCell(new Phrase(text, font));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(5);
            return cell;
        };

        java.util.function.BiFunction<String, Font, PdfPCell> rightCell = (text, font) -> {
            PdfPCell cell = new PdfPCell(new Phrase(text, font));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(5);
            return cell;
        };

        table.addCell(leftCell.apply("Tickets", boldFont));
        table.addCell(rightCell.apply(order.getCount() + " × S$" + order.getEvent().getPrice(), normalFont));

        table.addCell(leftCell.apply("Seats", boldFont));
        table.addCell(rightCell.apply(
                order.getSeat() != null && !order.getSeat().isEmpty()
                        ? order.getSeat().stream()
                        .map(Seat::getSeatNumber)
                        .collect(Collectors.joining(", "))
                        : "N/A",
                normalFont
        ));

        table.addCell(leftCell.apply("Total", boldFont));
        table.addCell(rightCell.apply("S$" + totalAmount, boldFont));

        document.add(table);
        document.add(new Paragraph("")); // Spacer

        // User Info
        Paragraph holder = new Paragraph("Ticket Holder", boldFont);
        holder.setSpacingBefore(10);
        document.add(holder);
        document.add(new Paragraph("Name: " + order.getUsers().getName(), normalFont));
        document.add(new Paragraph("Email: " + order.getUsers().getEmail(), normalFont));

        // Status - Centered & Gray
        Paragraph status = new Paragraph("Status: PAID", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
        status.setAlignment(Element.ALIGN_CENTER);
        status.setSpacingBefore(20);
        status.setSpacingAfter(10);

        PdfPCell statusCell = new PdfPCell(status);
        statusCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        statusCell.setBorder(Rectangle.NO_BORDER);
        statusCell.setPadding(10);

        PdfPTable statusTable = new PdfPTable(1);
        statusTable.setWidthPercentage(60);
        statusTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        statusTable.addCell(statusCell);
        document.add(statusTable);

        document.close();
        return baos.toByteArray();
    }
    // Repository Methods
    public Optional<Invoice> getInvoiceByOrderId(Long orderId) {
        return invoiceRepository.findById(orderId);
    }

    public List<Invoice> getInvoicesByUserId(Long userId) {
        return invoiceRepository.findByOrder_Users_Id(userId);
    }

    public Optional<Invoice> getInvoiceByInvoiceNumber(String invoiceNumber) {
        return invoiceRepository.findByInvoiceNumber(invoiceNumber);
    }
}