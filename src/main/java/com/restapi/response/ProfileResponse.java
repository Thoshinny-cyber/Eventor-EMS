package com.restapi.response;

import com.mysql.cj.exceptions.StreamingNotifiable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse {
    private long id;
    private String name;
    private String username;
    private String password;
    private String email;
    private long phone;
    private String gender;
    private String address;
    private String role;
}
