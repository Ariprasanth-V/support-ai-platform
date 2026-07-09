package com.supportai.ticket.event;

import java.time.LocalDateTime;

public class TicketEvent {
    private String eventType;
    private Long ticketId;
    private String title;
    private String status;
    private Long customerId;
    private LocalDateTime occurredAt = LocalDateTime.now();

    public TicketEvent() {}

    public TicketEvent(String eventType, Long ticketId, String title, String status, Long customerId) {
        this.eventType = eventType;
        this.ticketId = ticketId;
        this.title = title;
        this.status = status;
        this.customerId = customerId;
    }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
}
