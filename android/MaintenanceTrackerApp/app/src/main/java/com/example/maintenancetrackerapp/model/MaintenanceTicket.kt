package com.example.maintenancetrackerapp.model

data class MaintenanceTicket(
    val id: Long,
    val category: String,
    var status: String,
    val description: String? = null
)