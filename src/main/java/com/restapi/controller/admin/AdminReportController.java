package com.restapi.controller.admin;

import com.restapi.dto.EventDto;
import com.restapi.model.Event;
import com.restapi.response.ReportResponse;
import com.restapi.response.common.APIResponse;
import com.restapi.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("EventRegistration/API/Admin/Report")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminReportController {

    @Autowired
    private APIResponse apiResponse;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventDto eventDto;

    @GetMapping
    public ResponseEntity<APIResponse> getReports(){
        List<Event> eventList= eventService.getReport();
        List<ReportResponse> reportResponse=eventDto.mapToReport(eventList);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(reportResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
