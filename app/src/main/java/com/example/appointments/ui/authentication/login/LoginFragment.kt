package com.example.appointments.ui.authentication.login

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.appointments.R
import com.example.appointments.models.auth.SignInRequest
import com.example.appointments.ui.home.HomeActivity
import com.example.appointments.utils.Constants
import com.example.insuppli.repository.local.DataManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var resetPassTextView: TextView
    private lateinit var loginBtn: TextView
    private lateinit var signUpBtn: TextView
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
        val view: View = inflater.inflate(R.layout.fragment_login, container, false)
        setUI(view)
        return view
    }

    private fun setUI(view: View) {
        emailEditText = view.findViewById(R.id.email_et)
        passwordEditText = view.findViewById(R.id.password_et)
        resetPassTextView = view.findViewById(R.id.reset_password_tv)
        signUpBtn = view.findViewById(R.id.sign_up_btn)
        loginBtn = view.findViewById(R.id.login_btn)
        loading = view.findViewById(R.id.loading)
        loading.visibility = View.GONE

        loginBtn.setOnClickListener {
            if (emailEditText.text.toString() != "" && passwordEditText.text.toString() != "") {
                val signInModel = SignInRequest(
                    "password",
                    emailEditText.text.toString(),
                    passwordEditText.text.toString(),
                    Constants.API.CLIENT_SECRET,
                    Constants.API.CLIENT_ID
                )
                viewModel.signIn(signInModel)
                loading.visibility = View.VISIBLE
                updateResult()
            } else {
                generalAlert(
                    requireContext(),
                    "Missing Information",
                    "Both fields must be completed",
                    null,
                    "Ok"
                )
            }
        }

        signUpBtn.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }

        resetPassTextView.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
        }


    }

    private fun updateResult(){
        viewModel.successMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            Log.i("Token","Token: ${DataManager.getInstance().getAccessToken()}")
            Log.i("RefreshToken","Refresh Token: ${DataManager.getInstance().getRefreshAccessToken()}")
            viewModel.getCurrentUser()
            updateResult()
        })

        viewModel.successCurrentUserMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            startHomeActivity()
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

    private fun startHomeActivity(){
        Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_LONG).show()
        val intent = Intent(activity, HomeActivity::class.java)
        startActivity(intent)
    }
}