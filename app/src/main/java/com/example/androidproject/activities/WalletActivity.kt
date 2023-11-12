package com.example.androidproject.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.example.androidproject.R
import com.example.androidproject.mock.ExternalLogin
import com.google.android.material.snackbar.Snackbar

class WalletActivity : AppCompatActivity() {

    companion object {
        val WALLET_SAVE = "SharedPrefWallets"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        val spinner: Spinner = findViewById(R.id.spinnerCurrency)
        val currencies = arrayOf("ARS", "USD", "BTC") // Sample currencies

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

    }

    fun saveWalletClicked(view: View) {

        var walletName = findViewById<EditText>(R.id.etWalletName).text.toString()
        var walletAmount = findViewById<EditText>(R.id.etAmount).text.toString()
        val spinner: Spinner = findViewById(R.id.spinnerCurrency)
        val selectedCurrency = spinner.selectedItem.toString()

        Log.d("wallet", "$walletName $walletAmount $selectedCurrency")

    }
}










//        val saveSharedPref = getSharedPreferences(WalletActivity.WALLET_SAVE, Context.MODE_PRIVATE)
//        val editor = saveSharedPref.edit()
//        editor.putString("walletName", walletName)
//        editor.putString("walletAmount", walletAmount)
//        editor.apply()
