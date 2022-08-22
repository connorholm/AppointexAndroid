package com.example.appointments.ui.authentication.signup

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.appointments.R
import com.example.appointments.models.auth.ContactEmailRequest
import com.example.appointments.ui.authentication.forgotpass.ForgotPassViewModel
import com.example.appointments.ui.authentication.forgotpass.ForgotPasswordFragmentDirections
import com.example.appointments.ui.authentication.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var sendBtn: TextView
    private lateinit var backBtn: ImageView
    private lateinit var loading: ProgressBar
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_sign_up, container, false)
        setUI(view)
        return view
    }

    private fun setUI(view: View) {
        emailEditText = view.findViewById(R.id.email_et)
        sendBtn = view.findViewById(R.id.send_btn)
        backBtn = view.findViewById(R.id.back_icon)
        loading = view.findViewById(R.id.loading)
        loading.visibility = View.GONE

        backBtn.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
        }

        sendBtn.setOnClickListener {
            val email = emailEditText.text.toString()
            if (email.isEmailValid()) {
                loading.visibility = View.VISIBLE
                viewModel.sendContactEmail(ContactEmailRequest(email))
                updateResult()
            } else {
               generalAlert(requireContext(), "Error", "Email must be valid", null, "Ok")
            }
        }
    }

    private fun updateResult(){
        viewModel.successSendContactMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            Toast.makeText(requireContext(), "Email Sent", Toast.LENGTH_LONG).show()
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            generalAlert(requireContext(),"Error",it,null,"Ok")
        })
    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
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