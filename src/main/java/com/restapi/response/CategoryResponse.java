package com.restapi.response;

import com.restapi.model.Event;
import com.restapi.request.CEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryResponse {
    private Long id;
    private String name;
    private List<CEvent> events;
    private String image;
    private int eventsCount;
}
