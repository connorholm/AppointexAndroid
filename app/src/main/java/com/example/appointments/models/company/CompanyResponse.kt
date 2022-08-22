package com.example.appointments.models.company

import com.google.gson.annotations.SerializedName

data class CompanyResponse(
    @SerializedName("company")
    val company: Company
)
