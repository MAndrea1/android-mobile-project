package com.example.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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
                    accessWallet()
                } else {
                    val rootView = findViewById<View>(R.id.activity_login)
                    val snackbar = Snackbar.make(rootView, "Incorrect name or password", Snackbar.LENGTH_LONG)
                    snackbar.show()
                    Log.d("login", "Incorrect name or password $username $password")
                }
            }
        })
    }

    fun accessWallet() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}