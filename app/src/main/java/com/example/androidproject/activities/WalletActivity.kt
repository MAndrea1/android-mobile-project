package com.example.androidproject.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import com.example.androidproject.R
import com.example.androidproject.room.Wallet
import com.example.androidproject.room.WalletDatabase
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class WalletActivity : AppCompatActivity() {

    private val walletDatabase by lazy { WalletDatabase.getDatabase(this).walletDao() }

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

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun saveWalletClicked(view: View) {

        var walletName = findViewById<EditText>(R.id.etWalletName).text.toString()
        var walletAmountText = findViewById<EditText>(R.id.etAmount).text.toString()
        var walletAmount = walletAmountText.toFloat()
        val spinner: Spinner = findViewById(R.id.spinnerCurrency)
        val selectedCurrency = spinner.selectedItem.toString()

        Log.d("wallet", "$walletName $walletAmount $selectedCurrency")

        if (!walletName.isNullOrBlank() && !walletAmountText.isNullOrBlank() && !selectedCurrency.isNullOrBlank()) {
            lifecycleScope.launch {
                walletDatabase.insert(Wallet(walletName = walletName, walletAmount = walletAmount, walletCurrency = selectedCurrency))
                setResult(Activity.RESULT_OK)
                // Finish the activity to return to the calling fragment
                finish()
            }
            checkValues()
        } else {
            val rootView = findViewById<View>(R.id.activity_wallet)
            val snackbar = Snackbar.make(rootView, "Data missing", Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }
    private fun checkValues() {
        lifecycleScope.launch {
            var allWallets = walletDatabase.getAllWallets()
//            Log.d("wallet", allWallets.toString())
        }
    }
}





