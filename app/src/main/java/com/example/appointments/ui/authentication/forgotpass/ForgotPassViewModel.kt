package com.example.appointments.ui.authentication.forgotpass

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import androidx.lifecycle.*
import com.example.appointments.models.auth.ForgotPassRequest
import com.example.appointments.models.auth.ForgotPassResponse
import com.example.appointments.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ForgotPassViewModel @Inject constructor(
    private val userRepository: UserRepository,
    ) : ViewModel() {

    var successMessage: MutableLiveData<ForgotPassResponse> = MutableLiveData()
    var errorMessage: MutableLiveData<String> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun forgotPass(forgotBody: ForgotPassRequest){
        resetVariables()
        userRepository.userForgotPass(forgotBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onStart() }
            .doOnTerminate { onFinish() }
            .subscribe ({
                    it ->
                onForgotPassSuccess(it)

            }, { error ->
                if (error is HttpException) {
                    if (error.response()!!.code() == 401){
                        onForgotPassError("Incorrect email or password")
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
                        onForgotPassError(errorString)
                    }
                } else {
                    onForgotPassError(error.message!!)
                }
            })
    }

    private fun onStart() {
    }

    private fun onFinish() {
    }

    private fun onForgotPassSuccess(response: ForgotPassResponse) {
        successMessage.value = response
    }

    private fun onForgotPassError(it: String) {
         errorMessage.value = it
    }

    private fun resetVariables(){
        successMessage= MutableLiveData()
        errorMessage = MutableLiveData()
    }
}