package com.restapi.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class FilteredEvents {
    private LocalDate fromDate;
    private LocalDate toDate;
    private int minPrice;
    private int maxPrice;
    private List<Long> checkedCategory;
    private List<Long> checkedVenue;
}
