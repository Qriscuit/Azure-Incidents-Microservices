package com.Tatertot.Incident_Producer.service;

import com.Tatertot.Incident_Producer.dto.IncidentRequest;
import com.Tatertot.Incident_Producer.dto.BatchResponse;
import com.Tatertot.Incident_Producer.repository.IncidentCounterRepository;
import com.Tatertot.Incident_Producer.dto.Priority;
import com.Tatertot.Incident_Producer.event.IncidentEvent;
import com.Tatertot.Incident_Producer.kafka.IncidentEventProducer;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class IncidentService {

    private final IncidentEventProducer incidentEventProducer;
    private final IncidentCounterRepository incidentCounterRepository;

    @Value("${app.batch.number-of-incidents}")
    int numberOfIncidents;
    
    @Value("${app.batch.events-per-incident}")
    int eventsPerIncident;

    public IncidentService(IncidentEventProducer incidentEventProducer, IncidentCounterRepository incidentCounterRepository) {
        this.incidentEventProducer = incidentEventProducer;
        this.incidentCounterRepository = incidentCounterRepository;
    }

    public IncidentEvent createIncident(IncidentRequest request){
        IncidentEvent event = new IncidentEvent();

        event.setIncidentId(UUID.randomUUID().toString());

        event.setRaisedBy(request.getRaisedBy());
        event.setAssignedTeam(request.getAssignedTeam());
        event.setDescription(request.getDescription());
        event.setLocation(request.getLocation());
        event.setPriority(request.getPriority());

        event.setCreatedAt(Instant.now());
        event.setSequenceNumber(1);
        event.setEventType("INCIDENT_RAISED");

        incidentEventProducer.sendIncidentEvent(event);

        return event;
    }

    public BatchResponse generateBatchEvents(){

        if (numberOfIncidents <= 0 || eventsPerIncident <= 0) {
            throw new IllegalStateException(
                    "Batch incident and event counts must be positive"
            );
        }

        String batchId = UUID.randomUUID().toString().substring(0, 8);
        long lastIncidentNumber = incidentCounterRepository.reserveRange(numberOfIncidents);
        long firstIncidentNumber = lastIncidentNumber - numberOfIncidents + 1;
        List<String> incidentIds = new ArrayList<>(numberOfIncidents);

        ExecutorService executor = Executors.newFixedThreadPool(numberOfIncidents);

        try
        {
            List<Future<?>> tasks = new ArrayList<>();

            for (int index = 0; index < numberOfIncidents; index++) {
                final long currentIncidentNumber = firstIncidentNumber + index;

                String incidentId =
                        "BATCH-" + batchId + "-INC-" + currentIncidentNumber;

                incidentIds.add(incidentId);

                Future<?> task = executor.submit(() -> {
                    for (int seq = 1; seq <= eventsPerIncident; seq++) {
                        IncidentEvent event = new IncidentEvent();

                        event.setIncidentId(incidentId);
                        event.setRaisedBy("Batch Generator");
                        event.setAssignedTeam("DEBUG");
                        event.setLocation("SITE-" + currentIncidentNumber);
                        event.setDescription("Generated test incident event");
                        event.setPriority(Priority.HIGH);

                        event.setSequenceNumber(seq);
                        event.setCreatedAt(Instant.now());

                        if (seq == 1) {
                            event.setEventType("INCIDENT_CREATED");
                        } else if (seq == eventsPerIncident) {
                            event.setEventType("INCIDENT_RESOLVED");
                        } else {
                            event.setEventType("INCIDENT_UPDATED");
                        }

                        //incidentEventProducer.sendIncidentEvent(event);

                        try{
                            SendResult<String, IncidentEvent> result =
                                    incidentEventProducer.sendIncidentEvent(event).get();

                            System.out.println(
                                    "ACKNOWLEDGED: "
                                            + incidentId
                                            + " sequence=" + seq
                                            + " partition="
                                            + result.getRecordMetadata().partition()
                                            + " offset="
                                            + result.getRecordMetadata().offset());
                        }
                        catch(InterruptedException e){
                            Thread.currentThread().interrupt();

                            throw new RuntimeException(
                                    "Thread interrupted while publishing incident event "
                                    + incidentId, e
                            );
                        }
                        catch (ExecutionException e){
                            throw new RuntimeException(
                                    "kafka failed to publish " + incidentId + " sequence " + seq,
                                    e
                            );
                        }
                    }
                });

                tasks.add(task);
            }

            for (Future<?> task : tasks) {
                try {
                    task.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Batch Generation interrupted", e);
                } catch (ExecutionException e) {
                    throw new RuntimeException("Batch Generation Failed", e);
                }
            }

            return new BatchResponse(
                    batchId,
                    numberOfIncidents,
                    eventsPerIncident,
                    (long) numberOfIncidents * eventsPerIncident,
                    firstIncidentNumber,
                    lastIncidentNumber,
                    List.copyOf(incidentIds)
            );

        } finally {
            executor.shutdown();
        }
    }
}
