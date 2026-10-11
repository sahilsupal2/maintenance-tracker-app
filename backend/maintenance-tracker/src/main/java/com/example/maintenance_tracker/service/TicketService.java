package com.example.maintenance_tracker.service;

import com.example.maintenance_tracker.dto.CreateTicketRequest;
import com.example.maintenance_tracker.entity.MaintenanceTicket;
import com.example.maintenance_tracker.entity.Unit;
import com.example.maintenance_tracker.repository.MaintenanceTicketRepository;
import com.example.maintenance_tracker.repository.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final MaintenanceTicketRepository ticketRepository;
    private final UnitRepository unitRepository;

    // Constructor-based dependency injection
    public TicketService(MaintenanceTicketRepository ticketRepository, UnitRepository unitRepository) {
        this.ticketRepository = ticketRepository;
        this.unitRepository = unitRepository;
    }

    public List<MaintenanceTicket> getAllTickets(String status) {
        if (status != null && !status.trim().isEmpty()) {
            return ticketRepository.findByStatusIgnoreCase(status.trim());
        }
        return ticketRepository.findAll();
    }

    public Optional<MaintenanceTicket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public MaintenanceTicket createTicket(CreateTicketRequest request) {
        Unit unit = unitRepository.findById(request.getUnitId())
                .orElseThrow(() -> new RuntimeException("Unit not found with id: " + request.getUnitId()));

        MaintenanceTicket ticket = new MaintenanceTicket(
                unit,
                request.getCategory(),
                request.getDescription(),
                "OPEN"
        );
        return ticketRepository.save(ticket);
    }

    public MaintenanceTicket updateTicketStatus(Long id, String newStatus) {
        MaintenanceTicket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));

        ticket.setStatus(newStatus.toUpperCase());
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new RuntimeException("Ticket not found with id: " + id);
        }
        ticketRepository.deleteById(id);
    }
}