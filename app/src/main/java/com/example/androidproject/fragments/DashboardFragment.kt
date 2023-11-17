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
import com.example.androidproject.activities.LoginActivity
import com.example.androidproject.activities.MainActivity
import com.example.androidproject.activities.WalletActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@Serializable
data class Project(val name: String, val language: String)

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var walletList: ArrayList<Wallet>? = null
    private val walletDatabase by lazy { WalletDatabase.getDatabase(requireContext()).walletDao() }
    private var btcToUsd: Double? = null
    private var usdToArs: Double? = null

    private fun getData() {
        if (walletList == null || btcToUsd == null || usdToArs == null) {
            return
        }

        if (walletList!!.size == 0) {
            val fragmentID: TextView = requireView().findViewById(R.id.lblWalletTotal)
            fragmentID.text = getString(R.string.no_wallets)
        }

        if (walletList!!.size > 0 && btcToUsd != null && usdToArs !== null) {
            var totalUSD = 0.0
            Log.d("dash", "Values not null: " + totalUSD)

            for (wallet in walletList!!) {
                Log.d("dash", wallet.toString())
                if (wallet.walletCurrency == "USD") {
                    totalUSD += wallet.walletAmount.toDouble()
                }
                if (wallet.walletCurrency == "BTC") {
                    totalUSD += wallet.walletAmount.toDouble() * btcToUsd!!
                }
                if (wallet.walletCurrency == "ARS") {
                    totalUSD += wallet.walletAmount.toDouble() / usdToArs!!
                }
            }
            Log.d("dash", "Values not null: " + totalUSD)

            // Update UI
            activity?.runOnUiThread {
                val fragmentID: TextView = requireView().findViewById(R.id.lblWalletTotal)
                val formattedString = getString(R.string.wallet_total, totalUSD.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toString())
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
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
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
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}