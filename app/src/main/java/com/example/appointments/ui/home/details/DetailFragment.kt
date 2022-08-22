package com.example.appointments.ui.home.details

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.appointments.R
import com.example.appointments.models.appointments.Appointment
import com.example.appointments.models.appointments.CompleteAppointmentRequest
import com.example.appointments.ui.home.appointments.AppointmentsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class DetailFragment : Fragment(), OnMapReadyCallback {

    private lateinit var backBtn: ImageView
    private lateinit var settingBtn: ImageView
    private lateinit var directionsBtn: TextView
    private lateinit var timelineBtn: TextView
    private lateinit var completeBtn: TextView
    private lateinit var nameTV: TextView
    private lateinit var emailTV: TextView
    private lateinit var phoneTV: TextView
    private lateinit var timeTV: TextView
    private lateinit var dateTV: TextView
    private lateinit var loading: ProgressBar
    private lateinit var appointment: Appointment
    private lateinit var mMap: GoogleMap
    private var DEFAULT_ZOOM = 11.0f
    private val viewModel: AppointmentsViewModel by viewModels()
    private val args by navArgs<DetailFragmentArgs>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =  inflater.inflate(R.layout.fragment_detail, container, false)
        setUI(view)
        viewModel.getAppointment(args.appointmentId)
        loading.visibility = View.VISIBLE
        updateResult()
        return view
    }

    private fun setUI(view: View) {
        backBtn = view.findViewById(R.id.back_icon)
        settingBtn = view.findViewById(R.id.settings_icon)
        nameTV = view.findViewById(R.id.name_tv)
        emailTV = view.findViewById(R.id.email_tv)
        phoneTV = view.findViewById(R.id.phone_tv)
        timeTV = view.findViewById(R.id.time_tv)
        dateTV = view.findViewById(R.id.date_tv)
        loading = view.findViewById(R.id.loading)
        directionsBtn = view.findViewById(R.id.directions_btn)
        timelineBtn = view.findViewById(R.id.timeline_btn)
        completeBtn = view.findViewById(R.id.complete_btn)
        loading.visibility = View.GONE

        phoneTV.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:" + appointment.phone_number)
            startActivity(dialIntent)
        }

        directionsBtn.setOnClickListener {
            val gmmIntentUri =
                Uri.parse("google.navigation:q=${appointment.latitude},${appointment.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        timelineBtn.setOnClickListener {
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToTimelineFragment(args.appointmentId, appointment.phone_number))
        }

        completeBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Complete Appointment")
            builder.setMessage("Send a feedback text or just complete appointment.")
            builder.setNegativeButton("Feedback Message") { dialog, which ->
                findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToFeedbackFragment(args.appointmentId, appointment.phone_number))
            }
            builder.setNeutralButton("Cancel") { dialog, which ->
            }
            builder.setPositiveButton("Complete") { dialog, which ->
                viewModel.completeAppointment(args.appointmentId, CompleteAppointmentRequest(true))
                loading.visibility = View.VISIBLE
                updateResult()
            }
            builder.show()
        }

        backBtn.setOnClickListener {
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToAppointmentsFragment())
        }

        settingBtn.setOnClickListener {
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToUpdateAppointmentFragment(args.appointmentId, appointment))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshToken()
        loading.visibility = View.VISIBLE
        updateResult()
    }

    private fun updateResult(){

        viewModel.successAppointmentMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            nameTV.text = it.appointment.client_name
            emailTV.text = it.appointment.email
            phoneTV.text = it.appointment.phone_number
            timeTV.text = getTime(it.appointment.time)
            dateTV.text = getDate(it.appointment.time)
            appointment = it.appointment

            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        })

        viewModel.successRefreshMessage.observe(viewLifecycleOwner, Observer {
            viewModel.getAppointment(args.appointmentId)
            updateResult()
        })

        viewModel.successAppointmentCompleteMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            Toast.makeText(requireContext(), "Appointment Completed", Toast.LENGTH_LONG).show()
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToAppointmentsFragment())
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            generalAlert(requireContext(),"Error",it,null,"Ok")
        })
    }

    private fun generalAlert(context: Context, title: String, message: String, positiveButton: String?, negativeButton: String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        if (positiveButton != null){
            builder.setPositiveButton(positiveButton) { dialog, which ->
            }
        }
        builder.setNegativeButton(negativeButton) { dialog, which ->
        }
        builder.show()
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val appointmentLocation = LatLng(appointment.latitude, appointment.longitude)
        mMap.addMarker(
            MarkerOptions()
            .position(appointmentLocation)
            .title(appointment.client_name))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(appointmentLocation, DEFAULT_ZOOM))
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