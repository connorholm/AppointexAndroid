package com.example.appointments.models.appointments


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Appointment(
    @SerializedName("id")
    val id: Int,
    @SerializedName("time")
    val time: String,
    @SerializedName("client_name")
    val client_name: String,
    @SerializedName("street")
    val street: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone_number")
    val phone_number: String,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("updated_at")
    val updated_at: String,
    @SerializedName("company_id")
    val company_id: Int,
    @SerializedName("isComplete")
    val isComplete: Boolean,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
): Serializable