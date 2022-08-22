package com.example.appointments.ui.home.appointments

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appointments.R
import com.example.appointments.models.appointments.Appointment
import com.google.android.gms.common.util.CollectionUtils.listOf
import kotlinx.android.synthetic.main.item_appointment.view.*
import timber.log.Timber
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AppointmentsListAdapter(val appointmentsListCallbacks: AppointmentsAdapterCallbacks) :
    RecyclerView.Adapter<AppointmentsListAdapter.AppointmentListViewHolder>() {

    var appointmentData: List<Appointment> = listOf()
        set(value) {
            Timber.d("updating the appointment data")
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentListViewHolder {
        return AppointmentListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_appointment,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = appointmentData.size

    override fun onBindViewHolder(holder: AppointmentListViewHolder, position: Int) {
        holder.bindLibraryToView(appointmentData[position])
    }

    fun changeAppointmentList(businessList: List<Appointment>) {
        Timber.d("changeBusinessList : list changed. $businessList")
        appointmentData = businessList
        if (appointmentData.isEmpty()){
            appointmentsListCallbacks.onEmptyData()
        }
        else {
            appointmentsListCallbacks.onNonEmptyData()
            notifyDataSetChanged()
        }
    }
    fun addAppointmentList(businessList: List<Appointment>) {
        Timber.d("changeBusinessList : list added. $businessList")
        appointmentData = appointmentData.plus(businessList)
        if (appointmentData.isEmpty()){
            appointmentsListCallbacks.onEmptyData()
        }
        else {
            appointmentsListCallbacks.onNonEmptyData()
            notifyDataSetChanged()
        }
    }

    inner class AppointmentListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindLibraryToView(appointment: Appointment) {
            itemView.location_label.text = appointment.city + ", " + appointment.state
            itemView.name_label.text = appointment.client_name
            itemView.email_label.text = appointment.email
            itemView.phone_label.text = appointment.phone_number
            itemView.time_label.text = getTime(appointment.time)
            itemView.date_label.text = getDate(appointment.time)

            itemView.setOnClickListener {
                appointmentsListCallbacks.onAppointmentSelected(appointment)
            }
        }
    }

    @SuppressLint("NewApi")
    private fun getDate(timeString: String): String {
        val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date = LocalDate.parse(timeString , firstApiFormat)
        val substring = date.month.toString().subSequence(0,1)
        val end = date.month.toString().substring(1).lowercase()
        return "${substring}${end} ${date.dayOfMonth}, ${date.year}"
    }

    @SuppressLint("NewApi")
    private fun getTime(timeString: String): String {
        val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val time = LocalTime.parse(timeString, firstApiFormat)
        val tme = Time(time.hour, time.minute, 0)
        val formatter = SimpleDateFormat("h:mm a")
        return formatter.format(tme)
    }
}