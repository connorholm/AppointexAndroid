package com.example.appointments.ui.authentication.forgotpass

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.appointments.R
import com.example.appointments.models.auth.ForgotPassRequest
import com.example.appointments.models.auth.ForgotUserRequestEmail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var forgotPassBtn: TextView
    private lateinit var backBtn: ImageView
    private lateinit var loading: ProgressBar
    private val viewModel: ForgotPassViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_forgot_password, container, false)
        setUI(view)
        return view
    }
    private fun setUI(view: View) {
        emailEditText = view.findViewById(R.id.email_et)
        forgotPassBtn = view.findViewById(R.id.forgot_pass_btn)
        backBtn = view.findViewById(R.id.back_icon)
        loading = view.findViewById(R.id.loading)
        loading.visibility = View.GONE

        backBtn.setOnClickListener {
            findNavController().navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment())
        }

        forgotPassBtn.setOnClickListener {
            if (emailEditText.text.toString() != ""){
                val forgotModel = ForgotPassRequest(ForgotUserRequestEmail(emailEditText.text.toString()))
                viewModel.forgotPass(forgotModel)
                loading.visibility = View.VISIBLE
                updateResult()
            } else {
                generalAlert(requireContext(), "Missing Information", "Both fields must be completed", null, "Ok")
            }
        }
    }

    private fun updateResult(){
        viewModel.successMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            Toast.makeText(requireContext(), "Password Instructions Sent", Toast.LENGTH_LONG).show()
            findNavController().navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment())
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
}