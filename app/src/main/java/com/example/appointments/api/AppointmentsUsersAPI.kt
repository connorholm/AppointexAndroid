package com.example.appointments.api

import com.example.appointments.models.auth.*
import com.example.appointments.models.company.CompanyResponse
import com.example.appointments.utils.Constants
import com.example.appointments.models.user.CurrentUserResponse
import com.example.appointments.models.user.UpdateUserRequest
import com.example.appointments.models.user.UpdateUserResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface AppointmentsUsersAPI {
    companion object {
        const val USER_LOGIN_PATH = "/oauth/token"
        const val FORGOT_PASS_PATH = "/users/password"
        const val CURRENT_USER_PATH = "/api/v1/users/me"
        const val REFRESH_TOKEN = "/oauth/token"
        const val LOGOUT_PATH = "/oauth/revoke"
        const val CONTACT_PATH = "/api/v1/notifications/contact_email"

    }

    @POST(USER_LOGIN_PATH)
    fun logIn(
        @Header(Constants.Remote.CONTENT_TYPE_HEADER_KEY) contentType: String,
        @Body bodyRequest: SignInRequest
    ): Observable<SignInResponse>

    @POST(REFRESH_TOKEN)
    fun refreshToken(
        @Header(Constants.Remote.CONTENT_TYPE_HEADER_KEY) contentType: String,
        @Body bodyRequest: TokenRefreshRequest
    ): Observable<SignInResponse>

    @POST(LOGOUT_PATH)
    fun logout(
        @Header(Constants.Remote.CONTENT_TYPE_HEADER_KEY) contentType: String,
        @Body bodyRequest: TokenLogoutRequest
    ): Observable<Response<Unit>>

    @POST(FORGOT_PASS_PATH)
    fun forgotPass(
        @Header(Constants.Remote.CONTENT_TYPE_HEADER_KEY) contentType: String,
        @Body bodyRequest: ForgotPassRequest
    ): Observable<ForgotPassResponse>

    @GET(CURRENT_USER_PATH)
    fun currentUser(
        @Header(Constants.Remote.CONTENT_TYPE_HEADER_KEY) contentType: String,
        @Header("Authorization") bearerToken: String,
        ): Observable<CurrentUserResponse>

    @PUT("/users/{user_id}")
    fun updateUser(
        @Header(Constants.Remote.CONTENT_TYPE_HEADER_KEY) contentType: String,
        @Header("Authorization") bearerToken: String,
        @Path("user_id") user_id: Int,
        @Body bodyRequest: UpdateUserRequest
    ): Observable<UpdateUserResponse>

    @POST(CONTACT_PATH)
    fun sendContactEmail(
        @Header(Constants.Remote.CONTENT_TYPE_HEADER_KEY) contentType: String,
        @Body bodyRequest: ContactEmailRequest
    ): Observable<ContactEmailResponse>
}