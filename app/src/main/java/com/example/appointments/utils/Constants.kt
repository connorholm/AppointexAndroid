package com.example.appointments.utils

object Constants {

    class SharedPreference {
        companion object {
            const val LOGGED_IN = "logged_in"
            const val ACCESS_TOKEN = "access_token"
            const val TOKEN_EXPIRY_ON = "expiry_on"
            const val REMIND_ME_EXPIRY_ON = "remind_me_time"
            const val REFRESH_TOKEN = "refresh_access_token"
            const val TOKEN_TYPE = "token_type"
            const val USER_ID = "userID"
            const val EMAIL = "emailID"
            const val EMAIL_BASE64 = "base64_emailID"
            const val USER_ID_BASE64 = "base64_memberID"
            const val PASSWORD_BASE64 = "base64_password"
            const val PASSWORD_STR = "passwordSTR"
            const val USER_OBJECT = "user_object"
            const val CREATED_AT = "created_at"
            const val CURRENT_USER = "current_user"
            const val COMPANY = "company"
            const val FILTERS = "filters"
            const val FILTER_TYPE = "filter_type"
        }
    }

    class API {
        companion object {
            const val CLIENT_ID = "B-YGKEVmcf5QThg5v37KhBdW-dO_K8e9E_Ab5isp8DY"
            const val CLIENT_SECRET="tPyp7o2G7lNGJUdb2cBA0cwc4ifdnbII1VP8vvS9Kd4"
        }
    }

    class Remote {
        companion object {
            const val AUTH_PREFIX = "Bearer"
            const val CODE_ERROR = "Error"
            const val AUTHORIZATION_HEADER_KEY = "Authorization"
            const val CONTENT_TYPE_HEADER_KEY = "Content-Type"
            const val CONTENT_TYPE_HEADER_VALUE = "application/json"
        }
    }
}

