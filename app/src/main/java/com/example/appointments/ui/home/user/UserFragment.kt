package com.example.appointments.ui.home.user

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.appointments.R
import com.example.appointments.models.auth.TokenLogoutRequest
import com.example.appointments.models.user.UpdateUserRequest
import com.example.appointments.models.user.UpdateUserRequestBody
import com.example.appointments.models.user.User
import com.example.appointments.ui.authentication.AuthenticationActivity

import com.example.appointments.utils.Constants
import com.example.insuppli.repository.local.DataManager
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class UserFragment : Fragment() {

    private lateinit var backBtn: ImageView
    private lateinit var saveBtn: TextView
    private lateinit var logoutBtn: TextView
    private lateinit var changePassBtn: TextView
    private lateinit var firstNameEt: EditText
    private lateinit var lastNameEt: EditText
    private lateinit var emailEt: EditText
    private lateinit var loading: ProgressBar
    private lateinit var currentUser: User
    private val viewModel: UserViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =  inflater.inflate(R.layout.fragment_user, container, false)
        currentUser = DataManager.getInstance().getCurrentUser()!!
        setUI(view)
        return view
    }

    private fun setUI(view: View) {
        backBtn = view.findViewById(R.id.back_icon)
        saveBtn = view.findViewById(R.id.save_btn)
        logoutBtn = view.findViewById(R.id.logout_btn)
        changePassBtn = view.findViewById(R.id.change_pass_label)
        firstNameEt = view.findViewById(R.id.first_name_et)
        lastNameEt = view.findViewById(R.id.last_name_et)
        emailEt = view.findViewById(R.id.email_et)
        loading = view.findViewById(R.id.loading)
        loading.visibility = View.GONE

        firstNameEt.setText(currentUser.firstName)
        lastNameEt.setText(currentUser.lastName)
        emailEt.setText(currentUser.email)

        changePassBtn.setOnClickListener {
            findNavController().navigate(UserFragmentDirections.actionUserFragmentToPasswordFragment())
        }
        backBtn.setOnClickListener {
            findNavController().navigate(UserFragmentDirections.actionUserFragmentToAppointmentsFragment())
        }

        saveBtn.setOnClickListener {
            viewModel.updateUser(currentUser.id, UpdateUserRequest(UpdateUserRequestBody(firstNameEt.text.toString(),lastNameEt.text.toString(),emailEt.text.toString(), "","","")))
            loading.visibility = View.VISIBLE
            updateResult()
        }

        logoutBtn.setOnClickListener {
            viewModel.logout(TokenLogoutRequest(DataManager.getInstance().getAccessToken()!!, Constants.API.CLIENT_SECRET,Constants.API.CLIENT_ID))
            loading.visibility = View.VISIBLE
            updateResult()
        }
    }

    private fun updateResult(){
        viewModel.successLogoutMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            startAuthActivity()
        })

        viewModel.succesUpdateUserMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            Toast.makeText(requireContext(), "User Updated", Toast.LENGTH_LONG).show()
            findNavController().navigate(UserFragmentDirections.actionUserFragmentToAppointmentsFragment())
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

    private fun startAuthActivity(){
        Toast.makeText(requireContext(), "Logout Successful", Toast.LENGTH_LONG).show()
        val intent = Intent(activity, AuthenticationActivity::class.java)
        startActivity(intent)
    }

}