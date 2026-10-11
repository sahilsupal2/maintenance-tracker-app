package com.example.maintenancetrackerapp

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.maintenancetrackerapp.model.CreateUnitRequest
import com.example.maintenancetrackerapp.network.ApiClient
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class RegisterUnitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_unit)

        val etUnitNumber = findViewById<TextInputEditText>(R.id.etUnitNumber)
        val etResidentName = findViewById<TextInputEditText>(R.id.etResidentName)
        val etPhoneNumber = findViewById<TextInputEditText>(R.id.etPhoneNumber)
        val btnRegister = findViewById<Button>(R.id.btnRegisterUnit)

        btnRegister.setOnClickListener {
            val unitNum = etUnitNumber.text?.toString()?.trim().orEmpty()
            val name = etResidentName.text?.toString()?.trim().orEmpty()
            val phone = etPhoneNumber.text?.toString()?.trim().orEmpty()

            if (unitNum.isEmpty() || name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUnit(CreateUnitRequest(unitNum, name, phone))
        }
    }

    private fun registerUnit(request: CreateUnitRequest) {
        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.createUnit(request)
                if (response.isSuccessful) {
                    val createdUnit = response.body()
                    val unitId = createdUnit?.id ?: 0L

                    val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    prefs.edit().putLong("saved_unit_id", unitId).apply()

                    Toast.makeText(
                        this@RegisterUnitActivity,
                        "Registered! Your Unit ID is: $unitId",
                        Toast.LENGTH_LONG
                    ).show()

                    finish()
                } else {
                    Toast.makeText(this@RegisterUnitActivity, "Failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@RegisterUnitActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}