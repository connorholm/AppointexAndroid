package com.example.appointments.ui.authentication.home

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import androidx.lifecycle.*
import com.example.appointments.models.auth.SignInRequest
import com.example.appointments.models.auth.SignInResponse
import com.example.appointments.models.auth.TokenRefreshRequest
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
class HomeAuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    ) : ViewModel() {

    var successMessage: MutableLiveData<SignInResponse> = MutableLiveData()
    var successCurrentUserMessage: MutableLiveData<CurrentUserResponse> = MutableLiveData()
    var errorMessage: MutableLiveData<String> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun refreshToken(loginBody: TokenRefreshRequest){
        resetVariables()
        userRepository.refreshToken(loginBody)
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
        Log.e("ERROR", "onAuthenticateUserError ========> there was a problem refreshing token: $it")
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

    private fun resetVariables(){
        successMessage= MutableLiveData()
        successCurrentUserMessage = MutableLiveData()
        errorMessage = MutableLiveData()
    }
}