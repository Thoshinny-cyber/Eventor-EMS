package com.restapi.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class SeatRequest {
    @NotBlank
    private String seatNumber;      // ✅ Changed from seatnumber
    private boolean isSeatBooked;   // ✅ Changed from isbooked

    // These will be set in the service, not from frontend
    private Long userid;
    private Long eventid;
    private Long orderid;

}