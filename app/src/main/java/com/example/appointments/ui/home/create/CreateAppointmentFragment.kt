package com.example.appointments.ui.home.create

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.appointments.R
import com.example.appointments.models.appointments.CreateAppointmentRequest
import com.example.appointments.ui.home.appointments.AppointmentsViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.sql.Time
import java.text.Format
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
class CreateAppointmentFragment : Fragment() {

    private lateinit var backBtn: ImageView
    private lateinit var nameEt: EditText
    private lateinit var emailEt: EditText
    private lateinit var phoneEt: EditText
    private lateinit var streetEt: EditText
    private lateinit var cityEt: EditText
    private lateinit var dateTv: TextView
    private lateinit var timeTv: TextView
    private lateinit var createBtn: TextView
    private lateinit var dateBtn: Button
    private lateinit var timeBtn: Button
    private lateinit var loading: ProgressBar
    private lateinit var spinner: Spinner
    private lateinit var state: String
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0
    private lateinit var timeString: String
    private val viewModel: AppointmentsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_create_appointment, container, false)
        val c: Calendar = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)
        mHour = c.get(Calendar.HOUR_OF_DAY)
        mMinute = c.get(Calendar.MINUTE)
        setUI(view)
        return view
    }

    private fun setUI(view: View) {
        backBtn = view.findViewById(R.id.back_icon)
        nameEt = view.findViewById(R.id.client_name_et)
        emailEt = view.findViewById(R.id.email_et)
        phoneEt = view.findViewById(R.id.phone_et)
        streetEt = view.findViewById(R.id.address_et)
        cityEt = view.findViewById(R.id.city_et)
        dateTv = view.findViewById(R.id.date_tv)
        timeTv = view.findViewById(R.id.time_tv)
        dateBtn = view.findViewById(R.id.edit_date_btn)
        timeBtn = view.findViewById(R.id.edit_time_btn)
        createBtn = view.findViewById(R.id.create_btn)
        loading = view.findViewById(R.id.loading)
        spinner = view.findViewById(R.id.spinner)

        loading.visibility = View.GONE

        val states = resources.getStringArray(R.array.states)

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item, states
        )

        spinner.adapter = adapter
        spinner.setPopupBackgroundResource(R.color.darkGrey)


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

        timeString = ""
        dateTv.text = "${mDay}-${mMonth + 1}-${mYear}"
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
                    updateISO8601Time(mYear, mMonth + 1, mDay, hourOfDay, minute)
                }, mHour, mMinute, false
            )
            timePickerDialog.show();
        }

        backBtn.setOnClickListener {
            findNavController().navigate(
                CreateAppointmentFragmentDirections.actionCreateAppointmentFragmentToAppointmentsFragment()
            )
        }

        createBtn.setOnClickListener {
            if (timeString != "" && !nameEt.text.toString().isNullOrEmpty() && !streetEt.text.toString().isNullOrEmpty() && !cityEt.text.toString().isNullOrEmpty() && state != "" && !emailEt.text.toString().isNullOrEmpty() && !phoneEt.text.toString().isNullOrEmpty()){
                val location = getLocationFromAddress("${streetEt.text}, ${cityEt.text}, ${state}")
                if (location != null){
                    val createBody = CreateAppointmentRequest(
                        timeString,
                        nameEt.text.toString(),
                        streetEt.text.toString(),
                        cityEt.text.toString(),
                        state,
                        "United States",
                        emailEt.text.toString(),
                        phoneEt.text.toString(),
                        location.latitude,
                        location.longitude
                    )
                    viewModel.createAppointment(createBody)
                    loading.visibility = View.VISIBLE
                    updateResult()
                } else {
                    generalAlert(requireContext(),"Error", "Couldn't generate location coordinates.", null, "Ok")
                }
            } else {
                val createBody = CreateAppointmentRequest(
                    timeString,
                    nameEt.text.toString(),
                    streetEt.text.toString(),
                    cityEt.text.toString(),
                    state,
                    "United States",
                    emailEt.text.toString(),
                    phoneEt.text.toString(),
                    90.0,
                    90.0
                )
                Log.e("Testing", "${createBody}")
                generalAlert(requireContext(),"Error", "All fields must be complete.", null, "Ok")
            }
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

    private fun updateResult() {
        viewModel.successCreateAppointmentMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            Toast.makeText(requireContext(), "Created Appointment", Toast.LENGTH_LONG).show()
            findNavController().navigate(CreateAppointmentFragmentDirections.actionCreateAppointmentFragmentToDetailFragment(it.appointment.id))
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

    private fun getLocationFromAddress(strAddress: String?): LatLng? {
        val coder = Geocoder(requireContext())
        val address: List<Address>?
        var p1: LatLng? = null
        try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val location: Address = address[0]
            p1 = LatLng(location.latitude, location.longitude)
            return p1
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

}