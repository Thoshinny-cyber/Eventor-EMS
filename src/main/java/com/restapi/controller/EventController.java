package com.restapi.controller;

import com.restapi.dto.EventDto;
import com.restapi.model.Event;
import com.restapi.request.FilteredEvents;
import com.restapi.response.EventResponse;
import com.restapi.response.common.APIResponse;
import com.restapi.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("EventRegistration/API/User/Event")
public class EventController {

    @Autowired
    private APIResponse apiResponse;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventDto eventDto;

    @GetMapping
    public ResponseEntity<APIResponse> getAllEvents(){
        List<Event> eventList= eventService.findAll();
        List<EventResponse> eventResponses=eventDto.mapToEventResponse(eventList);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(eventResponses);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/Top")
    public ResponseEntity<APIResponse> getAllTopEvents(){
        List<Event> eventList= eventService.findTopEvents();
        List<EventResponse> eventResponses=eventDto.mapToEventResponse(eventList);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(eventResponses);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getAEvent(@PathVariable Long id) {
        Event event = eventService.findById(id);
        EventResponse eventResponse=eventDto.mapToSingleEventResponse(event);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(eventResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/filteredEvents")
    public ResponseEntity<APIResponse> getFilteredEvents(@RequestBody FilteredEvents events) {
        System.out.println("BarathEnteredcontroller");
        List<Event> event=eventService.getFilteredEvents(events);
        List<EventResponse> eventResponses=eventDto.mapToEventResponse(event);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(eventResponses);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


}
