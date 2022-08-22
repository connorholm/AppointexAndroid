package com.example.appointments.ui.home.appointments

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import androidx.lifecycle.*
import com.example.appointments.models.appointments.*
import com.example.appointments.models.auth.SignInRequest
import com.example.appointments.models.auth.SignInResponse
import com.example.appointments.models.auth.TokenRefreshRequest
import com.example.appointments.models.company.Company
import com.example.appointments.models.company.CompanyResponse
import com.example.appointments.models.notifications.SendMessageRequest
import com.example.appointments.models.notifications.SendMessageResponse
import com.example.appointments.models.user.CurrentUserResponse
import com.example.appointments.repository.AppointmentRepository
import com.example.appointments.repository.UserRepository
import com.example.appointments.utils.Constants
import com.example.insuppli.repository.local.DataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AppointmentsViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val userRepository: UserRepository,
    ) : ViewModel() {

    var successRefreshMessage: MutableLiveData<SignInResponse> = MutableLiveData()
    var successCompanyMessage: MutableLiveData<CompanyResponse> = MutableLiveData()
    var successSendTextMessage: MutableLiveData<SendMessageResponse> = MutableLiveData()
    var successAppointmentsByNameMessage: MutableLiveData<AppointmentsSearchResponse> = MutableLiveData()
    var successAppointmentsByTimeMessage: MutableLiveData<AppointmentsSearchResponse> = MutableLiveData()
    var successAppointmentMessage: MutableLiveData<AppointmentResponse> = MutableLiveData()
    var successAppointmentCompleteMessage: MutableLiveData<AppointmentResponse> = MutableLiveData()
    var successUpdateAppointmentMessage: MutableLiveData<AppointmentResponse> = MutableLiveData()
    var successCreateAppointmentMessage: MutableLiveData<AppointmentResponse> = MutableLiveData()
    var successDeleteAppointmentMessage: MutableLiveData<DeleteAppointmentsResponse> = MutableLiveData()
    var errorMessage: MutableLiveData<String> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun getCompany(company_id: Int){
        resetVariables()
        appointmentRepository.getCompany(company_id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onCompanyStart() }
            .doOnTerminate { onCompanyFinish() }
            .subscribe ({
                    it ->
                onCompanySuccess(it)

            }, { error ->
                if (error is HttpException) {
                    if (error.response()!!.code() == 401){

                        onCompanyError("Incorrect email or password")
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
                        onCompanyError(errorString)
                    }
                } else {
                    onCompanyError(error.message!!)
                }
            })
    }

    private fun onCompanyStart() {
    }

    private fun onCompanyFinish() {
    }

    private fun onCompanySuccess(response: CompanyResponse) {
        successCompanyMessage.value = response
        DataManager.getInstance().saveCompany(response.company)
    }

    private fun onCompanyError(it: String) {
         errorMessage.value = it
    }

    @SuppressLint("CheckResult")
    fun searchByName(search: String){
        resetVariables()
        appointmentRepository.searchByName(search)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onAppointmentsByNameStart() }
            .doOnTerminate { onAppointmentsByNameFinish() }
            .subscribe ({
                    it ->
                onAppointmentsByNameSuccess(it)

            }, { error ->
                if (error is HttpException) {
                    if (error.response()!!.code() == 401){
                        onAppointmentByNameError("Incorrect email or password")
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
                        onAppointmentByNameError(errorString)
                    }
                } else {
                    onAppointmentByNameError(error.message!!)
                }
            })
    }

    private fun onAppointmentsByNameStart() {
    }

    private fun onAppointmentsByNameFinish() {
    }

    private fun onAppointmentsByNameSuccess(response: AppointmentsSearchResponse) {
        successAppointmentsByNameMessage.value = response
    }

    private fun onAppointmentByNameError(it: String) {
        errorMessage.value = it
    }

    @SuppressLint("CheckResult")
    fun searchByTime(time: String){
        resetVariables()
        appointmentRepository.searchByTime(time)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onAppointmentsByTimeStart() }
            .doOnTerminate { onAppointmentsByTimeFinish() }
            .subscribe ({
                    it ->
                onAppointmentsByTimeSuccess(it)

            }, { error ->
                if (error is HttpException) {
                    if (error.response()!!.code() == 401){
                        onAppointmentByTimeError("Incorrect email or password")
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
                        onAppointmentByTimeError(errorString)
                    }
                } else {
                    onAppointmentByTimeError(error.message!!)
                }
            })
    }

    private fun onAppointmentsByTimeStart() {
    }

    private fun onAppointmentsByTimeFinish() {
    }

    private fun onAppointmentsByTimeSuccess(response: AppointmentsSearchResponse) {
        successAppointmentsByTimeMessage.value = response
    }

    private fun onAppointmentByTimeError(it: String) {
        errorMessage.value = it
    }

    @SuppressLint("CheckResult")
    fun getAppointment(appointment_id: Int){
        resetVariables()
        appointmentRepository.getAppointment(appointment_id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onAppointmentStart() }
            .doOnTerminate { onAppointmentFinish() }
            .subscribe ({
                    it ->
                onAppointmentSuccess(it)

            }, { error ->
                if (error is HttpException) {
                    if (error.response()!!.code() == 401){
                        onAppointmentError("Incorrect email or password")
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
                        onAppointmentError(errorString)
                    }
                } else {
                    onAppointmentError(error.message!!)
                }
            })
    }

    private fun onAppointmentStart() {
    }

    private fun onAppointmentFinish() {
    }

    private fun onAppointmentSuccess(response: AppointmentResponse) {
        successAppointmentMessage.value = response
    }

    private fun onAppointmentError(it: String) {
        errorMessage.value = it
    }

    @SuppressLint("CheckResult")
    fun completeAppointment(appointment_id: Int, body: CompleteAppointmentRequest){
        resetVariables()
        appointmentRepository.completeAppointment(appointment_id, body)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onAppointmentCompleteStart() }
            .doOnTerminate { onAppointmentCompleteFinish() }
            .subscribe ({
                    it ->
                onAppointmentCompleteSuccess(it)

            }, { error ->
                if (error is HttpException) {
                    if (error.response()!!.code() == 401){
                        onAppointmentCompleteError("Incorrect email or password")
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
                        onAppointmentCompleteError(errorString)
                    }
                } else {
                    onAppointmentCompleteError(error.message!!)
                }
            })
    }

    private fun onAppointmentCompleteStart() {
    }

    private fun onAppointmentCompleteFinish() {
    }

    private fun onAppointmentCompleteSuccess(response: AppointmentResponse) {
        successAppointmentCompleteMessage.value = response
    }

    private fun onAppointmentCompleteError(it: String) {
        errorMessage.value = it
    }

    @SuppressLint("CheckResult")
    fun updateAppointment(appointment_id: Int, body: UpdateAppointmentRequest){
        resetVariables()
        appointmentRepository.updateAppointment(appointment_id, body)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onUpdateAppointmentStart() }
            .doOnTerminate { onUpdateAppointmentFinish() }
            .subscribe ({
                    it ->
                onUpdateAppointmentSuccess(it)

            }, { error ->
                if (error is HttpException) {
                    if (error.response()!!.code() == 401){
                        onUpdateAppointmentError("Unauthorized")
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
                        onUpdateAppointmentError(errorString)
                    }
                } else {
                    onUpdateAppointmentError(error.message!!)
                }
            })
    }

    private fun onUpdateAppointmentStart() {
    }

    private fun onUpdateAppointmentFinish() {
    }

    private fun onUpdateAppointmentSuccess(response: AppointmentResponse) {
        successUpdateAppointmentMessage.value = response
    }

    private fun onUpdateAppointmentError(it: String) {
        errorMessage.value = it
    }

    @SuppressLint("CheckResult")
    fun deleteAppointment(appointment_id: Int){
        resetVariables()
        appointmentRepository.deleteAppointment(appointment_id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onDeleteAppointmentStart() }
            .doOnTerminate { onDeleteAppointmentFinish() }
            .subscribe ({
                    it ->
                onDeleteAppointmentSuccess(it)

            }, { error ->
                if (error is HttpException) {
                    if (error.response()!!.code() == 401){
                        onDeleteAppointmentError("Unauthorized")
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
                        onDeleteAppointmentError(errorString)
                    }
                } else {
                    onDeleteAppointmentError(error.message!!)
                }
            })
    }

    private fun onDeleteAppointmentStart() {
    }

    private fun onDeleteAppointmentFinish() {
    }

    private fun onDeleteAppointmentSuccess(response: DeleteAppointmentsResponse) {
        successDeleteAppointmentMessage.value = response
    }

    private fun onDeleteAppointmentError(it: String) {
        errorMessage.value = it
    }

    @SuppressLint("CheckResult")
    fun createAppointment(body: CreateAppointmentRequest){
        resetVariables()
        appointmentRepository.createAppointment(body)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onCreateAppointmentStart() }
            .doOnTerminate { onCreateAppointmentFinish() }
            .subscribe ({
                    it ->
                onCreateAppointmentSuccess(it)

            }, { error ->
                if (error is HttpException) {
                    if (error.response()!!.code() == 401){
                        onCreateAppointmentError("Unauthorized")
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
                        onCreateAppointmentError(errorString)
                    }
                } else {
                    onCreateAppointmentError(error.message!!)
                }
            })
    }

    private fun onCreateAppointmentStart() {
    }

    private fun onCreateAppointmentFinish() {
    }

    private fun onCreateAppointmentSuccess(response: AppointmentResponse) {
        successCreateAppointmentMessage.value = response
    }

    private fun onCreateAppointmentError(it: String) {
        errorMessage.value = it
    }

    @SuppressLint("CheckResult")
    fun sendText(body: SendMessageRequest){
        resetVariables()
        appointmentRepository.sendMessage(body)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onSendTextStart() }
            .doOnTerminate { onSendTextFinish() }
            .subscribe ({
                    it ->
                onSendTextSuccess(it)

            }, { error ->
                if (error is HttpException) {
                    if (error.response()!!.code() == 401){
                        onSendTextError("Unauthorized")
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
                        onSendTextError(errorString)
                    }
                } else {
                    onSendTextError(error.message!!)
                }
            })
    }

    private fun onSendTextStart() {
    }

    private fun onSendTextFinish() {
    }

    private fun onSendTextSuccess(response: SendMessageResponse) {
        successSendTextMessage.value = response
    }

    private fun onSendTextError(it: String) {
        errorMessage.value = it
    }

    @SuppressLint("CheckResult")
    fun refreshToken(){
        resetVariables()
        userRepository.refreshToken(TokenRefreshRequest("refresh_token", DataManager.getInstance().getRefreshAccessToken().toString(), Constants.API.CLIENT_SECRET, Constants.API.CLIENT_ID))
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
        successRefreshMessage.value = response
    }

    private fun onAuthenticateUserError(it: String) {
        Log.e("ERROR", "onAuthenticateUserError ========> there was a problem refreshing token: $it")
        errorMessage.value = it
    }

    private fun resetVariables(){
        successRefreshMessage = MutableLiveData()
        successAppointmentsByNameMessage = MutableLiveData()
        successAppointmentsByTimeMessage = MutableLiveData()
        successAppointmentMessage = MutableLiveData()
        successCompanyMessage = MutableLiveData()
        successAppointmentCompleteMessage = MutableLiveData()
        successUpdateAppointmentMessage = MutableLiveData()
        successDeleteAppointmentMessage = MutableLiveData()
        successCreateAppointmentMessage = MutableLiveData()
        successSendTextMessage = MutableLiveData()
        errorMessage = MutableLiveData()
    }
}