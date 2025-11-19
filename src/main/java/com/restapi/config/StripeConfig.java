// src/main/java/com/restapi/config/StripeConfig.java
package com.restapi.config;

import com.stripe.Stripe;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    @Value("${stripe.secret-key}")
    private String secretKey;

    @Value("${stripe.publishable-key}")
    private String publishableKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey; // ← Sets global key for Stripe SDK
        System.out.println("Stripe initialized with secret key: " +
                (secretKey.startsWith("sk_test_") ? "sk_test_..." : "sk_live_..."));
    }

//    // Optional: expose publishable key to frontend via API
//    public String getPublishableKey() {
//        return publishableKey;
//    }
}