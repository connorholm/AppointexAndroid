package com.example.appointments.models.appointments


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CompleteAppointmentRequest(
    @SerializedName("isComplete")
    val isComplete: Boolean,
): Serializable