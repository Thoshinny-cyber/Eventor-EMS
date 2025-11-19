// src/main/java/com/restapi/controller/BookingController.java
package com.restapi.controller;

import com.restapi.dto.BookingEvent;
import com.restapi.repository.UserRepository;
import com.restapi.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/confirm")
    public String confirmBooking(@RequestParam long userId, @RequestBody BookingEvent event) {
        // Fetch user email from DB
        String userEmail = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getEmail();

        // Attach email dynamically before sending event to Kafka
        event.setEmail(userEmail);

        // Publish the booking event to Kafka
        kafkaProducerService.sendBookingEvent(event);

        System.out.println("Controller: Booking event published for user " + userEmail);
        return "Booking confirmed and event published to Kafka!";
    }
}
