package com.example.androidproject

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun loginClicked(view: View) {
        var usernameField = findViewById<EditText>(R.id.etUsername)
        var username = usernameField.text.toString()
        var passwordField = findViewById<EditText>(R.id.etPassword)
        var password = passwordField.text.toString()

        Log.d("login", username + password)
        Toast.makeText(this, "Incorrect name or password", Toast.LENGTH_LONG).show()

    }
}