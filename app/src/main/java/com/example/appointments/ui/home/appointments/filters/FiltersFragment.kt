package com.example.appointments.ui.home.appointments.filters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.appointments.R
import com.example.appointments.models.appointments.Appointment
import com.example.appointments.models.user.User
import com.example.appointments.ui.home.appointments.AppointmentsListAdapter
import com.example.appointments.ui.home.appointments.AppointmentsViewModel
import com.example.insuppli.repository.local.DataManager
import java.sql.Time
import java.text.DateFormat
import java.text.Format
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class FiltersFragment : Fragment() {

    private lateinit var backBtn: ImageView
    private lateinit var toggleGroup: RadioGroup
    private lateinit var nameRadioBtn: RadioButton
    private lateinit var timeRadioBtn: RadioButton
    private lateinit var loading: ProgressBar
    private lateinit var dateTv: TextView
    private lateinit var dateBtn: Button
    private lateinit var searchBtn: TextView
    private lateinit var nameEt: EditText
    private lateinit var nameLayout: ConstraintLayout
    private lateinit var dateLayout: ConstraintLayout
    private var nameSelected = true
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0
    private lateinit var timeString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_filters, container, false)
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
        toggleGroup = view.findViewById(R.id.radioGroup)
        nameRadioBtn = view.findViewById(R.id.nameRadioButton)
        timeRadioBtn = view.findViewById(R.id.timeRadioButton)
        loading = view.findViewById(R.id.loading)
        dateTv = view.findViewById(R.id.date_tv)
        dateBtn = view.findViewById(R.id.edit_date_btn)
        searchBtn = view.findViewById(R.id.search_btn)
        nameEt = view.findViewById(R.id.client_name_et)
        dateLayout = view.findViewById(R.id.date_layout)
        nameLayout = view.findViewById(R.id.name_layout)
        dateLayout.visibility = View.GONE

        loading.visibility = View.GONE

        updateISO8601Time(mYear, mMonth + 1, mDay, mHour, mMinute)
        dateTv.text = getDate(timeString)

        toggleGroup.setOnCheckedChangeListener { radioGroup, radioButtonID ->
            val selectedRadioButton = radioGroup.findViewById<RadioButton>(radioButtonID)
            if (selectedRadioButton.text == "Name"){
                nameSelected = true
                nameLayout.visibility = View.VISIBLE
                dateLayout.visibility = View.GONE
            } else if (selectedRadioButton.text == "Date"){
                nameSelected = false
                nameLayout.visibility = View.GONE
                dateLayout.visibility = View.VISIBLE
            }
        }

        dateBtn.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    updateISO8601Time(year, monthOfYear + 1, dayOfMonth, mHour, mMinute)
                    dateTv.text = getDate(timeString)
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        searchBtn.setOnClickListener {
            if (nameSelected) {
                if (nameEt.text.toString() != ""){
                    DataManager.getInstance().saveFilters(nameEt.text.toString(), "name")
                    findNavController().navigate(FiltersFragmentDirections.actionFiltersFragmentToAppointmentsFragment())
                } else {
                    generalAlert(requireContext(),"Error", "Name Field Cannot Be Empty.", null, "Ok")
                }
            } else {
                DataManager.getInstance().saveFilters(timeString, "time")
                findNavController().navigate(FiltersFragmentDirections.actionFiltersFragmentToAppointmentsFragment())
            }
        }

        backBtn.setOnClickListener {
            DataManager.getInstance().torchFilters()
            findNavController().navigate(FiltersFragmentDirections.actionFiltersFragmentToAppointmentsFragment())
        }
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

    private fun getTime(hr: Int, min: Int): String? {
        val tme = Time(hr, min, 0) //seconds by default set to zero
        val formatter: Format
        formatter = SimpleDateFormat("h:mm a")
        return formatter.format(tme)
    }

    @SuppressLint("NewApi")
    private fun getDate(timeString: String): String {
        val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val date = LocalDate.parse(timeString , firstApiFormat)
        val substring = date.month.toString().subSequence(0,1)
        val end = date.month.toString().substring(1).lowercase()
        return "${substring}${end} ${date.dayOfMonth}, ${date.year}"
    }

    @SuppressLint("NewApi")
    private fun updateISO8601Time(year: Int, month: Int, day: Int, hour: Int, minute: Int) {
        val localDate = LocalDateTime.of(year, month, day, hour, minute)
        val offsetDate = OffsetDateTime.of(localDate, ZoneOffset.UTC)
        timeString = offsetDate.format(DateTimeFormatter.ISO_DATE_TIME)
    }
}