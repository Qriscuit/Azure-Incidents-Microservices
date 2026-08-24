package com.Tatertot.Incident_Producer.kafka;

import com.Tatertot.Incident_Producer.event.IncidentEvent;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.util.concurrent.CompletableFuture;

@Component
public class IncidentEventProducer {

    @Value("${app.Kafka.incident-topic}")
    private String TOPIC;

    private final KafkaTemplate<String, IncidentEvent> kafkaTemplate;

    public IncidentEventProducer(KafkaTemplate<String, IncidentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<SendResult<String, IncidentEvent>> sendIncidentEvent(IncidentEvent event) {
        return kafkaTemplate.send(TOPIC, event.getIncidentId(), event
        ).whenComplete((result, exception) ->{
            if (exception != null) {
                System.err.println(
                        "FAILED: "
                                + event.getIncidentId()
                                + " sequence "
                                + event.getSequenceNumber()
                );
                exception.printStackTrace();
                return;
            }

            System.out.println(
                    "SENT: "
                            + event.getIncidentId()
                            + " sequence="
                            + event.getSequenceNumber()
                            + " partition="
                            + result.getRecordMetadata().partition()
                            + " offset="
                            + result.getRecordMetadata().offset()
            );
        });
    }
}
