package com.example.appointments.models.user


import com.google.gson.annotations.SerializedName

data class UpdateUserRequest(
    @SerializedName("user")
    val user: UpdateUserRequestBody
)

data class UpdateUserRequestBody(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("new_password")
    val new_password: String,
    @SerializedName("confirmation_password")
    val confirmation_password: String,
    @SerializedName("current_password")
    val current_password: String,
)