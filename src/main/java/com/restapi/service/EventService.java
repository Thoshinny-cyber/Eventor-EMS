package com.restapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapi.dto.EventDto;
import com.restapi.exception.common.ResourceNotFoundException;
import com.restapi.model.Category;
import com.restapi.model.Event;
import com.restapi.repository.CategoryRepository;
import com.restapi.repository.EventRepository;
import com.restapi.request.EventRequest;
import com.restapi.request.FilteredEvents;
import lombok.SneakyThrows;
import net.bytebuddy.pool.TypePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventDto eventDto;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

//    @Cacheable(value = "Event", key = "'allEvent'")
//    public  List<Event> findAll() {
////        String key="Eventor_Events";
////        HashOperations<String, Long, List<Event>> hashOperations = redisTemplate.opsForHash();
////
////        if (redisTemplate.hasKey(key)) {
////            Map<Long, List<Event>> cachedEvents = hashOperations.entries(key);
//////            System.out.println("Retrieved from cache: " + cachedEvents.toString());
////            return cachedEvents.values().stream().flatMap(List::stream).toList();
////        }
////
////        // Store & Retrieve a HashMap
////            Map<Long, List<Event> > Events = new HashMap<>();
////            for(int i=0;i<eventRepository.findAll().size();i++){
////                Events.computeIfAbsent(eventRepository.findAll().get(i).getId(), k -> new ArrayList<>()).add(eventRepository.findAll().get(i));
////            }
//////            System.out.println("BarathEventsMap"+Events.toString());
////
////            hashOperations.putAll(key, Events);
//
//        return eventRepository.findAll();
//    }

public List<Event> findAll() {
    List<Event> events = eventRepository.findAll();
    System.out.println("findAll(): Returning " + events.size() + " events");
    return events;
}

    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Id","Id",id));
    }

    public Event createEvent(EventRequest eventRequest) {
        Event event = eventDto.mapToEvent(eventRequest);

        // Set Category
        if (eventRequest.getCategoryId() != null) {
            Category category = categoryRepository.findById(eventRequest.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("category", "id", eventRequest.getCategoryId()));
            event.setCategory(category);
        }

        // Save the event
        Event savedEvent = eventRepository.save(event);

        // Log for debugging
        System.out.println("EVENT CREATED SUCCESSFULLY: ID=" + savedEvent.getId() + ", Name=" + savedEvent.getName());

        return savedEvent;// Return the created Event, NOT List<Event>

    }


//    public List<Event> updateEvent(EventRequest eventRequest) {
//        Event event=eventDto.mapToEvent(eventRequest);
//        Category category=categoryRepository.findById(eventRequest.getCategoryId())
//                .orElseThrow(()-> new ResourceNotFoundException("category","category", eventRequest.getCategoryId()));
//        event.setCategory(category);
//        eventRepository.save(event);
//        return findAll();
//    }
public Event updateEvent(EventRequest eventRequest) {
    System.out.println("UPDATING EVENT: ID=" + eventRequest.getId());

    if (eventRequest.getId() == null) {
        throw new IllegalArgumentException("Event ID is required");
    }

    Event event = eventRepository.findById(eventRequest.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventRequest.getId()));

    // UPDATE FIELDS
    event.setName(eventRequest.getName());
    event.setDescription(eventRequest.getDescription());
    event.setVenue(eventRequest.getVenue());
    event.setDate(eventRequest.getDate());
    event.setHost(eventRequest.getHost());
    event.setPrice(eventRequest.getPrice());
    event.setAvailableTickets(eventRequest.getAvailableTickets());
    if (eventRequest.getImage() != null && !eventRequest.getImage().isEmpty()) {
        event.setImage(eventRequest.getImage().getBytes());
    }

    if (eventRequest.getCategoryId() != null) {
        Category category = categoryRepository.findById(eventRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", eventRequest.getCategoryId()));
        event.setCategory(category);
    }

    Event saved = eventRepository.save(event);
    System.out.println("EVENT SAVED TO DB: ID=" + saved.getId() + ", Venue=" + saved.getVenue());

    return saved;
}

    public List<Event> deleteById(Long id) {
        eventRepository.deleteById(id);
        return findAll();
    }

    public List<Event> getReport() {
        return eventRepository.findAll();
    }

    public List<Event> findTopEvents() {
        return eventRepository.findAllTopEvents();
    }

    public List<Event> getFilteredEvents(FilteredEvents events){
        if(!events.getCheckedCategory().isEmpty() && !events.getCheckedVenue().isEmpty()){
            return eventRepository.findFilteredEvents(events.getFromDate(),events.getToDate(),events.getMinPrice(),events.getMaxPrice(),events.getCheckedCategory(),events.getCheckedVenue());
        } else if (events.getCheckedCategory().isEmpty() && events.getCheckedVenue().isEmpty()) {
            return eventRepository.findByDateBetweenAndPriceBetween(events.getFromDate(),events.getToDate(),events.getMinPrice(),events.getMaxPrice());
        } else if (events.getCheckedVenue().isEmpty()) {
            return eventRepository.findFilteredEventsWithoutVenues(events.getFromDate(),events.getToDate(),events.getMinPrice(),events.getMaxPrice(),events.getCheckedCategory());
        }
        else {
            return eventRepository.findByDateBetweenAndPriceBetweenAndIdIn(events.getFromDate(),events.getToDate(),events.getMinPrice(),events.getMaxPrice(),events.getCheckedVenue());
        }
    }
}
