package com.example.appointments.ui.authentication.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.appointments.R
import com.example.appointments.models.auth.SignInRequest
import com.example.appointments.models.auth.TokenRefreshRequest
import com.example.appointments.ui.authentication.login.LoginViewModel
import com.example.appointments.ui.home.HomeActivity
import com.example.appointments.utils.Constants
import com.example.insuppli.repository.local.DataManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeAuthFragment : Fragment() {

    private lateinit var loading: ProgressBar
    private val viewModel: HomeAuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home_auth, container, false)
        setUI(view)
        if (DataManager.getInstance().getRefreshAccessToken() == null){
            findNavController().navigate(HomeAuthFragmentDirections.actionHomeAuthFragmentToLoginFragment())
        } else {
            viewModel.refreshToken(TokenRefreshRequest("refresh_token", DataManager.getInstance().getRefreshAccessToken().toString(), Constants.API.CLIENT_SECRET, Constants.API.CLIENT_ID))
            loading.visibility = View.VISIBLE
            updateResult()

        }
        return view
    }

    private fun setUI(view: View){
        loading = view.findViewById(R.id.loading)
        loading.visibility = View.GONE

    }

    private fun updateResult(){
        viewModel.successMessage.observe(viewLifecycleOwner, Observer {
            viewModel.getCurrentUser()
            updateResult()
        })

        viewModel.successCurrentUserMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            Log.i("Login", "Successful token refresh")
            Log.e("Testing", "current user: ${it.user.toString()}")
            startHomeActivity()
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            generalAlert(requireContext(),"Error",it,null,"Ok")
            findNavController().navigate(HomeAuthFragmentDirections.actionHomeAuthFragmentToLoginFragment())
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
        val intent = Intent(activity, HomeActivity::class.java)
        startActivity(intent)
    }
}