// src/main/java/com/restapi/service/KafkaProducerService.java
package com.restapi.service;

import com.restapi.dto.BookingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, BookingEvent> kafkaTemplate;

    private static final String TOPIC = "booking-events";

    public void sendBookingEvent(BookingEvent event) {
        System.out.println("Sending to Kafka: " + event);
        kafkaTemplate.send(TOPIC, event);
    }
}
