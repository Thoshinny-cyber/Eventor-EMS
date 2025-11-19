package com.restapi.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
public class OrderRequest {
    @NotNull
    private Long userId;
    @NotNull private Long eventId;
    @Min(1) private Integer count;
    @Positive
    private Double totalPrice;
    @NotBlank
    private String paymentStatus;
    @NotEmpty
    private List<@Valid SeatRequest> bookedSeats;         // NEW FIELD

}