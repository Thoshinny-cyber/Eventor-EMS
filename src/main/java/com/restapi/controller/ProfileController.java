package com.restapi.controller;

import com.restapi.dto.UserDto;
import com.restapi.model.*;
import com.restapi.request.ProfileRequest;
import com.restapi.response.DpResponse;
import com.restapi.response.UserResponse;
import com.restapi.response.common.APIResponse;
import com.restapi.response.common.ErrorResponse;
import com.restapi.service.FileDownloadingService;
import com.restapi.service.ProfileService;
import com.restapi.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("EventRegistration/API/User/profile")
@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private APIResponse apiResponse;

    @Autowired
    private UserDto userDto;

    @Autowired
    private FileDownloadingService imageService;

    @Autowired
    private StorageService storageService;


    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getUser(@PathVariable Long id){
        AppUser user=profileService.findUser(id);
        UserResponse userResponse=userDto.mapToUserResponse(user);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(userResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<APIResponse> updateUser(
            @RequestBody ProfileRequest profileRequest
    ){
        AppUser user=profileService.updateUser(profileRequest);
        UserResponse userResponse=userDto.mapToUserResponse(user);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(userResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/dp")
    public ResponseEntity<APIResponse> updateDp(@RequestParam("image") MultipartFile image, @RequestParam("id") long id){
        UserDP dp=new UserDP();
        String file=storageService.storeFile(image);
        AppUser user=profileService.findUser(id);
        dp.setDp(file);
        dp.setUser(user);
        profileService.addDp(dp);
        DpResponse dpResponse=userDto.mapToDpResponse(dp);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(dpResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/DP/{id}")
    public ResponseEntity<APIResponse> getUserDP(@PathVariable Long id) {
        UserDP userDp = profileService.findDp(id);

        if (userDp == null) {
            apiResponse.setStatus(HttpStatus.NOT_FOUND.value());
            apiResponse.setData(null);
            apiResponse.setError(new ErrorResponse(
                    "Profile picture not found",
                    "User id: " + id + " has no profile picture in the database."
            ));
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }

        DpResponse userResponse = userDto.mapToDpResponse(userDp);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(userResponse);
        apiResponse.setError(null);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


//    DpResponse userResponse = userDto.mapToDpResponse(userDp);
//        apiResponse.setStatus(HttpStatus.OK.value());
//        apiResponse.setData(userResponse);
//        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//    }


}
