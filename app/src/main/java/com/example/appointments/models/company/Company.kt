package com.example.appointments.models.company


import com.google.gson.annotations.SerializedName

data class Company(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("updated_at")
    val updated_at: String,
    @SerializedName("user_ids")
    val user_ids: List<Int>,
    @SerializedName("appointment_ids")
    val appointment_ids: List<Int>,
)