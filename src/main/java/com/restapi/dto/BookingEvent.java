// src/main/java/com/restapi/dto/BookingEvent.java
package com.restapi.dto;

import lombok.Data;

@Data
public class BookingEvent {
    private Long orderId;
    private Long invoiceId;
    private String email;
    private String eventName;
    private Double totalAmount;
    private String invoiceNumber;
}