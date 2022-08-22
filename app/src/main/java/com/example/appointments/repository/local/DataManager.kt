package com.example.insuppli.repository.local

import android.content.Context
import android.util.Log
import com.example.appointments.models.company.Company
import com.example.appointments.models.user.CurrentUserResponse
import com.example.appointments.models.user.User
import com.example.appointments.utils.Constants
import com.example.appointments.utils.Constants.SharedPreference
import com.example.appointments.utils.Utilities
import com.example.insuppli.repository.local.sharedpreference.SharedPreferenceManager
import com.google.gson.Gson


class DataManager private constructor(context: Context) {

    private val sharedPreferenceManager: SharedPreferenceManager

    init {
        //Shared Preference Initialization
        SharedPreferenceManager.init(context)
        sharedPreferenceManager = SharedPreferenceManager.getInstance()
    }

    companion object {
        @Volatile
        private var INSTANCE: DataManager? = null

        fun init(context: Context): DataManager = INSTANCE ?: synchronized(this) {
            INSTANCE ?: DataManager(context).also { INSTANCE = it }
        }

        fun getInstance() = INSTANCE
                ?: throw IllegalStateException("DataManager not initialized." +
                        " Call init() before calling getInstance()")
    }

    fun saveAccessToken(tokenResponse: String, refreshToken: String) {
        sharedPreferenceManager.putString(Constants.SharedPreference.ACCESS_TOKEN, tokenResponse)
        sharedPreferenceManager.putString(Constants.SharedPreference.REFRESH_TOKEN, refreshToken)
        sharedPreferenceManager.putBoolean(SharedPreference.LOGGED_IN, true)
        sharedPreferenceManager.save()
    }

    fun saveCurrentUser(user: User) {
        val gson = Gson()
        val json = gson.toJson(user)
        sharedPreferenceManager.putString(Constants.SharedPreference.CURRENT_USER, json)
        sharedPreferenceManager.save()
    }

    fun saveCompany(company: Company) {
        val gson = Gson()
        val json = gson.toJson(company)
        sharedPreferenceManager.putString(Constants.SharedPreference.COMPANY, json)
        sharedPreferenceManager.save()
    }

    fun saveFilters(searchQuery: String, filter_type: String){
        sharedPreferenceManager.putString(Constants.SharedPreference.FILTERS, searchQuery)
        sharedPreferenceManager.putString(Constants.SharedPreference.FILTER_TYPE, filter_type)
        sharedPreferenceManager.save()
    }

    fun torchFilters(){
        sharedPreferenceManager.remove(SharedPreference.FILTERS)
        sharedPreferenceManager.remove(SharedPreference.FILTER_TYPE)
        sharedPreferenceManager.save()
    }

    fun getFilters(): String? {
        return sharedPreferenceManager.getString(Constants.SharedPreference.FILTERS, "")
    }
    fun getFilterType(): String? {
        return sharedPreferenceManager.getString(Constants.SharedPreference.FILTER_TYPE, "")
    }

    fun torchAccessTokens() {
        sharedPreferenceManager.remove(SharedPreference.ACCESS_TOKEN)
        sharedPreferenceManager.remove(SharedPreference.TOKEN_EXPIRY_ON)
        sharedPreferenceManager.remove(SharedPreference.REFRESH_TOKEN)
        sharedPreferenceManager.remove(SharedPreference.TOKEN_TYPE)
        sharedPreferenceManager.remove(SharedPreference.CREATED_AT)
        sharedPreferenceManager.remove(SharedPreference.LOGGED_IN)
        sharedPreferenceManager.remove(SharedPreference.USER_OBJECT)
        sharedPreferenceManager.save()
    }

    fun getCurrentUser(): User? {
        val gson = Gson()
        val json: String =
            sharedPreferenceManager.getString(Constants.SharedPreference.CURRENT_USER, "")!!
        return gson.fromJson(json, User::class.java)
    }

    fun torchCurrentUser() {
        sharedPreferenceManager.remove(SharedPreference.CURRENT_USER)
        sharedPreferenceManager.save()
    }

    fun getCompany(): Company? {
        val gson = Gson()
        val json: String =
            sharedPreferenceManager.getString(Constants.SharedPreference.COMPANY, "")!!
        return gson.fromJson(json, Company::class.java)
    }

    fun torchCompany() {
        sharedPreferenceManager.remove(SharedPreference.COMPANY)
        sharedPreferenceManager.save()
    }

    fun getRefreshAccessToken() = sharedPreferenceManager.getString(SharedPreference.REFRESH_TOKEN)

    fun getAccessToken() = sharedPreferenceManager.getString(SharedPreference.ACCESS_TOKEN)

    fun getLoggedIn() = sharedPreferenceManager.getBoolean(SharedPreference.LOGGED_IN)

    fun getSavedBearerAccessToken() = "${Constants.Remote.AUTH_PREFIX} ${getAccessToken()}"

    fun getCreatedAt() =  sharedPreferenceManager.getInt(SharedPreference.CREATED_AT)


    fun getTokenType() = sharedPreferenceManager.getString(SharedPreference.TOKEN_TYPE)

    fun saveRemindMeLater(dateTime: Long) {
        sharedPreferenceManager.putLong(SharedPreference.REMIND_ME_EXPIRY_ON, dateTime)
        sharedPreferenceManager.save()
    }

    fun getRemindMeLater() = sharedPreferenceManager.getLong(SharedPreference.REMIND_ME_EXPIRY_ON)

    fun isLoggedIn() = sharedPreferenceManager.getBoolean(SharedPreference.LOGGED_IN)

    fun saveUserID(value: String) {
        sharedPreferenceManager.saveString(SharedPreference.USER_ID, value)
    }

    fun saveEmail(email: String) {
        sharedPreferenceManager.saveString(SharedPreference.EMAIL, email)
    }

    fun saveEmailBase64(email: String) {
        sharedPreferenceManager.saveString(SharedPreference.EMAIL_BASE64, Utilities.encodeBase64(email))
    }

    fun getEmail(): String? {
        return sharedPreferenceManager.getString(SharedPreference.EMAIL, "")
    }

    fun getBase64Email(): String? {
        return sharedPreferenceManager.getString(SharedPreference.EMAIL_BASE64)
    }

    fun getBase64UserId(): String? {
        return sharedPreferenceManager.getString(SharedPreference.USER_ID_BASE64)
    }

    fun deleteEmail() {
        return sharedPreferenceManager.remove(SharedPreference.EMAIL)
    }

    fun saveBase64Password(password: String) {
        sharedPreferenceManager.saveString(SharedPreference.PASSWORD_BASE64, password)
    }

    fun getBase64Password(): String? {
        return sharedPreferenceManager.getString(SharedPreference.PASSWORD_BASE64)
    }

    fun getPassword(): String? {
        return sharedPreferenceManager.getString(SharedPreference.PASSWORD_STR, "")
    }

    fun savePassword(password: String) {
        sharedPreferenceManager.saveString(SharedPreference.PASSWORD_STR, password)
    }

    fun saveUserInfo(userInfo: CurrentUserResponse) {
        //sharedPreferenceManager.putString(SharedPreference.EMAIL, userInfo.email)
        //sharedPreferenceManager.putInt(SharedPreference.USER_ID, userInfo.id)
        // etc ...
        sharedPreferenceManager.save()
    }

    fun getUserID(): String? {
        return sharedPreferenceManager.getString(SharedPreference.USER_ID, "")
    }
//
//    fun setCurrentUser(user: User?){
//        val json = Gson().toJson(user)
//        sharedPreferenceManager.saveString(SharedPreference.USER_OBJECT, json)
//    }
//
//    fun getCurrentUser(): String?{
//        return sharedPreferenceManager.getString(SharedPreference.USER_OBJECT, "")
//    }

    fun clearUserData() {
        sharedPreferenceManager.remove(SharedPreference.USER_OBJECT)
    }

    fun clearAllData() {
        sharedPreferenceManager.clear()
    }
}