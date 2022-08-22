package com.example.appointments.models.auth

import com.google.gson.annotations.SerializedName

data class ContactEmailRequest(
    @SerializedName("email")
    val email: String,
)

data class ContactEmailResponse(
    @SerializedName("message")
    val message: String,
)
