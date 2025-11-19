package com.restapi.request;

import com.restapi.model.Event;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryRequest {
    private long id;
    private String name;
    private String image;
    private List<Event> events;
}
