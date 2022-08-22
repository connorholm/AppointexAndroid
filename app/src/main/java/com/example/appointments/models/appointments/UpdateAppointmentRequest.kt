package com.example.appointments.models.appointments


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateAppointmentRequest(
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
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
): Serializable