package com.example.appointments.models.notifications

import com.google.gson.annotations.SerializedName

data class SendMessageRequest(
    @SerializedName("message")
    val message: String,
    @SerializedName("phone_number")
    val phone_number: String,
)
