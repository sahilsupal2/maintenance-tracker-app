package com.example.maintenance_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.maintenance_tracker.entity.MaintenanceTicket;

import java.util.List;

@Repository
public interface MaintenanceTicketRepository extends JpaRepository<MaintenanceTicket, Long> {
    // Custom query method: Spring Data JPA automatically writes the SQL query based on method name
    List<MaintenanceTicket> findByStatusIgnoreCase(String status);
}
