package com.example.maintenance_tracker.dto;

public class CreateUnitRequest {
    private String unitNumber;
    private String residentName;
    private String phoneNumber;

    public CreateUnitRequest() {}

    public String getUnitNumber() { return unitNumber; }
    public void setUnitNumber(String unitNumber) { this.unitNumber = unitNumber; }

    public String getResidentName() { return residentName; }
    public void setResidentName(String residentName) { this.residentName = residentName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}