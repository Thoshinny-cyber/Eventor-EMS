package com.restapi.dto;

import com.restapi.model.Category;
import com.restapi.model.Event;
import com.restapi.request.CEvent;
import com.restapi.request.CategoryRequest;
import com.restapi.response.CategoryResponse;
import com.restapi.response.EventResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryDto {
    @Autowired
    private EventDto eventDto;
    public List<CategoryResponse> mapToCategoryResponse(List<Category> categoryList) {
        List<CategoryResponse> rs=new ArrayList<>();
        for(int i=0;i<categoryList.size();i++){
            CategoryResponse categoryResponse=new CategoryResponse();
            categoryResponse.setId(categoryList.get(i).getId());
            categoryResponse.setEvents(mapToCEventResponse(categoryList.get(i).getEvents()));
            categoryResponse.setName(categoryList.get(i).getName());
            categoryResponse.setEventsCount(categoryList.get(i).getEvents().size());
            rs.add(categoryResponse);
        }
        return rs;
    }

    public List<CEvent> mapToCEventResponse(List<Event> event){
        List<CEvent> rs=new ArrayList<>();
        for (int i=0;i<event.size();i++){
            CEvent cEvent=new CEvent();
            cEvent.setName(event.get(i).getName());
            rs.add(cEvent);
        }
        return rs;
    }

    public List<EventResponse> mapToEventResponse(List<Event> categoryList) {
        List<EventResponse> rs=new ArrayList<>();
        for(int i=0;i<categoryList.size();i++){
            EventResponse eventResponse=new EventResponse();
            eventResponse.setCategoryId(categoryList.get(i).getCategory().getId());
            eventResponse.setSeats(eventDto.mapToEventSeatResponse(categoryList.get(i).getSeat()));
            eventResponse.setAvailableTickets(categoryList.get(i).getAvailableTickets());
            eventResponse.setDate(categoryList.get(i).getDate());
            eventResponse.setVenue(categoryList.get(i).getVenue());
            eventResponse.setDescription(categoryList.get(i).getDescription());
            eventResponse.setSoldTickets(categoryList.get(i).getSoldTickets());
            eventResponse.setHost(categoryList.get(i).getHost());
            eventResponse.setId(categoryList.get(i).getId());
            eventResponse.setName(categoryList.get(i).getName());
            eventResponse.setPrice(categoryList.get(i).getPrice());
            rs.add(eventResponse);
        }
        return rs;
    }
}
