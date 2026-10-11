package com.example.maintenancetrackerapp.model

data class CreateUnitRequest(
    val unitNumber: String,
    val residentName: String,
    val phoneNumber: String
)