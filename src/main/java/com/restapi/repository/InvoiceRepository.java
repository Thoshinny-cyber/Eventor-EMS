package com.restapi.repository;

import com.restapi.model.Invoice;
import com.restapi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByOrder(Order order);
    List<Invoice> findByOrder_Users_Id(Long userId);
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}

