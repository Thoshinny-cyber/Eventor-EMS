package com.restapi.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReportResponse {
    private long eventId;
    private String eventName;
    private LocalDate date;
    private int tickets_sold;
    private int available_tickets;
    private int Revenue;
}
