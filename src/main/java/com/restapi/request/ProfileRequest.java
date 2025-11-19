package com.restapi.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ProfileRequest {

    private String username;
    private String password;
    private String name;
    private long id;
    private String email;
    private long phone;
    private String address;
    private String role;
    private String gender;
}
