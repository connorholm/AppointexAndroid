package com.example.appointments.models.notifications

import com.google.gson.annotations.SerializedName

data class SendMessageResponse(
    @SerializedName("message")
    val message: String,
)
