package com.Tatertot.Incident_Consumer.repository;


import com.Tatertot.Incident_Consumer.entity.IncidentEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentEventRepository
    extends JpaRepository<IncidentEventEntity, Long>
{
}
