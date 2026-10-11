package com.example.maintenancetrackerapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maintenancetrackerapp.model.MaintenanceTicket
import com.example.maintenancetrackerapp.model.UpdateStatusRequest
import com.example.maintenancetrackerapp.network.ApiClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var ticketAdapter: TicketAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutError: View
    private lateinit var tvErrorMessage: TextView
    private lateinit var btnRetry: Button
    private lateinit var tvEmptyState: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val savedUnitId = prefs.getLong("saved_unit_id", -1L)
        if (savedUnitId == -1L) {
            startActivity(Intent(this, RegisterUnitActivity::class.java))
        }

        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewTickets)
        progressBar = findViewById(R.id.progressBar)
        layoutError = findViewById(R.id.layoutError)
        tvErrorMessage = findViewById(R.id.tvErrorMessage)
        btnRetry = findViewById(R.id.btnRetry)
        tvEmptyState = findViewById(R.id.tvEmptyState)

        setupRecyclerView()

        btnRetry.setOnClickListener {
            fetchTickets()
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startActivity(Intent(this, CreateTicketActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        fetchTickets()
    }

    private fun setupRecyclerView() {
        ticketAdapter = TicketAdapter(emptyList()) { ticket ->
            resolveTicket(ticket)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ticketAdapter
    }

    private fun fetchTickets() {
        showLoadingState()

        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.getTickets()
                if (response.isSuccessful) {
                    val tickets = response.body().orEmpty()
                    if (tickets.isEmpty()) {
                        showEmptyState()
                    } else {
                        showDataState(tickets)
                    }
                } else {
                    showErrorState("Server returned error ${response.code()}")
                }
            } catch (e: IOException) {
                showErrorState("Network offline or server unreachable.\nPlease check your connection.")
            } catch (e: Exception) {
                showErrorState("Unexpected error: ${e.localizedMessage}")
            }
        }
    }

    private fun showLoadingState() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        layoutError.visibility = View.GONE
        tvEmptyState.visibility = View.GONE
    }

    private fun showDataState(tickets: List<MaintenanceTicket>) {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        layoutError.visibility = View.GONE
        tvEmptyState.visibility = View.GONE
        ticketAdapter.updateTickets(tickets)
    }

    private fun showEmptyState() {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.GONE
        layoutError.visibility = View.GONE
        tvEmptyState.visibility = View.VISIBLE
    }

    private fun showErrorState(message: String) {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.GONE
        tvEmptyState.visibility = View.GONE
        layoutError.visibility = View.VISIBLE
        tvErrorMessage.text = message
    }

    private fun resolveTicket(ticket: MaintenanceTicket) {
        lifecycleScope.launch {
            try {
                val request = UpdateStatusRequest(status = "RESOLVED")
                val response = ApiClient.apiService.updateTicketStatus(ticket.id, request)

                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Ticket #${ticket.id} marked as RESOLVED", Toast.LENGTH_SHORT).show()
                    fetchTickets()
                } else {
                    Toast.makeText(this@MainActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Cannot connect to server", Toast.LENGTH_SHORT).show()
            }
        }
    }
}