package com.restapi.controller;

import com.restapi.model.Category;
import com.restapi.model.Discount;
import com.restapi.response.CategoryResponse;
import com.restapi.response.common.APIResponse;
import com.restapi.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DiscountController {

    @Autowired
    private APIResponse apiResponse;

    @Autowired
    private DiscountService discountService;

    @GetMapping("/discounts")
    public ResponseEntity<APIResponse> getAllDiscounts(){
        List<Discount> discount = discountService.getAllDiscounts();
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(discount);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
