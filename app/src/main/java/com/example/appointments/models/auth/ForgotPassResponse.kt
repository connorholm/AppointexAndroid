package com.example.appointments.models.auth

import com.google.gson.annotations.SerializedName

data class ForgotPassResponse(
    @SerializedName("Success")
    val Success: String,
)
