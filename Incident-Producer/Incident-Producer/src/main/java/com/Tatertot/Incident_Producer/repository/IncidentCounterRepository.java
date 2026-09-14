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
    public long reserveRangeStart(int incidentCount) {
        if (incidentCount <= 0) {
            throw new IllegalArgumentException(
                    "Incident count must be positive"
            );
        }

        Long firstNumber = jdbcTemplate.queryForObject(
                """
                INSERT INTO public.incident_counters (counter_name, last_number)
                VALUES ('batch_incident', ?)
                ON CONFLICT (counter_name)
                DO UPDATE SET last_number =
                    public.incident_counters.last_number + EXCLUDED.last_number
                RETURNING last_number - ? + 1
                """,
                Long.class,
                incidentCount,
                incidentCount
        );

        if (firstNumber == null) {
            throw new IllegalStateException(
                    "Database did not return an incident number"
            );
        }

        return firstNumber;
    }
}
