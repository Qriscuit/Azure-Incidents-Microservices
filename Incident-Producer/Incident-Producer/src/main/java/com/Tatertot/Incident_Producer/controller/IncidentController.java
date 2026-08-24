package com.Tatertot.Incident_Producer.controller;

import com.Tatertot.Incident_Producer.dto.IncidentRequest;
import com.Tatertot.Incident_Producer.event.IncidentEvent;
import com.Tatertot.Incident_Producer.service.IncidentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @PostMapping
    public ResponseEntity<IncidentEvent> createIncident(@RequestBody IncidentRequest incidentRequest) {

        IncidentEvent event = incidentService.createIncident(incidentRequest);

        return ResponseEntity.ok(event);


    }

    @PostMapping("/batch")
    public ResponseEntity<String> generateBatch(){
        int messageCount = incidentService.generateBatchEvents();
        return ResponseEntity.ok("Generated "+messageCount + " kafka messages");
    }

}
