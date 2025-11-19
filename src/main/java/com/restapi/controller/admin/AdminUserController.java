package com.restapi.controller.admin;

import com.restapi.dto.UserDto;
import com.restapi.model.AppUser;
import com.restapi.request.LoginRequest;
import com.restapi.response.AuthResponse;
import com.restapi.response.ProfileResponse;
import com.restapi.response.common.APIResponse;
import com.restapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("EventRegistration/API/Admin/UserControl")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminUserController {
    @Autowired
    private APIResponse apiResponse;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDto userDto;

    @GetMapping
    public ResponseEntity<APIResponse> GetAllUsers() {
        List<AppUser> users = userService.getAllProfiles();
        List<ProfileResponse> profileResponses = userDto.mapToProfileResponse(users);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(profileResponses);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable long id) {
        List<AppUser> users=userService.deleteUser(id);
        List<ProfileResponse> profileResponses = userDto.mapToProfileResponse(users);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(profileResponses);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
