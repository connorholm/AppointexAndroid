package com.example.appointments.ui.authentication.login

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import androidx.lifecycle.*
import com.example.appointments.models.auth.ContactEmailRequest
import com.example.appointments.models.auth.ContactEmailResponse
import com.example.appointments.models.auth.SignInRequest
import com.example.appointments.models.auth.SignInResponse
import com.example.appointments.models.user.CurrentUserResponse
import com.example.appointments.repository.UserRepository
import com.example.insuppli.repository.local.DataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    ) : ViewModel() {

    var successMessage: MutableLiveData<SignInResponse> = MutableLiveData()
    var successCurrentUserMessage: MutableLiveData<CurrentUserResponse> = MutableLiveData()
    var successSendContactMessage: MutableLiveData<ContactEmailResponse> = MutableLiveData()
    var errorMessage: MutableLiveData<String> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun signIn(loginBody: SignInRequest){
        resetVariables()
        userRepository.userSignIn(loginBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onAuthenticateUserStart() }
            .doOnTerminate { onAuthenticateUserFinish() }
            .subscribe ({
                    it ->
                onAuthenticateUserSuccess(it)

            }, { error ->
//                var message = "Something went wrong, please try again"
                if (error is HttpException) {
                    Log.e("Testing","Error: ${error.response().toString()}")
                    if (error.response()!!.code() == 401){
                        onAuthenticateUserError("Incorrect email or password")
                    } else if (error.response()!!.code() == 400){
                        onSendContactError("Invalid email or password.")
                    } else {
                        val jObjError = JSONObject(error.response()!!.errorBody()!!.string())
                        var errorString = "An error occurred"
                        try {
                            errorString = jObjError.getJSONArray("errors").get(0).toString()
                        }catch (e: Exception){
                            try {
                                errorString = jObjError.getJSONArray("base").get(0).toString()
                            }catch (e: Exception){
                                errorString = jObjError.toString()
                            }
                        }
                        onAuthenticateUserError(errorString)
                    }
                } else {
                    onAuthenticateUserError(error.message!!)
                }
            })
    }

    private fun onAuthenticateUserStart() {
    }

    private fun onAuthenticateUserFinish() {
    }

    private fun onAuthenticateUserSuccess(response: SignInResponse) {
        DataManager.getInstance().saveAccessToken(response.access_token, response.refresh_token)
        successMessage.value = response
    }

    private fun onAuthenticateUserError(it: String) {
        Log.e("ERROR", "onAuthenticateUserError ========> there was a problem getting token: $it")
         errorMessage.value = it
    }

    @SuppressLint("CheckResult")
    fun getCurrentUser(){
        resetVariables()
        userRepository.currentUser()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onStart() }
            .doOnTerminate { onFinish() }
            .subscribe ({
                    it ->
                onCurrentUserSuccess(it)

            }, { error ->
//                var message = "Something went wrong, please try again"
                if (error is HttpException) {
                    Log.e("Testing","Error: ${error.response().toString()}")
                    if (error.response()!!.code() == 401){
                        onCurrentUserError("Incorrect email or password")
                    } else {
                        val jObjError = JSONObject(error.response()!!.errorBody()!!.string())
                        var errorString = "An error occurred"
                        try {
                            errorString = jObjError.getJSONArray("errors").get(0).toString()
                        }catch (e: Exception){
                            try {
                                errorString = jObjError.getJSONArray("base").get(0).toString()
                            }catch (e: Exception){
                                errorString = jObjError.toString()
                            }
                        }
                        onCurrentUserError(errorString)
                    }
                } else {
                    onCurrentUserError(error.message!!)
                }
            })
    }

    private fun onStart() {
    }

    private fun onFinish() {
    }

    private fun onCurrentUserSuccess(response: CurrentUserResponse) {
        DataManager.getInstance().saveCurrentUser(response.user)
        successCurrentUserMessage.value = response
    }

    private fun onCurrentUserError(it: String) {
        errorMessage.value = it
    }

    @SuppressLint("CheckResult")
    fun sendContactEmail(body: ContactEmailRequest){
        resetVariables()
        userRepository.sendContactEmail(body)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onSendContactStart() }
            .doOnTerminate { onSendContactFinish() }
            .subscribe ({
                    it ->
                onSendContactSuccess(it)

            }, { error ->
                if (error is HttpException) {
                    if (error.response()!!.code() == 401){
                        onSendContactError("Not authorized. Try restarting app")
                    } else {
                        val jObjError = JSONObject(error.response()!!.errorBody()!!.string())
                        var errorString = "An error occurred"
                        try {
                            errorString = jObjError.getJSONArray("errors").get(0).toString()
                        }catch (e: Exception){
                            try {
                                errorString = jObjError.getJSONArray("base").get(0).toString()
                            }catch (e: Exception){
                                errorString = jObjError.toString()
                            }
                        }
                        onSendContactError(errorString)
                    }
                } else {
                    onSendContactError(error.message!!)
                }
            })
    }

    private fun onSendContactStart() {
    }

    private fun onSendContactFinish() {
    }

    private fun onSendContactSuccess(response: ContactEmailResponse) {
        successSendContactMessage.value = response
    }

    private fun onSendContactError(it: String) {
        errorMessage.value = it
    }

    private fun resetVariables(){
        successCurrentUserMessage = MutableLiveData()
        successSendContactMessage = MutableLiveData()
        successMessage= MutableLiveData()
        errorMessage = MutableLiveData()
    }
}