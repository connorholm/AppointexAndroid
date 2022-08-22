package com.example.appointments.models.auth

import com.google.gson.annotations.SerializedName



data class TokenLogoutRequest(
    @SerializedName("token")
    val token: String,
    @SerializedName("client_secret")
    val client_secret: String,
    @SerializedName("client_id")
    val client_id: String,
)