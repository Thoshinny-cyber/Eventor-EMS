package com.restapi.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restapi.model.Category;
import com.restapi.model.Order;
import com.restapi.model.Seat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EventResponse {
        private long id;
        private String name;
        private String description;
        private String venue;
        private LocalDate date;
        private String host;
        private int price;
        private int availableTickets;
        private int soldTickets;
        private long categoryId;
        private List<String> seats;
}