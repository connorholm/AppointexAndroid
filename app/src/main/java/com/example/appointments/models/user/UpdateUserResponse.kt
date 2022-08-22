package com.example.appointments.models.user


import com.google.gson.annotations.SerializedName

data class UpdateUserResponse(
    @SerializedName("user")
    val user: User,
    @SerializedName("message")
    val message: String
)

data class UpdateUserResponseBody(
    @SerializedName("email")
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("last_name")
    val lastName: String,
)