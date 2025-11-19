package com.restapi.service;

import com.restapi.model.Category;
import com.restapi.model.Event;
import com.restapi.repository.CategoryRepository;
import com.restapi.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

//    @Cacheable(value = "Category", key = "'allCategory'")
    public List<Category> findAll() {
//        String key="Eventor_Categories";
//        HashOperations<String, Long, List<Category>> hashOperations = redisTemplate.opsForHash();
//
//        if (redisTemplate.hasKey(key)) {
//            Map<Long, List<Category>> cachedEvents = hashOperations.entries(key);
//            System.out.println("Retrieved from cache: " + cachedEvents.toString());
//            return cachedEvents.values().stream().flatMap(List::stream).toList();
//        }
//
//        // Store & Retrieve a HashMap
//            Map<Long, List<Category> > Events = new HashMap<>();
//            for(int i=0;i<categoryRepository.findAll().size();i++){
//                Events.computeIfAbsent(categoryRepository.findAll().get(i).getId(), k -> new ArrayList<>()).add(categoryRepository.findAll().get(i));
//            }
//            System.out.println("BarathEventsMap"+Events.toString());
//
//            hashOperations.putAll(key, Events);
        return categoryRepository.findAll();
    }

    public  List<Category> addCategory(Category category) {
        categoryRepository.save(category);
        return findAll();
    }

    public List<Category> updateCategory(Category category){
        categoryRepository.save(category);
        return findAll();
    }

    public List<Category> deleteCategory(Long id){
        categoryRepository.deleteById(id);
        return findAll();
    }

    public List<Event> findACategory(Long id) {
        return eventRepository.findCategory(id);

    }

    public Optional<Category> showCategory(Long id) {
        return categoryRepository.findById(id);
    }
}
