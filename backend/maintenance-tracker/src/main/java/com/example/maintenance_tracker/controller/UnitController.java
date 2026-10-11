package com.example.maintenance_tracker.controller;

import com.example.maintenance_tracker.dto.CreateUnitRequest;
import com.example.maintenance_tracker.entity.Unit;
import com.example.maintenance_tracker.repository.UnitRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units")
@CrossOrigin(origins = "*")
public class UnitController {

    private final UnitRepository unitRepository;

    public UnitController(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    // GET all units
    @GetMapping
    public ResponseEntity<List<Unit>> getAllUnits() {
        return ResponseEntity.ok(unitRepository.findAll());
    }

    // POST create a unit
    @PostMapping
    public ResponseEntity<Unit> createUnit(@RequestBody CreateUnitRequest request) {
        Unit unit = new Unit();
        unit.setUnitNumber(request.getUnitNumber());
        unit.setResidentName(request.getResidentName());
        unit.setPhoneNumber(request.getPhoneNumber());

        Unit savedUnit = unitRepository.save(unit);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUnit);
    }
}