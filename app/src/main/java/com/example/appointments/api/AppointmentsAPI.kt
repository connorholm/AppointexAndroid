package com.example.appointments.api

import com.example.appointments.models.appointments.*
import com.example.appointments.models.company.Company
import com.example.appointments.models.company.CompanyResponse
import com.example.appointments.models.notifications.SendMessageRequest
import com.example.appointments.models.notifications.SendMessageResponse
import com.example.appointments.models.user.UpdateUserRequest
import com.example.appointments.utils.Constants
import io.reactivex.Observable
import retrofit2.http.*

interface AppointmentsAPI {

    @GET
    fun searchAppointmentsByName(
        @Url url: String,
        @Header(Constants.Remote.CONTENT_TYPE_HEADER_KEY) contentType: String,
        @Header("Authorization") bearerToken: String,
    ): Observable<AppointmentsSearchResponse>

    @GET
    fun searchAppointmentsByTime(
        @Url url: String,
        @Header(Constants.Remote.CONTENT_TYPE_HEADER_KEY) contentType: String,
        @Header("Authorization") bearerToken: String,
    ): Observable<AppointmentsSearchResponse>

    @GET("/api/v1/companys/{company_id}")
    fun getCompany(
        @Header(Constants.Remote.CONTENT_TYPE_HEADER_KEY) contentType: String,
        @Header("Authorization") bearerToken: String,
        @Path("company_id") company_id: Int
    ): Observable<CompanyResponse>

    @GET("/api/v1/appointments/{appointment_id}")
    fun getAppointment(
        @Header(Constants.Remote.CONTENT_TYPE_HEADER_KEY) contentType: String,
        @Header("Authorization") bearerToken: String,
        @Path("appointment_id") appointment_id: Int
    ): Observable<AppointmentResponse>

    @PATCH("/api/v1/appointments/{appointment_id}/complete")
    fun completeAppointment(
        @Header(Constants.Remote.CONTENT_TYPE_HEADER_KEY) contentType: String,
        @Header("Authorization") bearerToken: String,
        @Path("appointment_id") appointment_id: Int,
        @Body bodyRequest: CompleteAppointmentRequest
    ): Observable<AppointmentResponse>

    @PATCH("/api/v1/appointments/{appointment_id}")
    fun updateAppointment(
        @Header(Constants.Remote.CONTENT_TYPE_HEADER_KEY) contentType: String,
        @Header("Authorization") bearerToken: String,
        @Path("appointment_id") appointment_id: Int,
        @Body bodyRequest: UpdateAppointmentRequest
    ): Observable<AppointmentResponse>

    @POST("/api/v1/appointments/")
    fun createAppointment(
        @Header(Constants.Remote.CONTENT_TYPE_HEADER_KEY) contentType: String,
        @Header("Authorization") bearerToken: String,
        @Body bodyRequest: CreateAppointmentRequest
    ): Observable<AppointmentResponse>

    @DELETE("/api/v1/appointments/{appointment_id}")
    fun deleteAppointment(
        @Header(Constants.Remote.CONTENT_TYPE_HEADER_KEY) contentType: String,
        @Header("Authorization") bearerToken: String,
        @Path("appointment_id") appointment_id: Int,
    ): Observable<DeleteAppointmentsResponse>

    @POST("/api/v1/notifications/send")
    fun sendMessage(
        @Header(Constants.Remote.CONTENT_TYPE_HEADER_KEY) contentType: String,
        @Header("Authorization") bearerToken: String,
        @Body bodyRequest: SendMessageRequest
    ): Observable<SendMessageResponse>
}