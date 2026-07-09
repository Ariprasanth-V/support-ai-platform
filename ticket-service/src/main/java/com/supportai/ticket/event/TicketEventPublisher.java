package com.supportai.ticket.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TicketEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(TicketEventPublisher.class);
    private static final String TOPIC = "ticket-events";

    private final KafkaTemplate<String, TicketEvent> kafkaTemplate;

    public TicketEventPublisher(KafkaTemplate<String, TicketEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(TicketEvent event) {
        kafkaTemplate.send(TOPIC, event.getTicketId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish {} for ticket {}: {}",
                                event.getEventType(), event.getTicketId(), ex.getMessage());
                    } else {
                        log.info("Published {} for ticket {}", event.getEventType(), event.getTicketId());
                    }
                });
    }
}
