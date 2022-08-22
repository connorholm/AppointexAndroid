package com.example.appointments.ui.home.appointments

import com.example.appointments.models.appointments.Appointment

interface AppointmentsAdapterCallbacks {
    fun onAppointmentSelected(appointment: Appointment)
    fun onEmptyData()
    fun onNonEmptyData()
}