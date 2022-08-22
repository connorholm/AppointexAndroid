package com.example.appointments.models.appointments

import java.io.Serializable

data class AppointmentObj(
    val id: Int,
    val time: String,
    val client_name: String,
    val street: String,
    val city: String,
    val state: String,
    val country: String,
    val email: String,
    val phone_number: String,
    val created_at: String,
    val updated_at: String,
    val company_id: Int,
    val isComplete: Boolean,
    val latitude: Double,
    val longitude: Double,
): Serializable