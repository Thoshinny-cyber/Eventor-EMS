package com.restapi.controller.admin;

import com.restapi.dto.CategoryDto;
import com.restapi.model.Category;
import com.restapi.model.Event;
import com.restapi.request.CategoryRequest;
import com.restapi.response.common.APIResponse;
import com.restapi.service.CategoryService;
import com.restapi.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("EventRegistration/API/Admin/Category")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class
AdminCategoryController {
    @Autowired
    private APIResponse apiResponse;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private CategoryDto categoryDto;

    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllCategories(){
        List<Category> categoryList= categoryService.findAll();
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(categoryList);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<APIResponse> addCategory(
            @RequestParam("image") MultipartFile image,
            @RequestParam("name") String name,
            @RequestParam("id") long id
//            @RequestParam("events") List<Event> events
    ){
        String file=storageService.storeFile(image);
        Category category=new Category();
        category.setId(id);
        category.setImage(file);
        category.setName(name);
        List<Category> categoryList= categoryService.addCategory(category);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(categoryList);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<APIResponse> updateCategory(@RequestBody Category category){
        List<Category> categoryList= categoryService.addCategory(category);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(categoryList);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteCategoryById(@PathVariable Long id){
        List<Category> categoryList= categoryService.deleteCategory(id);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(categoryList);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
