package com.example.maintenancetrackerapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.maintenancetrackerapp.model.CreateTicketRequest
import com.example.maintenancetrackerapp.network.ApiClient
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.io.IOException

class CreateTicketActivity : AppCompatActivity() {

    private var registeredUnitId: Long = -1L
    private lateinit var btnSubmit: Button
    private lateinit var etCategory: TextInputEditText
    private lateinit var etDescription: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_ticket)

        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        registeredUnitId = prefs.getLong("saved_unit_id", -1L)

        if (registeredUnitId == -1L) {
            Toast.makeText(this, "Please register your unit first", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, RegisterUnitActivity::class.java))
            finish()
            return
        }

        findViewById<TextView>(R.id.tvRegisteredUnitInfo).text = "Filing ticket for Unit #$registeredUnitId"

        etCategory = findViewById(R.id.etCategory)
        etDescription = findViewById(R.id.etDescription)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val category = etCategory.text?.toString()?.trim().orEmpty()
            val description = etDescription.text?.toString()?.trim().orEmpty()

            if (category.isEmpty()) {
                etCategory.error = "Please enter a category"
                return@setOnClickListener
            }

            if (description.isEmpty()) {
                etDescription.error = "Please enter a description"
                return@setOnClickListener
            }

            val request = CreateTicketRequest(
                unitId = registeredUnitId,
                category = category,
                description = description
            )

            submitTicket(request)
        }
    }

    private fun submitTicket(request: CreateTicketRequest) {
        btnSubmit.isEnabled = false
        btnSubmit.text = "Submitting..."

        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.createTicket(request)

                if (response.isSuccessful) {
                    Toast.makeText(this@CreateTicketActivity, "Ticket Created Successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    btnSubmit.isEnabled = true
                    btnSubmit.text = "Submit Ticket"
                    Toast.makeText(this@CreateTicketActivity, "Server Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }

            } catch (e: IOException) {
                btnSubmit.isEnabled = true
                btnSubmit.text = "Submit Ticket"
                Toast.makeText(
                    this@CreateTicketActivity,
                    "Offline: Cannot reach server. Please check your connection.",
                    Toast.LENGTH_LONG
                ).show()

            } catch (e: Exception) {
                btnSubmit.isEnabled = true
                btnSubmit.text = "Submit Ticket"
                Toast.makeText(this@CreateTicketActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}