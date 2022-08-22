package com.example.appointments.ui.home.user.password

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
import com.example.appointments.models.auth.TokenLogoutRequest
import com.example.appointments.models.user.UpdateUserRequest
import com.example.appointments.models.user.UpdateUserRequestBody
import com.example.appointments.models.user.User
import com.example.appointments.ui.home.user.UserFragmentDirections
import com.example.appointments.ui.home.user.UserViewModel
import com.example.appointments.utils.Constants
import com.example.insuppli.repository.local.DataManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordFragment : Fragment() {

    private lateinit var backBtn: ImageView
    private lateinit var saveBtn: TextView
    private lateinit var forgotPassBtn: TextView
    private lateinit var oldPassEt: EditText
    private lateinit var newPassEt: EditText
    private lateinit var confirmPassEt: EditText
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
        val view: View = inflater.inflate(R.layout.fragment_password, container, false)
        currentUser = DataManager.getInstance().getCurrentUser()!!
        setUI(view)
        return view
    }

    private fun setUI(view: View) {

        backBtn = view.findViewById(R.id.back_icon)
        saveBtn = view.findViewById(R.id.save_btn)
        forgotPassBtn = view.findViewById(R.id.forgot_pass_label)
        oldPassEt = view.findViewById(R.id.old_pass_et)
        newPassEt = view.findViewById(R.id.new_pass_et)
        confirmPassEt = view.findViewById(R.id.confirm_pass_et)
        loading = view.findViewById(R.id.loading)
        loading.visibility = View.GONE

        forgotPassBtn.setOnClickListener {
            findNavController().navigate(PasswordFragmentDirections.actionPasswordFragmentToForgotPassFragment())
        }

        backBtn.setOnClickListener {
            findNavController().navigate(PasswordFragmentDirections.actionPasswordFragmentToUserFragment())
        }

        saveBtn.setOnClickListener {
            if (newPassEt.text.toString() != "" && confirmPassEt.text.toString() != "" && oldPassEt.text.toString() != ""){
                viewModel.updateUser(currentUser.id, UpdateUserRequest(UpdateUserRequestBody(currentUser.firstName,currentUser.lastName,currentUser.email, newPassEt.text.toString(),confirmPassEt.text.toString(),oldPassEt.text.toString())))
                loading.visibility = View.VISIBLE
                updateResult()
            } else {
                generalAlert(requireContext(),"Error","Make sure all fields are filled in.",null,"Ok")
            }
        }
    }

    private fun updateResult(){
        viewModel.succesUpdateUserMessage.observe(viewLifecycleOwner, Observer {
            loading.visibility = View.GONE
            Toast.makeText(requireContext(), "Password Updated", Toast.LENGTH_LONG).show()
            findNavController().navigate(PasswordFragmentDirections.actionPasswordFragmentToUserFragment())
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