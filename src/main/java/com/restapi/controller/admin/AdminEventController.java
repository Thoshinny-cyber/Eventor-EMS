package com.restapi.controller.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.restapi.model.Event;
import com.restapi.request.EventRequest;
import com.restapi.response.common.APIResponse;
import com.restapi.service.EventService;
import com.restapi.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("EventRegistration/API/Admin/Event")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminEventController {

    @Autowired
    private APIResponse apiResponse;

    @Autowired
    private EventService eventService;

    @Autowired
    private StorageService storageService;

//    @GetMapping("/all")
//    public ResponseEntity<APIResponse> getAllEvents(){
//        List<Event> eventList= eventService.findAll();
//        apiResponse.setStatus(HttpStatus.OK.value());
//        apiResponse.setData(eventList);
//        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
//    }
// In AdminEventController
@GetMapping("/all")
public ResponseEntity<APIResponse> getAllEvents() {
    List<Event> events = eventService.findAll();

    System.out.println("ADMIN API: DB has " + events.size() + " events");

    List<Map<String, Object>> dto = events.stream().map(e -> {
        Map<String, Object> map = new HashMap<>();
        map.put("id", e.getId());
        map.put("name", e.getName());
        map.put("description", e.getDescription());
        map.put("venue", e.getVenue());
        map.put("date", e.getDate()); // LocalDate → JSON: "2025-11-27"
        map.put("host", e.getHost());
        map.put("price", e.getPrice());
        map.put("availableTickets", e.getAvailableTickets());
        map.put("soldTickets", e.getSoldTickets());
        map.put("image", e.getImage()); // filename only
        map.put("imageUrl", e.getImage() != null ? "/api/events/image/" + e.getId() : null);

        if (e.getCategory() != null) {
            map.put("categoryId", e.getCategory().getId());
            map.put("categoryName", e.getCategory().getName());
        }

        return map;
    }).collect(Collectors.toList());

    System.out.println("ADMIN API: Returning " + dto.size() + " DTOs");

    apiResponse.setStatus(HttpStatus.OK.value());
    apiResponse.setData(dto);
    return new ResponseEntity<>(apiResponse, HttpStatus.OK);
}

    @GetMapping("/image/{eventId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long eventId) {
        Event event = eventService.findById(eventId);
        if (event.getImage() == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(event.getImage());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getAEvent(@PathVariable Long id){
        Event event= eventService.findById(id);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(event);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<APIResponse> addEvent(
            @RequestParam MultipartFile image,
            @RequestParam(required = false) Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String venue,
            @RequestParam String date,
            @RequestParam String host,
            @RequestParam int price,
            @RequestParam int availableTickets,
            @RequestParam Long categoryId
    ) {
        EventRequest eventRequest = new EventRequest();
        String fileName = storageService.storeFile(image);
        eventRequest.setImage(fileName);
        eventRequest.setId(id != null ? id : 0L);
        eventRequest.setName(name);
        eventRequest.setDescription(description);
        eventRequest.setVenue(venue);
        eventRequest.setHost(host);
        eventRequest.setPrice(price);
        eventRequest.setAvailableTickets(availableTickets);
        eventRequest.setCategoryId(categoryId);

        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        eventRequest.setDate(localDate);

        // SAVE + LOG
        Event savedEvent = eventService.createEvent(eventRequest);
        System.out.println("EVENT SAVED IN DB: ID=" + savedEvent.getId());

        // REFRESH LIST
        List<Event> allEvents = eventService.findAll();
        System.out.println("REFRESHED LIST: " + allEvents.size() + " events");

        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(allEvents);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<APIResponse> updateEvent(@RequestBody EventRequest eventRequest) {
        System.out.println("PUT REQUEST: " + eventRequest.getVenue());

        Event updated = eventService.updateEvent(eventRequest);

        // RETURN FRESH LIST
        List<Event> freshList = eventService.findAll();
        System.out.println("RETURNING " + freshList.size() + " EVENTS");

        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(freshList);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteEventById(@PathVariable Long id){
        List<Event> eventList= eventService.deleteById(id);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(eventList);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
