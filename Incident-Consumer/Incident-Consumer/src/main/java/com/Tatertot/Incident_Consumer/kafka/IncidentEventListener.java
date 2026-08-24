package com.Tatertot.Incident_Consumer.kafka;

import com.Tatertot.Incident_Consumer.event.IncidentEvent;
import com.Tatertot.Incident_Consumer.service.IncidentProcessingService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class IncidentEventListener {

    private final IncidentProcessingService incidentProcessingService;
    public IncidentEventListener(IncidentProcessingService incidentProcessingService) {
        this.incidentProcessingService = incidentProcessingService;
    }

    @KafkaListener(topics = "${app.kafka.incident-topic}")
    public void consume(ConsumerRecord<String, IncidentEvent> record) {
        IncidentEvent event = record.value();

        System.out.println(
                "RECIEVED: incident="
                + event.getIncidentId()
                + " sequence="
                + event.getSequenceNumber()
                + " partition= "
                + record.partition()
                + " offset="
                + record.offset()
                +  " THREAD="
                + Thread.currentThread().getName()
        );

        incidentProcessingService.process(event, record.partition(), record.offset());
    }
}
