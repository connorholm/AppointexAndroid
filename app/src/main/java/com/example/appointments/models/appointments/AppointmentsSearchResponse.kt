package com.example.appointments.models.appointments

import com.google.gson.annotations.SerializedName

data class AppointmentsSearchResponse(
    @SerializedName("appointments")
    val appointments: List<Appointment>,
    @SerializedName("completed_appointments")
    val completed_appointments: List<Appointment>,
    @SerializedName("total_count")
    val total_count: Int,
    @SerializedName("unfinished_count")
    val unfinished_count: Int,
    @SerializedName("completed_count")
    val completed_count: Int,
)
