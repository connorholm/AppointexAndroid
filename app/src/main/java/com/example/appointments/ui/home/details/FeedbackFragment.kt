package com.example.appointments.ui.home.details

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.appointments.R
import com.example.appointments.models.appointments.CompleteAppointmentRequest
import com.example.appointments.models.notifications.SendMessageRequest
import com.example.appointments.ui.home.appointments.AppointmentsViewModel
import com.example.insuppli.repository.local.DataManager
import com.google.android.gms.maps.SupportMapFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedbackFragment : Fragment() {

    private lateinit var backBtn: ImageView
    private lateinit var sendBtn: TextView
    private lateinit var previewTv: TextView
    private lateinit var previewLayout: ConstraintLayout
    private lateinit var previewET: EditText
    private lateinit var loading: ProgressBar
    private val args by navArgs<FeedbackFragmentArgs>()
    private val viewModel: AppointmentsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_feedback, container, false)
        setUI(view)
        return view
    }

    private fun setUI(view: View) {
        backBtn = view.findViewById(R.id.back_icon)
        sendBtn = view.findViewById(R.id.send_btn)
        previewTv = view.findViewById(R.id.preview_tv)
        previewLayout = view.findViewById(R.id.preview_layout)
        previewET = view.findViewById(R.id.preview_et)
        loading = view.findViewById(R.id.loading)
        loading.visibility = View.GONE

        previewET.setText("This is ${DataManager.getInstance().getCompany()!!.name}. We hope your appointment went well and would appreciate it if you could rate us on google. Thank you! https://search.google.com/local/writereview?placeid=ChIJJ5mxxWUb9ocRkbZNKFNVCJQ")


        sendBtn.setOnClickListener {
            if (previewET.text.toString() != ""){
                if (previewET.text.toString().length < 300){
                    viewModel.sendText(SendMessageRequest(previewET.text.toString(), "+1${args.appointmentPhone}"))
                    updateResult()
                    loading.visibility = View.VISIBLE
                } else {
                    generalAlert(requireContext(), "Error", "Minutes Field Must Be Less Than 300 characters.", null, "Ok")
                }
            } else {
                generalAlert(requireContext(), "Error", "Minutes Field Must Be Complete to Send Message.", null, "Ok")
            }
        }

        backBtn.setOnClickListener {
            findNavController().navigate(FeedbackFragmentDirections.actionFeedbackFragmentToDetailFragment(args.appointmentId))
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

    private fun updateResult(){

        viewModel.successSendTextMessage.observe(viewLifecycleOwner, Observer {
            viewModel.completeAppointment(args.appointmentId, CompleteAppointmentRequest(true))
            Toast.makeText(requireContext(), "Message Sent", Toast.LENGTH_LONG).show()
            updateResult()
        })

        viewModel.successAppointmentCompleteMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            findNavController().navigate(FeedbackFragmentDirections.actionFeedbackFragmentToAppointmentsFragment())
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            generalAlert(requireContext(),"Error",it,null,"Ok")
        })
    }

}