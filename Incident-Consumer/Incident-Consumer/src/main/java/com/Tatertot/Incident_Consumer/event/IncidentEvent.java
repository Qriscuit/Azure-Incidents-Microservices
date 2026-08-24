package com.Tatertot.Incident_Consumer.event;

import java.time.Instant;

public class IncidentEvent {

    private String incidentId;
    private String raisedBy;
    private String location;
    private String description;
    private String assignedTeam;
    private Priority priority;

    private String eventType;
    private int sequenceNumber;
    private Instant createdAt;

    public  IncidentEvent(){

    }

    public String getIncidentId() { return incidentId; }
    public void setIncidentId(String IncidentId) {this.incidentId = IncidentId;}

    public int getSequenceNumber() { return sequenceNumber; }
    public void setSequenceNumber(int sequenceNumber) {this.sequenceNumber = sequenceNumber;}

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) {this.createdAt = createdAt;}

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getRaisedBy() {
        return raisedBy;
    }
    public void setRaisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignedTeam() {return assignedTeam;}
    public void setAssignedTeam(String assignedTeam) { this.assignedTeam = assignedTeam; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
}
