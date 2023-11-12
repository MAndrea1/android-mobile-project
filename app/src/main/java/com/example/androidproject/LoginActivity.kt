package com.example.androidproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    companion object {
         val LOGIN_DATA = "SharedPrefLogin"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginData = getSharedPreferences(LOGIN_DATA, Context.MODE_PRIVATE)
        val usernameSP = loginData.getString("username", "")
        val passwordSP = loginData.getString("password", "")

        Log.d("sanity", "$usernameSP $passwordSP")

        if (!usernameSP.isNullOrBlank() && !passwordSP.isNullOrBlank()) {
            autoLogin(usernameSP, passwordSP)
        }
    }

    private fun autoLogin(username: String, password: String) {
        val externalLogin = ExternalLogin()
        externalLogin.validateLogin(username, password, object : ExternalLogin.LoginCallback {
            override fun onResult(isValid: Boolean) {
                if (isValid) {
                    accessWallet()
                }
            }
        })
    }

    fun loginClicked(view: View) {
        var usernameField = findViewById<EditText>(R.id.etUsername)
        var username = usernameField.text.toString()
        var passwordField = findViewById<EditText>(R.id.etPassword)
        var password = passwordField.text.toString()

        Log.d("login", username + password)

        val externalLogin = ExternalLogin()
        externalLogin.validateLogin(username, password, object : ExternalLogin.LoginCallback {
            override fun onResult(isValid: Boolean) {
                if (isValid) {
                    val cred = getSharedPreferences(LOGIN_DATA, Context.MODE_PRIVATE)
                    val editor = cred.edit()
                    editor.putString("username", username)
                    editor.putString("password", password)
                    editor.apply()
                    accessWallet()
                } else {
                    val rootView = findViewById<View>(R.id.activity_login)
                    val snackbar = Snackbar.make(rootView, "Incorrect name or password", Snackbar.LENGTH_LONG)
                    snackbar.show()
                    // Log.d("login", "Incorrect name or password $username $password")
                }
            }
        })
    }

    fun accessWallet() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // - This closes the LoginActivity so the back button cannot return to it.
    }
}