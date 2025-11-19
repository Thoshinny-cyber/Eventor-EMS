package com.restapi.controller;

import com.restapi.dto.CategoryDto;
import com.restapi.model.Category;
import com.restapi.model.Event;
import com.restapi.request.EventRequest;
import com.restapi.response.CategoryResponse;
import com.restapi.response.EventResponse;
import com.restapi.response.common.APIResponse;
import com.restapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("EventRegistration/API/User/Category")
public class CategoryController {
    @Autowired
    private APIResponse apiResponse;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryDto categoryDto;

    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllCategories(){
        List<Category> categoryList= categoryService.findAll();
        List<CategoryResponse> categoryResponses=categoryDto.mapToCategoryResponse(categoryList);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(categoryResponses);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getParticularCategory(@PathVariable Long id){
        List<Event> categoryList= categoryService.findACategory(id);
        List<EventResponse> newCategoryList=categoryDto.mapToEventResponse(categoryList);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(newCategoryList);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<APIResponse> showCategory(@PathVariable Long id){
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(categoryService.showCategory(id));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
