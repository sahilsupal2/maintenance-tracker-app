package com.example.maintenance_tracker.controller;

import com.example.maintenance_tracker.dto.CreateTicketRequest;
import com.example.maintenance_tracker.dto.UpdateStatusRequest;
import com.example.maintenance_tracker.entity.MaintenanceTicket;
import com.example.maintenance_tracker.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*") // Allows calls from mobile apps and web frontends
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // 1. GET all tickets (with optional filter: ?status=OPEN)
    @GetMapping
    public ResponseEntity<List<MaintenanceTicket>> getAllTickets(
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(ticketService.getAllTickets(status));
    }

    // 2. GET ticket by ID
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceTicket> getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. POST create a ticket
    @PostMapping
    public ResponseEntity<MaintenanceTicket> createTicket(@RequestBody CreateTicketRequest request) {
        MaintenanceTicket created = ticketService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // 4. PATCH update status
    @PatchMapping("/{id}/status")
    public ResponseEntity<MaintenanceTicket> updateTicketStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest request) {
        MaintenanceTicket updated = ticketService.updateTicketStatus(id, request.getStatus());
        return ResponseEntity.ok(updated);
    }

    // 5. DELETE a ticket
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}