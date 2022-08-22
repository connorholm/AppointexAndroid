package com.example.appointments.models.auth

import com.google.gson.annotations.SerializedName

data class ForgotPassRequest(
    @SerializedName("user")
    val user: ForgotUserRequestEmail,
)

data class ForgotUserRequestEmail(
    @SerializedName("email")
    val email: String
)
