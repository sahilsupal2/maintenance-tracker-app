package com.example.maintenancetrackerapp.network

import com.example.maintenancetrackerapp.model.CreateTicketRequest
import com.example.maintenancetrackerapp.model.CreateUnitRequest
import com.example.maintenancetrackerapp.model.MaintenanceTicket
import com.example.maintenancetrackerapp.model.UnitResponse
import com.example.maintenancetrackerapp.model.UpdateStatusRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("api/tickets")
    suspend fun getTickets(): Response<List<MaintenanceTicket>>


    @PATCH("api/tickets/{id}/status")
    suspend fun resolveTicket(@Path("id") id: Long): Response<MaintenanceTicket>


    @POST("api/tickets")
    suspend fun createTicket(
        @Body request: CreateTicketRequest
    ): Response<MaintenanceTicket>

    @PATCH("api/tickets/{id}/status")
    suspend fun updateTicketStatus(
        @Path("id") id: Long,
        @Body request: UpdateStatusRequest
    ): Response<MaintenanceTicket>





    @POST("api/units")
    suspend fun createUnit(
        @Body request: CreateUnitRequest
    ): Response<UnitResponse>
}