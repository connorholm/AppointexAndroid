package com.example.appointments.models.auth

import com.google.gson.annotations.SerializedName

/*
{
    "access_token": "u0f-Cb2jJgK9rk8FDh-ON9FXrqHc5-2k_RE7o_BwnA",
    "token_type": "Bearer",
    "expires_in": 7200,
    "refresh_token": "NsQSGzMnT-gL7kpWh3lFxPZrFzpxcEjouQujjKbj1Q",
    "created_at": 1659921036
}
 */
data class SignInResponse(
    @SerializedName("access_token")
    val access_token: String,
    @SerializedName("token_type")
    val token_type: String,
    @SerializedName("expires_in")
    val expires_in: Int,
    @SerializedName("refresh_token")
    val refresh_token: String,
    @SerializedName("created_at")
    val created_at: Int,
)