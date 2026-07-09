package com.supportai.ticket.service;

import com.supportai.ticket.dto.*;
import com.supportai.ticket.entity.Comment;
import com.supportai.ticket.entity.Ticket;
import com.supportai.ticket.event.TicketEvent;
import com.supportai.ticket.event.TicketEventPublisher;
import com.supportai.ticket.repository.CommentRepository;
import com.supportai.ticket.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;
    private final TicketEventPublisher eventPublisher;

    public TicketService(TicketRepository ticketRepository,
                          CommentRepository commentRepository,
                          TicketEventPublisher eventPublisher) {
        this.ticketRepository = ticketRepository;
        this.commentRepository = commentRepository;
        this.eventPublisher = eventPublisher;
    }

    public Ticket createTicket(CreateTicketRequest request) {
        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setCustomerId(request.getCustomerId());
        Ticket saved = ticketRepository.save(ticket);

        eventPublisher.publish(new TicketEvent(
                "TicketCreated", saved.getId(), saved.getTitle(),
                saved.getStatus().name(), saved.getCustomerId()));

        return saved;
    }

    public Ticket getTicket(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found: " + id));
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public List<Ticket> getTicketsByCustomer(Long customerId) {
        return ticketRepository.findByCustomerId(customerId);
    }

    public Ticket updateStatus(Long id, String statusStr) {
        Ticket ticket = getTicket(id);
        Ticket.Status newStatus;
        try {
            newStatus = Ticket.Status.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + statusStr);
        }
        ticket.setStatus(newStatus);
        ticket.setUpdatedAt(LocalDateTime.now());
        Ticket saved = ticketRepository.save(ticket);

        eventPublisher.publish(new TicketEvent(
                "TicketUpdated", saved.getId(), saved.getTitle(),
                saved.getStatus().name(), saved.getCustomerId()));

        return saved;
    }

    public Comment addComment(Long ticketId, AddCommentRequest request) {
        getTicket(ticketId); // ensures ticket exists
        Comment comment = new Comment();
        comment.setTicketId(ticketId);
        comment.setAuthorId(request.getAuthorId());
        comment.setBody(request.getBody());
        return commentRepository.save(comment);
    }

    public List<Comment> getComments(Long ticketId) {
        return commentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId);
    }
}
