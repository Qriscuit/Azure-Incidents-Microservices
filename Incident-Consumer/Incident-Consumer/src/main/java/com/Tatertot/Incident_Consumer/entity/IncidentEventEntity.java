package com.Tatertot.Incident_Consumer.entity;


import com.Tatertot.Incident_Consumer.event.Priority;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "incident_events",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "incident_sequence",
                        columnNames = {"incident_id", "sequence_number"}
                )
        }
)
public class IncidentEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "incident_id", nullable = false)
    private String incidentId;

    @Column(name = "sequence_number", nullable = false)
    private int sequenceNumber;

    private String eventType;

    private String raisedBy;

    private String location;

    private String description;

    private String assignedTeam;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private Instant createdAt;

    private int kafkaPartition;

    private long kafkaOffset;

    public IncidentEventEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(String incidentId) {
        this.incidentId = incidentId;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

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

    public String getAssignedTeam() {
        return assignedTeam;
    }

    public void setAssignedTeam(String assignedTeam) {
        this.assignedTeam = assignedTeam;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public int getKafkaPartition() {
        return kafkaPartition;
    }

    public void setKafkaPartition(int kafkaPartition) {
        this.kafkaPartition = kafkaPartition;
    }

    public long getKafkaOffset() {
        return kafkaOffset;
    }

    public void setKafkaOffset(long kafkaOffset) {
        this.kafkaOffset = kafkaOffset;
    }
}
