package com.restapi.controller.admin;

import com.restapi.dto.OrderDto;
import com.restapi.model.Order;
import com.restapi.response.OrderResponse;
import com.restapi.response.common.APIResponse;
import com.restapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("EventRegistration/API/Admin/Order")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminOrderController {

    @Autowired
    private APIResponse apiResponse;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDto orderDto;

    @GetMapping
    public ResponseEntity<APIResponse> viewAll(){
        List<Order> orderList= orderService.findAll();
        List<OrderResponse> orderResponse= orderDto.mapListToOrderResponse(orderList);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(orderResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
