package com.example.appointments.models.appointments

import com.google.gson.annotations.SerializedName

data class DeleteAppointmentsResponse(
    @SerializedName("message")
    val message: String,

)
