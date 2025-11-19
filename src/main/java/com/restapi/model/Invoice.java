package com.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invoices")
public class Invoice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @Column(nullable = false, unique = true)
    private String invoiceNumber;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private String paymentStatus; // PAID, PENDING, FAILED

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String invoiceDetails; // JSON string or formatted text
}

