package com.restapi.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private long id;
    private String name;
    private String username;
    private String gender;
    private String email;
    private long phone;
    private String password;
    private String address;
    private String role;
    private String image;
}
