package com.Tatertot.Incident_Producer.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class IncidentCounterRepository {

    private final JdbcTemplate jdbcTemplate;

    public IncidentCounterRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long reserveRange(int incidentCount) {
        if (incidentCount <= 0) {
            throw new IllegalArgumentException(
                    "Incident count must be positive"
            );
        }

        Long lastNumber = jdbcTemplate.queryForObject(
                """
                UPDATE public.incident_counters
                SET last_number = last_number + ?
                WHERE counter_name = 'batch_incident'
                RETURNING last_number
                """,
                Long.class,
                incidentCount
        );

        if (lastNumber == null) {
            throw new IllegalStateException(
                    "Database did not return an incident number"
            );
        }

        return lastNumber;
    }
}