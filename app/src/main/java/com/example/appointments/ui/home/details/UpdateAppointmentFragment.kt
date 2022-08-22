package com.example.appointments.ui.home.details

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.appointments.R
import com.example.appointments.models.appointments.Appointment
import com.example.appointments.models.appointments.UpdateAppointmentRequest
import com.example.appointments.ui.home.appointments.AppointmentsViewModel
import com.google.type.DateTime
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Time
import java.text.Format
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import androidx.lifecycle.Observer
import java.util.logging.SimpleFormatter


@AndroidEntryPoint
class UpdateAppointmentFragment : Fragment() {

    private lateinit var backBtn: ImageView
    private lateinit var deleteBtn: ImageView
    private lateinit var nameEt: EditText
    private lateinit var emailEt: EditText
    private lateinit var phoneEt: EditText
    private lateinit var streetEt: EditText
    private lateinit var cityEt: EditText
    private lateinit var dateTv: TextView
    private lateinit var timeTv: TextView
    private lateinit var updateBtn: TextView
    private lateinit var dateBtn: Button
    private lateinit var timeBtn: Button
    private lateinit var loading: ProgressBar
    private lateinit var spinner: Spinner
    private lateinit var appointment: Appointment
    private lateinit var state: String
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0
    private lateinit var timeString: String
    private val viewModel: AppointmentsViewModel by viewModels()
    private val args by navArgs<UpdateAppointmentFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_update_appointment, container, false)
        appointment = args.appointment
        timeString = appointment.time

        val c: Calendar = Calendar.getInstance();
        val date: Date = getDateFromISO8601()
        c.time = date
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH) + 1
        mDay = c.get(Calendar.DAY_OF_MONTH)
        mHour = c.get(Calendar.HOUR_OF_DAY)
        mMinute = c.get(Calendar.MINUTE)

        setUI(view)
        Log.e("Testing", "${args.appointment}")
        return view
    }

    private fun setUI(view: View) {
        backBtn = view.findViewById(R.id.back_icon)
        deleteBtn = view.findViewById(R.id.delete_icon)
        nameEt = view.findViewById(R.id.client_name_et)
        emailEt = view.findViewById(R.id.email_et)
        phoneEt = view.findViewById(R.id.phone_et)
        streetEt = view.findViewById(R.id.address_et)
        cityEt = view.findViewById(R.id.city_et)
        dateTv = view.findViewById(R.id.date_tv)
        timeTv = view.findViewById(R.id.time_tv)
        dateBtn = view.findViewById(R.id.edit_date_btn)
        timeBtn = view.findViewById(R.id.edit_time_btn)
        updateBtn = view.findViewById(R.id.update_btn)
        loading = view.findViewById(R.id.loading)
        spinner = view.findViewById(R.id.spinner)

        loading.visibility = View.GONE

        nameEt.setText(appointment.client_name)
        emailEt.setText(appointment.email)
        phoneEt.setText(appointment.phone_number)
        streetEt.setText(appointment.street)
        cityEt.setText(appointment.city)

        val states = resources.getStringArray(R.array.states)

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item, states
        )

        var index = 0
        for (state in states) {
            if (state == appointment.state) {
                break
            }
            index += 1
        }
        spinner.adapter = adapter
        spinner.setSelection(index)
        spinner.setPopupBackgroundResource(R.color.darkGrey)
        state = states[index]


        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                state = states[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        dateTv.text = "${mDay}-${mMonth}-${mYear}"
        timeTv.text = getTime(mHour, mMinute)

        dateBtn.setOnClickListener {

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    dateTv.text = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                    updateISO8601Time(year, monthOfYear + 1, dayOfMonth, mHour, mMinute)
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        timeBtn.setOnClickListener {

            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                OnTimeSetListener { view, hourOfDay, minute ->
                    timeTv.text = getTime(hourOfDay, minute)
                    updateISO8601Time(mYear, mMonth, mDay, hourOfDay, minute)
                }, mHour, mMinute, false
            )
            timePickerDialog.show();
        }

        backBtn.setOnClickListener {
            findNavController().navigate(
                UpdateAppointmentFragmentDirections.actionUpdateAppointmentFragmentToDetailFragment(
                    args.appointmentId
                )
            )
        }

        updateBtn.setOnClickListener {
            val updateBody = UpdateAppointmentRequest(
                timeString,
                nameEt.text.toString(),
                streetEt.text.toString(),
                cityEt.text.toString(),
                state, "United States",
                emailEt.text.toString(),
                phoneEt.text.toString(),
                appointment.latitude,
                appointment.longitude
            )
            viewModel.updateAppointment(args.appointmentId, updateBody)
            loading.visibility = View.VISIBLE
            updateResult()
        }

        deleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete Appointment")
            builder.setMessage("Are you sure?")
            builder.setPositiveButton("Delete") { dialog, which ->
                viewModel.deleteAppointment(args.appointmentId)
                loading.visibility = View.VISIBLE
                updateResult()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
            }
            builder.show()
        }
    }

    private fun getTime(hr: Int, min: Int): String? {
        val tme = Time(hr, min, 0) //seconds by default set to zero
        val formatter: Format
        formatter = SimpleDateFormat("h:mm a")
        return formatter.format(tme)
    }

    @SuppressLint("NewApi")
    private fun updateISO8601Time(year: Int, month: Int, day: Int, hour: Int, minute: Int) {
        val localDate = LocalDateTime.of(year, month, day, hour, minute)
        val offsetDate = OffsetDateTime.of(localDate, ZoneOffset.UTC)
        timeString = offsetDate.format(DateTimeFormatter.ISO_DATE_TIME)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateFromISO8601(): Date {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return format.parse(timeString)!!
    }

    private fun updateResult() {
        viewModel.successUpdateAppointmentMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            Toast.makeText(requireContext(), "Update Successful", Toast.LENGTH_LONG).show()
            findNavController().navigate(UpdateAppointmentFragmentDirections.actionUpdateAppointmentFragmentToDetailFragment(args.appointmentId))
        })

        viewModel.successDeleteAppointmentMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            Toast.makeText(requireContext(), "Deleted Appointment", Toast.LENGTH_LONG).show()
            findNavController().navigate(UpdateAppointmentFragmentDirections.actionUpdateAppointmentFragmentToAppointmentsFragment())
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            generalAlert(requireContext(), "Error", it, null, "Ok")
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

}