package com.example.maintenance_tracker.dto;

public class CreateTicketRequest {
    private Long unitId;
    private String category;
    private String description;

    public CreateTicketRequest() {}

    public Long getUnitId() { return unitId; }
    public void setUnitId(Long unitId) { this.unitId = unitId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}