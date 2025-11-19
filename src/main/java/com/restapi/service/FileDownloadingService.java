package com.restapi.service;

import com.restapi.exception.common.ResourceNotFoundException;
import com.restapi.model.Category;
import com.restapi.model.Event;
import com.restapi.model.UserDP;
import com.restapi.repository.CategoryRepository;
import com.restapi.repository.DpRepository;
import com.restapi.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.persistence.Access;
import javax.xml.stream.Location;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Service
public class FileDownloadingService {
    @Autowired
    StorageService storageService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private DpRepository dpRepository;

    public File getFile(long id) throws IOException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id", "id", id));
        Resource resource = storageService.loadFileAsResource(category.getImage());
        return resource.getFile();
    }

    public File getEventImage(long id) throws IOException{
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id","id",id));
        Resource resource = storageService.loadFileAsResource(Arrays.toString(event.getImage()));
        return resource.getFile();
    }

    public File getDp(long id) throws IOException{
        UserDP userDP = dpRepository.findByUserId(id);
        Resource resource = storageService.loadFileAsResource(userDP.getDp());
        return resource.getFile();
    }
}