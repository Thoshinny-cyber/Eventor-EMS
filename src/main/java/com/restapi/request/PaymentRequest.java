package com.restapi.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private Long orderId;
    private Double amount;
    private String paymentIntentId;  // Stripe only
    // Remove: razorpayOrderId, razorpayPaymentId, razorpaySignature
}