package com.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seat implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    private String seatNumber;
    @JsonProperty("isSeatBooked") // ✅ This tells Jackson to use "isSeatBooked" in JSON
    private boolean isSeatBooked;

    @ManyToOne
    @JoinColumn(name = "orderId",referencedColumnName = "id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "userId",referencedColumnName = "id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "eventId",referencedColumnName = "id")
    private Event event;
}
