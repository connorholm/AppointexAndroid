package com.example.appointments.models.auth

import com.google.gson.annotations.SerializedName


/*
{
    "grant_type":"password",
    "email":"connorjholm@gmail.com",
    "password":"test",
    "client_secret": "tPyp7o2G7lNGJUdb2cBA0cwc4ifdnbII1VP8vvS9Kd4",
    "client_id": "B-YGKEVmcf5QThg5v37KhBdW-dO_K8e9E_Ab5isp8DY"
}
 */

data class SignInRequest(
    @SerializedName("grant_type")
    val grant_type: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("client_secret")
    val client_secret: String,
    @SerializedName("client_id")
    val client_id: String,
)