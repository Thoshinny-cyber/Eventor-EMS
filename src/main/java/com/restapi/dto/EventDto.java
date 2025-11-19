package com.restapi.dto;

import com.restapi.model.Event;
import com.restapi.model.Seat;
import com.restapi.request.EventRequest;
import com.restapi.response.EventResponse;
import com.restapi.response.ReportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventDto {

    public Event mapToEvent(EventRequest eventRequest){
        Event event=new Event();
        event.setId(eventRequest.getId());
        event.setHost(eventRequest.getHost());
        event.setDate(eventRequest.getDate());
        event.setName(eventRequest.getName());
        event.setDescription(eventRequest.getDescription());
        event.setPrice(eventRequest.getPrice());
        event.setAvailableTickets(eventRequest.getAvailableTickets());
        event.setVenue(eventRequest.getVenue());
        event.setImage(eventRequest.getImage().getBytes());
        return event;
    }

    public List<ReportResponse> mapToReport(List<Event> eventList) {
        List<ReportResponse> rs=new ArrayList<>();
        for (Event event : eventList) {
            ReportResponse reportResponse = new ReportResponse();
            reportResponse.setEventName(event.getName());
            reportResponse.setDate(event.getDate());
            reportResponse.setTickets_sold(event.getSoldTickets());
            reportResponse.setRevenue(event.getPrice()* event.getSoldTickets());
            reportResponse.setEventId(event.getId());
            reportResponse.setAvailable_tickets(event.getAvailableTickets());
            rs.add(reportResponse);
        }
        return rs;
    }

//    public List<EventResponse> mapToEventResponse(List<Event> event){
//        List<EventResponse> rs=new ArrayList<>();
//        for(int i=0;i<event.size();i++){
//            EventResponse eventResponse=new EventResponse();
//            eventResponse.setPrice(event.get(i).getPrice());
//            eventResponse.setHost(event.get(i).getHost());
//            eventResponse.setName(event.get(i).getName());
//            eventResponse.setDate(event.get(i).getDate());
//            eventResponse.setDescription(event.get(i).getDescription());
//            eventResponse.setVenue(event.get(i).getVenue());
//            eventResponse.setSoldTickets(event.get(i).getSoldTickets());
//            eventResponse.setCategoryId(event.get(i).getCategory().getId());
//            eventResponse.setId(event.get(i).getId());
//            eventResponse.setAvailableTickets(event.get(i).getAvailableTickets());
//            eventResponse.setSeats(mapToEventSeatResponse(event.get(i).getSeat()));
//            rs.add(eventResponse);
//        }
//        return rs;
//    }

    // ✅ Convert List<Event> → List<EventResponse>
    public List<EventResponse> mapToEventResponse(List<Event> eventList) {
        List<EventResponse> responseList = new ArrayList<>();
        if (eventList == null || eventList.isEmpty()) {
            return responseList;
        }
        for (Event event : eventList) {
            responseList.add(mapToSingleEventResponse(event));
        }
        return responseList;
    }


    public EventResponse mapToSingleEventResponse(Event event){
        if (event == null) {
            return null;}
            EventResponse eventResponse=new EventResponse();
            eventResponse.setPrice(event.getPrice());
            eventResponse.setHost(event.getHost());
            eventResponse.setName(event.getName());
            eventResponse.setDate(event.getDate());
            eventResponse.setDescription(event.getDescription());
            eventResponse.setVenue(event.getVenue());
            eventResponse.setSoldTickets(event.getSoldTickets());
            eventResponse.setCategoryId(event.getCategory().getId());
            eventResponse.setId(event.getId());
            eventResponse.setAvailableTickets(event.getAvailableTickets());
            eventResponse.setSeats(mapToEventSeatResponse(event.getSeat()));
        return eventResponse;
    }

//    public List<String> mapToEventSeatResponse(List<Seat> seats){
//        List<String> bookedSeats=new ArrayList<>();
//        if (seats == null) {
//            return bookedSeats;
//        }
//        for (Seat seat : seats) {
//            bookedSeats.add(seat.getSeatNumber());
//        }
//        return bookedSeats;
//    }
// EventDto.java
public List<String> mapToEventSeatResponse(List<Seat> seats) {
    if (seats == null) return Collections.emptyList();
    return seats.stream()
            .map(Seat::getSeatNumber)
            .collect(Collectors.toList());
}
}
