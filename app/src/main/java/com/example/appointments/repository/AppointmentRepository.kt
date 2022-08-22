package com.example.appointments.repository

import com.example.appointments.api.AppointmentsAPI
import com.example.appointments.models.appointments.*
import com.example.appointments.models.company.Company
import com.example.appointments.models.company.CompanyResponse
import com.example.appointments.models.notifications.SendMessageRequest
import com.example.appointments.models.notifications.SendMessageResponse
import com.example.appointments.models.user.CurrentUserResponse
import com.example.appointments.utils.Constants
import com.example.insuppli.repository.local.DataManager
import io.reactivex.Observable

import javax.inject.Inject

class AppointmentRepository @Inject constructor(private val appointmentAPI: AppointmentsAPI) {

    companion object {
        const val SEARCH_NAME_PATH = "/api/v1/appointments/name/"
        const val SEARCH_TIME_PATH = "/api/v1/appointments/time/"
    }

    fun searchByName(searQuery: String): Observable<AppointmentsSearchResponse> {
        val accessToken = "Bearer " + DataManager.getInstance().getAccessToken().toString()
        return appointmentAPI.searchAppointmentsByName(SEARCH_NAME_PATH + searQuery, Constants.Remote.CONTENT_TYPE_HEADER_VALUE, accessToken)
    }

    fun searchByTime(timeQuery: String): Observable<AppointmentsSearchResponse> {
        val accessToken = "Bearer " + DataManager.getInstance().getAccessToken().toString()
        return appointmentAPI.searchAppointmentsByTime(SEARCH_TIME_PATH + timeQuery, Constants.Remote.CONTENT_TYPE_HEADER_VALUE, accessToken)
    }

    fun getCompany(company_id: Int): Observable<CompanyResponse> {
        val accessToken = "Bearer " + DataManager.getInstance().getAccessToken().toString()
        return appointmentAPI.getCompany(Constants.Remote.CONTENT_TYPE_HEADER_VALUE, accessToken, company_id)
    }

    fun getAppointment(appointment_id: Int): Observable<AppointmentResponse> {
        val accessToken = "Bearer " + DataManager.getInstance().getAccessToken().toString()
        return appointmentAPI.getAppointment(Constants.Remote.CONTENT_TYPE_HEADER_VALUE, accessToken, appointment_id)
    }

    fun completeAppointment(appointment_id: Int, body: CompleteAppointmentRequest): Observable<AppointmentResponse> {
        val accessToken = "Bearer " + DataManager.getInstance().getAccessToken().toString()
        return appointmentAPI.completeAppointment(Constants.Remote.CONTENT_TYPE_HEADER_VALUE, accessToken, appointment_id, body)
    }

    fun updateAppointment(appointment_id: Int, body: UpdateAppointmentRequest): Observable<AppointmentResponse> {
        val accessToken = "Bearer " + DataManager.getInstance().getAccessToken().toString()
        return appointmentAPI.updateAppointment(Constants.Remote.CONTENT_TYPE_HEADER_VALUE, accessToken, appointment_id, body)
    }

    fun createAppointment(body: CreateAppointmentRequest): Observable<AppointmentResponse> {
        val accessToken = "Bearer " + DataManager.getInstance().getAccessToken().toString()
        return appointmentAPI.createAppointment(Constants.Remote.CONTENT_TYPE_HEADER_VALUE, accessToken, body)
    }

    fun deleteAppointment(appointment_id: Int): Observable<DeleteAppointmentsResponse> {
        val accessToken = "Bearer " + DataManager.getInstance().getAccessToken().toString()
        return appointmentAPI.deleteAppointment(Constants.Remote.CONTENT_TYPE_HEADER_VALUE, accessToken, appointment_id)
    }

    fun sendMessage(body: SendMessageRequest): Observable<SendMessageResponse> {
        val accessToken = "Bearer " + DataManager.getInstance().getAccessToken().toString()
        return appointmentAPI.sendMessage(Constants.Remote.CONTENT_TYPE_HEADER_VALUE, accessToken, body)
    }
}