package com.restapi.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String password;
    private String confirmPassword;
    private String name;
    private String email;
    private String gender;
    private Long phone;
    private String role; // "vendor" for admin, "user" for regular user
}
