package com.example.androidproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.androidproject.R
import com.example.androidproject.api.CurrenciesAPI
import com.example.androidproject.api.HttpClientSingleton
import com.example.androidproject.room.Wallet
import com.example.androidproject.room.WalletDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.math.RoundingMode
import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager
import com.example.androidproject.activities.LoginActivity
import com.example.androidproject.activities.SettingsActivity

@Serializable
data class Project(val name: String, val language: String)

class DashboardFragment : Fragment() {
    private var walletList: ArrayList<Wallet>? = null
    private val walletDatabase by lazy { WalletDatabase.getDatabase(requireContext()).walletDao() }
    private var btcToUsd: Double? = null
    private var usdToArs: Double? = null
    private var usdToArsBlue: Double? = null
    private var walletCurrency: String = "USD"

    private fun getData() {
        if (walletList == null || btcToUsd == null || usdToArs == null) {
            return
        }

        if (walletList!!.size == 0) {
            val fragmentID: TextView = requireView().findViewById(R.id.lblWalletTotal)
            fragmentID.text = getString(R.string.no_wallets)
        }

        if (walletList!!.size > 0 && btcToUsd != null && usdToArs !== null) {
            var totalAmount = 0.0
            var currency = walletCurrency
            Log.d("dash", "Values not null: " + totalAmount)

            Log.d("settingsFeature", "walletCurrency " + walletCurrency)

            when(walletCurrency) {
                "USD" -> {

                    for (wallet in walletList!!) {
                        Log.d("dash", wallet.toString())
                        if (wallet.walletCurrency == "USD") {
                            totalAmount += wallet.walletAmount.toDouble()
                        }
                        if (wallet.walletCurrency == "BTC") {
                            totalAmount += wallet.walletAmount.toDouble() * btcToUsd!!
                        }
                        if (wallet.walletCurrency == "ARS") {
                            totalAmount += wallet.walletAmount.toDouble() / usdToArs!!
                        }
                    }
                }
                "Blue" -> {
                    currency = "ARS $walletCurrency"

                    for (wallet in walletList!!) {
                        Log.d("dash", wallet.toString())
                        if (wallet.walletCurrency == "USD") {
                            totalAmount += wallet.walletAmount.toDouble() * usdToArsBlue!!
                        }
                        if (wallet.walletCurrency == "BTC") {
                            totalAmount += wallet.walletAmount.toDouble() * btcToUsd!! * usdToArsBlue!!
                        }
                        if (wallet.walletCurrency == "ARS") {
                            totalAmount += wallet.walletAmount.toDouble()
                        }
                    }
                }
                "Official" -> {
                    currency = "ARS " + getString(R.string.lbl_official)

                    for (wallet in walletList!!) {
                        Log.d("dash", wallet.toString())
                        if (wallet.walletCurrency == "USD") {
                            totalAmount += wallet.walletAmount.toDouble() * usdToArs!!
                        }
                        if (wallet.walletCurrency == "BTC") {
                            totalAmount += wallet.walletAmount.toDouble() * btcToUsd!! * usdToArs!!
                        }
                        if (wallet.walletCurrency == "ARS") {
                            totalAmount += wallet.walletAmount.toDouble()
                        }
                    }
                }

            }

            Log.d("dash", "Values not null: $totalAmount")

            // Update UI
            activity?.runOnUiThread {
                val fragmentID: TextView = requireView().findViewById(R.id.lblWalletTotal)
                val formattedString = getString(R.string.wallet_total, totalAmount.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toString(), currency)
                fragmentID.text = formattedString
            }
        }
    }

    private suspend fun getWalletsFromDB() {
        withContext(Dispatchers.IO) {
            walletList = ArrayList(walletDatabase.getAllWallets())
            getData()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val okHttpClient = HttpClientSingleton.instance
        val currenciesAPI = CurrenciesAPI(okHttpClient, viewLifecycleOwner.lifecycleScope)

        currenciesAPI.getPrices("https://cex.io/api/tickers/BTC/USD") { result ->
            val obj = Json{ignoreUnknownKeys=true}.decodeFromString<Cex>(result)
            btcToUsd = obj.data[0].last.toDouble()
            getData()
        }

        currenciesAPI.getPrices("https://api.bluelytics.com.ar/v2/latest") { result ->

            val obj = Json{ignoreUnknownKeys=true}.decodeFromString<ApiBlue>(result)
            usdToArs = obj.oficial.value_avg.toDouble()
            usdToArsBlue = obj.blue.value_avg.toDouble()
            getData()
        }

        lifecycleScope.launch {
            getWalletsFromDB()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                Log.d("dash", "button pressed")
                val cred = requireActivity().getSharedPreferences(LOGIN_DATA, Context.MODE_PRIVATE)
                val editor = cred.edit()
                editor.putString("username", "")
                editor.putString("password", "")
                editor.apply()

                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
                true
            }
            R.id.action_settings -> {
                Log.d("settingsFeature", "in action settings")
                val intent = Intent(requireActivity(), SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        walletCurrency = sharedPref.getString("currency", "USD") ?: "USD"

        Log.d("Dashboard", "on dashboard onResume")
        lifecycleScope.launch(Dispatchers.Main) {
            walletList = ArrayList(walletDatabase.getAllWallets())

            getData()
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        val LOGIN_DATA = "SharedPrefLogin"
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}