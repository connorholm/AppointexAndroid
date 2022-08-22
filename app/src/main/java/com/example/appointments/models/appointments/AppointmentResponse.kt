package com.example.appointments.models.appointments


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AppointmentResponse(
    @SerializedName("appointment")
    val appointment: Appointment,
): Serializable