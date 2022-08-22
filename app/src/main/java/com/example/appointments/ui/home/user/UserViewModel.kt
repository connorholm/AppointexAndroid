package com.example.appointments.ui.home.user

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import androidx.lifecycle.*
import com.example.appointments.models.auth.SignInRequest
import com.example.appointments.models.auth.SignInResponse
import com.example.appointments.models.auth.TokenLogoutRequest
import com.example.appointments.models.user.CurrentUserResponse
import com.example.appointments.models.user.UpdateUserRequest
import com.example.appointments.models.user.UpdateUserResponse
import com.example.appointments.repository.UserRepository
import com.example.insuppli.repository.local.DataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    ) : ViewModel() {

    var successLogoutMessage: MutableLiveData<Response<Unit>> = MutableLiveData()
    var succesUpdateUserMessage: MutableLiveData<UpdateUserResponse> = MutableLiveData()
    var errorMessage: MutableLiveData<String> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun logout(logoutBody: TokenLogoutRequest){
        resetVariables()
        userRepository.logout(logoutBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onLogoutStart() }
            .doOnTerminate { onLogoutFinish() }
            .subscribe ({
                    it ->
                onLogoutSuccess(it)

            }, { error ->
//                var message = "Something went wrong, please try again"
                if (error is HttpException) {
                    Log.e("Testing","Error: ${error.response().toString()}")
                    if (error.response()!!.code() == 401){
                        onLogoutError("Incorrect email or password")
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
                        onLogoutError(errorString)
                    }
                } else {
                    onLogoutError(error.message!!)
                }
            })
    }

    private fun onLogoutStart() {
    }

    private fun onLogoutFinish() {
    }

    private fun onLogoutSuccess(response: Response<Unit>) {
        DataManager.getInstance().torchAccessTokens()
        DataManager.getInstance().torchCurrentUser()
        DataManager.getInstance().torchCompany()
        DataManager.getInstance().torchFilters()
        successLogoutMessage.value = response
    }

    private fun onLogoutError(it: String) {
         errorMessage.value = it
    }

    @SuppressLint("CheckResult")
    fun updateUser(user_id: Int, updateBody: UpdateUserRequest){
        resetVariables()
        userRepository.updateUser(user_id, updateBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onStart() }
            .doOnTerminate { onFinish() }
            .subscribe ({
                    it ->
                onUpdateUserSuccess(it)

            }, { error ->
//                var message = "Something went wrong, please try again"
                if (error is HttpException) {
                    Log.e("Testing","Error: ${error.response().toString()}")
                    if (error.response()!!.code() == 401){
                        onUpdateUserError("Incorrect email or password")
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
                        onUpdateUserError(errorString)
                    }
                } else {
                    onUpdateUserError(error.message!!)
                }
            })
    }

    private fun onStart() {
    }

    private fun onFinish() {
    }

    private fun onUpdateUserSuccess(response: UpdateUserResponse) {
        DataManager.getInstance().saveCurrentUser(response.user)
        succesUpdateUserMessage.value = response
    }

    private fun onUpdateUserError(it: String) {
        errorMessage.value = it
    }

    private fun resetVariables(){
        succesUpdateUserMessage = MutableLiveData()
        successLogoutMessage= MutableLiveData()
        errorMessage = MutableLiveData()
    }
}