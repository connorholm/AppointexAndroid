package com.example.appointments.ui.home.appointments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appointments.R
import com.example.appointments.models.appointments.Appointment
import com.example.appointments.models.user.User
import com.example.insuppli.repository.local.DataManager
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.button.MaterialButtonToggleGroup.OnButtonCheckedListener
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AppointmentsFragment : Fragment(), AppointmentsAdapterCallbacks {

    private lateinit var appointmentsRV: RecyclerView
    private lateinit var adapter: AppointmentsListAdapter
    private lateinit var createBtn: ImageView
    private lateinit var searchBtn: ImageView
    private lateinit var clearBtn: Button
    private lateinit var userBtn: ImageView
    private lateinit var noResultsImage: ImageView
    private lateinit var pageTV: TextView
    private lateinit var titleTv: TextView
    private lateinit var toggleGroup: RadioGroup
    private lateinit var ongoingBtn: RadioButton
    private lateinit var completedBtn: RadioButton
    private lateinit var loading: ProgressBar
    private lateinit var user: User
    private lateinit var appointmentsList: List<Appointment>
    private lateinit var completedAppointmentsList: List<Appointment>
    private val viewModel: AppointmentsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =  inflater.inflate(R.layout.fragment_appointments, container, false)
        setUI(view)
        user = DataManager.getInstance().getCurrentUser()!!

        viewModel.getCompany(user.company_id)
        loading.visibility = View.VISIBLE
        updateResult()
        callAppointments()

        return view
    }

    @SuppressLint("NewApi")
    private fun callAppointments(){
        val filter = DataManager.getInstance().getFilters()
        val filterType = DataManager.getInstance().getFilterType()
        loading.visibility = View.VISIBLE
        if (filter.isNullOrEmpty()) {
            clearBtn.visibility = View.GONE
            titleTv.text = "Today's Appointments"
            val time: String = LocalDateTime.now().toString()
            viewModel.searchByTime(time)
            updateResult()
        } else {
            if (filterType == "name") {
                clearBtn.visibility = View.VISIBLE
                titleTv.text = "Search: ${filter}"
                viewModel.searchByName(filter)
                updateResult()
            } else {
                clearBtn.visibility = View.VISIBLE
                titleTv.text = "Date: ${getDate(filter)}"
                viewModel.searchByTime(filter)
                updateResult()
            }
        }
    }

    private fun setUI(view: View) {
        createBtn = view.findViewById(R.id.create_icon)
        userBtn = view.findViewById(R.id.user_icon)
        searchBtn = view.findViewById(R.id.search_icon)
        clearBtn = view.findViewById(R.id.clear_filters_btn)
        pageTV = view.findViewById(R.id.page_title)
        titleTv = view.findViewById(R.id.appointment_title_tv)
        loading = view.findViewById(R.id.loading)
        appointmentsRV = view.findViewById(R.id.appointmentsRV)
        toggleGroup = view.findViewById(R.id.radioGroup)
        ongoingBtn = view.findViewById(R.id.onGoingRadioButton)
        completedBtn = view.findViewById(R.id.completeRadioButton)
        noResultsImage = view.findViewById(R.id.no_results_iv)
        noResultsImage.visibility = View.GONE
        loading.visibility = View.GONE
        clearBtn.visibility = View.GONE

        clearBtn.setOnClickListener {
            DataManager.getInstance().torchFilters()
            callAppointments()
        }

        searchBtn.setOnClickListener {
            findNavController().navigate(AppointmentsFragmentDirections.actionAppointmentsFragmentToFiltersFragment())
        }

        toggleGroup.setOnCheckedChangeListener { radioGroup, radioButtonID ->
            val selectedRadioButton = radioGroup.findViewById<RadioButton>(radioButtonID)
            if (selectedRadioButton.text == "On-Going"){
                adapter.changeAppointmentList(appointmentsList)
                if (appointmentsList.isEmpty()){
                    noResultsImage.visibility = View.VISIBLE
                } else {
                    noResultsImage.visibility = View.GONE
                }
            } else if (selectedRadioButton.text == "Completed"){
                adapter.changeAppointmentList(completedAppointmentsList)
                if (completedAppointmentsList.isEmpty()){
                    noResultsImage.visibility = View.VISIBLE
                } else {
                    noResultsImage.visibility = View.GONE
                }
            }
        }


        userBtn.setOnClickListener {
            findNavController().navigate(AppointmentsFragmentDirections.actionAppointmentsFragmentToUserFragment())
        }

        createBtn.setOnClickListener {
            findNavController().navigate(AppointmentsFragmentDirections.actionAppointmentsFragmentToCreateAppointmentFragment())
        }

        initializeRecyclerView()
    }

    override fun onResume() {
        Log.e("Testing","hello this is a test")
        super.onResume()
        viewModel.refreshToken()
        updateResult()
        loading.visibility = View.VISIBLE
    }

    private fun initializeRecyclerView() {
        adapter = AppointmentsListAdapter(this)
        appointmentsRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        appointmentsRV.adapter = adapter
    }

    private fun updateResult(){
        viewModel.successCompanyMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            pageTV.text = it.company.name
        })

        viewModel.successRefreshMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            callAppointments()
        })

        viewModel.successAppointmentsByNameMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            adapter.changeAppointmentList(it.appointments)
            appointmentsList = it.appointments
            completedAppointmentsList = it.completed_appointments
            if (appointmentsList.isEmpty()){
                noResultsImage.visibility = View.VISIBLE
            }
//            DataManager.getInstance().torchFilters()
        })

        viewModel.successAppointmentsByTimeMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            Log.e("Test","Appointments: ${it.appointments}")
            adapter.changeAppointmentList(it.appointments)
            appointmentsList = it.appointments
            completedAppointmentsList = it.completed_appointments
            if (appointmentsList.isEmpty()){
                noResultsImage.visibility = View.VISIBLE
            }
//            DataManager.getInstance().torchFilters()
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

    override fun onAppointmentSelected(appointment: Appointment) {
        findNavController().navigate(AppointmentsFragmentDirections.actionAppointmentsFragmentToDetailFragment(appointment.id))
    }

    override fun onEmptyData() {
    }

    override fun onNonEmptyData() {
    }

    @SuppressLint("NewApi")
    private fun getDate(timeString: String): String {
        val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val date = LocalDate.parse(timeString , firstApiFormat)
        val substring = date.month.toString().subSequence(0,1)
        val end = date.month.toString().substring(1).lowercase()
        return "${substring}${end} ${date.dayOfMonth}"
    }

}