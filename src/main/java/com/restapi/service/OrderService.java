package com.restapi.service;

import com.restapi.dto.OrderDto;
import com.restapi.exception.common.ResourceNotFoundException;
import com.restapi.model.AppUser;
import com.restapi.model.Event;
import com.restapi.model.Order;
import com.restapi.model.Seat;
import com.restapi.repository.EventRepository;
import com.restapi.repository.OrderRepository;
import com.restapi.repository.SeatRepository;
import com.restapi.repository.UserRepository;
import com.restapi.request.OrderRequest;
import com.restapi.request.SeatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDto orderDto;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SeatRepository seatRepository;

    public List<Order> findAll(Long id) {
        System.out.println(orderRepository.findAllForId(id));
        return orderRepository.findAllForId(id);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order bookTickets(OrderRequest orderRequest) {
        System.out.println("=== ORDER BOOKING SERVICE STARTED ===");
        System.out.println("User ID: " + orderRequest.getUserId());
        System.out.println("Event ID: " + orderRequest.getEventId());
        System.out.println("Ticket Count: " + orderRequest.getCount());
        System.out.println("Booked Seats: " + orderRequest.getBookedSeats());

        // Create order entity
        Order order = orderDto.mapToOrder(orderRequest);


        // Fetch user and event
        AppUser appUser = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("userId", "userId", orderRequest.getUserId()));
        Event event = eventRepository.findById(orderRequest.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("eventId", "eventId", orderRequest.getEventId()));

        order.setUsers(appUser);
        order.setEvent(event);
        order.setPaymentStatus("PENDING");

        // Save order first to get the ID
        order = orderRepository.save(order);
        System.out.println("Order saved with ID: " + order.getId());
        // Save seats
        for (SeatRequest seatRequest : orderRequest.getBookedSeats()) {
            seatRequest.setOrderid(order.getId());
            seatRequest.setEventid(event.getId());
            seatRequest.setUserid(appUser.getId());

            Seat bookedSeat = orderDto.mapSeatRequestToSeat(seatRequest);
            seatRepository.save(bookedSeat);
            System.out.println("Seat saved: " + seatRequest.getSeatNumber());
        }

        System.out.println("=== ORDER BOOKING COMPLETED SUCCESSFULLY ===");
        order.setPaymentStatus(orderRequest.getPaymentStatus() != null ? orderRequest.getPaymentStatus() : "PENDING");
        order.setPaymentStatus("PAID"); // ← FORCE PAID
        if ("PAID".equals(orderRequest.getPaymentStatus())) {
            int existingSoldTickets = event.getSoldTickets();
            event.setSoldTickets(existingSoldTickets + order.getCount());
            eventRepository.save(event);
            System.out.println("Event updated - Sold tickets: " + event.getSoldTickets());
            // DECREMENT AVAILABLE
            int newAvailable = event.getAvailableTickets() - order.getCount();
            if (newAvailable < 0) {
                throw new IllegalStateException("Not enough tickets available");
            }
            event.setAvailableTickets(newAvailable);
        }
        order = orderRepository.save(order);
        return order;
    }

//    public Object deleteOrder(long id) {
//        seatRepository.deleteSeats(id);
//        int count=orderRepository.findById(id).get().getEvent().getSoldTickets()-orderRepository.findById(id).get().getCount();
/// /        eventRepository.updateSoldTickets(orderRepository.findById(id).get().getEvent().getId(),count);
///
/// @return
//        System.out.println(count);
//        orderRepository.deleteById(id);
//        return findAll();
//    }
public Object deleteOrder(Long orderId) {
    Order order = orderRepository.findById(orderId).orElseThrow();

    // Only decrement if it was PAID
    if ("PAID".equals(order.getPaymentStatus())) {
        Event event = eventRepository.findById(order.getEventId()).orElseThrow();
        int newCount = event.getSoldTickets() - order.getCount();
        int availableCount= event.getAvailableTickets() + order.getCount();
        event.setSoldTickets(Math.max(0, newCount)); // prevent negative
        event.setAvailableTickets(event.getAvailableTickets() + availableCount);
        eventRepository.save(event);
    }

    orderRepository.delete(order);
    return "Order deleted successfully";
}

}