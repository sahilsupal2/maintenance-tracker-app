package com.example.maintenancetrackerapp.model

data class CreateTicketRequest(
    val unitId: Long,
    val category: String,
    val description: String
)

