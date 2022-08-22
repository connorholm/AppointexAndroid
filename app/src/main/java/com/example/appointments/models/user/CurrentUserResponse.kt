package com.example.appointments.models.user


import com.google.gson.annotations.SerializedName

data class CurrentUserResponse(
    @SerializedName("user")
    val user: User
)

data class User(
    @SerializedName("email")
    val email: String,
    @SerializedName("is_admin")
    val is_admin: Boolean,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("company_id")
    val company_id: Int
)