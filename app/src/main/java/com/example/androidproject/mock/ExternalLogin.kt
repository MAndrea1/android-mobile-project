package com.example.androidproject.mock

import android.util.Log

class ExternalLogin {

    interface LoginCallback {
        fun onResult(isValid: Boolean)
    }

    fun validateLogin(username: String, password: String, callback: LoginCallback) {
        Log.d("login", "validateLogin $username $password")
        Log.d("login", (username == "admin").toString())
        Log.d("login", (password == "123456").toString())
        val isValidUser = username == "admin" && password == "123456"
        callback.onResult(isValidUser)
    }
}