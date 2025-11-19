package com.restapi.response;

import com.restapi.model.Seat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private Long userId;
    private String userName;
    private Long eventId;
    private String eventName;
    private int count;
    private long totalPrice;
    private LocalDate date;
   // private List<Seat> bookedSeats;
    private List<String> bookedSeatsString;
    private String paymentStatus;
    private String venue;
//    private List<BookedUser> bookedUserList;
}
