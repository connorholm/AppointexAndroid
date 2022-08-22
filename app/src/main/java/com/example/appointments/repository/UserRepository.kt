package com.example.appointments.repository

import com.example.appointments.api.AppointmentsUsersAPI
import com.example.appointments.models.auth.*
import com.example.appointments.models.user.CurrentUserResponse
import com.example.appointments.models.user.UpdateUserRequest
import com.example.appointments.models.user.UpdateUserResponse
import com.example.appointments.utils.Constants.Remote
import com.example.insuppli.repository.local.DataManager
import io.reactivex.Observable
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: AppointmentsUsersAPI) {

    fun userSignIn(bodyRequest: SignInRequest): Observable<SignInResponse> {
        return userAPI.logIn(Remote.CONTENT_TYPE_HEADER_VALUE, bodyRequest)
    }

    fun refreshToken(bodyRequest: TokenRefreshRequest): Observable<SignInResponse> {
        return userAPI.refreshToken(Remote.CONTENT_TYPE_HEADER_VALUE, bodyRequest)
    }

    fun logout(bodyRequest: TokenLogoutRequest): Observable<Response<Unit>> {
        return userAPI.logout(Remote.CONTENT_TYPE_HEADER_VALUE, bodyRequest)
    }

    fun userForgotPass(bodyRequest: ForgotPassRequest): Observable<ForgotPassResponse> {
        return userAPI.forgotPass(Remote.CONTENT_TYPE_HEADER_VALUE, bodyRequest)
    }

    fun currentUser(): Observable<CurrentUserResponse> {
        val accessToken = "Bearer "+ DataManager.getInstance().getAccessToken().toString()
        return userAPI.currentUser(Remote.CONTENT_TYPE_HEADER_VALUE, accessToken)
    }

    fun updateUser(user_id: Int, body: UpdateUserRequest): Observable<UpdateUserResponse> {
        val accessToken = "Bearer "+ DataManager.getInstance().getAccessToken().toString()
        return userAPI.updateUser(Remote.CONTENT_TYPE_HEADER_VALUE, accessToken, user_id, body)
    }

    fun sendContactEmail(body: ContactEmailRequest): Observable<ContactEmailResponse> {
        return userAPI.sendContactEmail(Remote.CONTENT_TYPE_HEADER_VALUE, body)
    }

}