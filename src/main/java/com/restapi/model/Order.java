package com.restapi.model;

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
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private AppUser users;

    @ManyToOne()
    @JoinColumn(name = "eventId",referencedColumnName = "id")
    private Event event;
    @Transient
    public Long getEventId() {
        return event != null ? event.getId() : null;
    }

    private int count;

    @OneToMany(mappedBy = "order")
    private List<Seat> seat;

    @Column
    private String paymentStatus; // PAID, PENDING, FAILED

    @OneToOne(mappedBy = "order")
    private Invoice invoice;

    @Column(name = "payment_intent_id")
    private String paymentIntentId;
   // private Long eventId;
//
//    public Long getEventId() {
//        return eventId;
//    }
//    public void setEventId(Long eventId) {
//        this.eventId = eventId;
//    }
}

