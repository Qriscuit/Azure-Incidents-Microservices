package com.Tatertot.Incident_Consumer.service;

import com.Tatertot.Incident_Consumer.entity.IncidentEventEntity;
import com.Tatertot.Incident_Consumer.event.IncidentEvent;
import com.Tatertot.Incident_Consumer.repository.IncidentEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IncidentProcessingService {

    private final IncidentEventRepository repository;

    public IncidentProcessingService(IncidentEventRepository repository)
    {
        this.repository = repository;
    }

    @Transactional
    public void process(IncidentEvent event, int kafkaPartition, long kafkaOffset){
        IncidentEventEntity entity = new IncidentEventEntity();

        entity.setIncidentId(event.getIncidentId());
        entity.setSequenceNumber(event.getSequenceNumber());
        entity.setEventType(event.getEventType());

        entity.setRaisedBy(event.getRaisedBy());
        entity.setLocation(event.getLocation());
        entity.setDescription(event.getDescription());
        entity.setAssignedTeam(event.getAssignedTeam());
        entity.setPriority(event.getPriority());

        entity.setCreatedAt(event.getCreatedAt());

        entity.setKafkaPartition(kafkaPartition);
        entity.setKafkaOffset(kafkaOffset);

        repository.save(entity);
    }

}
