package com.Tatertot.Incident_Producer.dto;

import java.util.List;

public record BatchResponse (
        String batchId,
        int incidentCount,
        int eventsPerIncident,
        long messageCount,
        long firstIncidentNumber,
        long lastIncidentNumber,
        List<String> incidentIds
)
{

}
