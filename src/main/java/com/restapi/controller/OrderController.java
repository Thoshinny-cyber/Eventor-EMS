package com.restapi.controller;

import com.restapi.dto.OrderDto;
import com.restapi.model.Event;
import com.restapi.model.Order;
import com.restapi.request.OrderRequest;
import com.restapi.response.OrderResponse;
import com.restapi.response.common.APIResponse;
import com.restapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("EventRegistration/API/User")
@PreAuthorize("hasRole('ROLE_USER')")
public class OrderController {

    @Autowired
    private APIResponse apiResponse;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDto orderDto;

//    @PostMapping("/create")
//    public ResponseEntity<APIResponse> createOrder(@RequestBody OrderRequest request) {
//        Order order = new Order();
//        order.setUserId(request.getUserId());
//        order.setEventId(request.getEventId());
//        order.setCount(request.getCount());
//        order.setBookedSeats(String.join(",", request.getBookedSeats()));
//        order.setTotalPrice(request.getTotalPrice());
//        order.setPaymentStatus("PENDING");
//        order = orderRepository.save(order);
//
//        apiResponse.setStatus(HttpStatus.OK.value());
//        apiResponse.setData(order);
//        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//    }

//    @GetMapping("/Order/{id}")
//    public ResponseEntity<APIResponse> viewAllOrders(@PathVariable Long id){
//        List<Order> orderList= orderService.findAll(id);
//        List<OrderResponse> orderResponse= orderDto.mapListToOrderResponse(orderList);
//        apiResponse.setStatus(HttpStatus.OK.value());
//        apiResponse.setData(orderResponse);
//        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//    }
@GetMapping("/Order/{id}")
public ResponseEntity<APIResponse> viewAllOrders(@PathVariable Long id) {
    List<Order> orderList = orderService.findAll(id);
    List<OrderResponse> orderResponse = orderDto.mapListToOrderResponse(orderList);

    System.out.println("=== USER ORDERS ===");
    orderList.forEach(o -> System.out.println("Order ID: " + o.getId() +
            ", Seats: " + o.getSeat() +
            ", Payment: " + o.getPaymentStatus()));

    apiResponse.setStatus(HttpStatus.OK.value());
    apiResponse.setData(orderResponse);
    return new ResponseEntity<>(apiResponse, HttpStatus.OK);
}

    @PostMapping("/Order/Ticketbooking")
    public ResponseEntity<APIResponse> bookTickets(@Valid @RequestBody OrderRequest orderRequest) {
        try {
            System.out.println("=== ORDER BOOKING REQUEST STARTED ===");
            System.out.println("Received OrderRequest: " + orderRequest);
            Order order = orderService.bookTickets(orderRequest);
            if (orderRequest.getPaymentStatus() != null) {
                order.setPaymentStatus(orderRequest.getPaymentStatus()); // <-- THIS LINE
            }

            System.out.println("Order created successfully: " + order.getId());

            OrderResponse orderResponse = orderDto.mapToOrderResponse(order);

            apiResponse.setStatus(HttpStatus.OK.value());
            apiResponse.setData(orderResponse);

            System.out.println("=== ORDER BOOKING COMPLETED SUCCESSFULLY ===");
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("=== ORDER BOOKING FAILED ===");
            e.printStackTrace();

            apiResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            apiResponse.setData("Booking failed: " + e.getMessage());
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("order/delete/{id}")
    public ResponseEntity<APIResponse> deleteOrder(@PathVariable long id){
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(orderService.deleteOrder(id));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
