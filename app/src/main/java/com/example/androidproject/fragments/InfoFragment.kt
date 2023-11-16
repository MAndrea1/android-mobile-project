package com.example.androidproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.androidproject.R
import com.example.androidproject.api.CurrenciesAPI
import com.example.androidproject.api.HttpClientSingleton
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class PricesCur(val value_avg: Float, val value_sell: Float, val value_buy: Float)
@Serializable
data class ApiBlue(val oficial: PricesCur, val blue: PricesCur)


@Serializable
data class Pair(val last: String)
@Serializable
data class Cex(val data: List<Pair>)


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val okHttpClient = HttpClientSingleton.instance
        val currenciesAPI = CurrenciesAPI(okHttpClient, viewLifecycleOwner.lifecycleScope)

        currenciesAPI.getPrices("https://cex.io/api/tickers/BTC/USD") { result ->

            val obj = Json{ignoreUnknownKeys=true}.decodeFromString<Cex>(result)
            Log.d("info", obj.toString())
            Log.d("info", "btc: " + obj.data[0].last)

            activity?.runOnUiThread {
                Log.d("currenciesAPI", "from infoFragment ")
                val lblOutput: TextView = requireView().findViewById(R.id.lblAmountBTC)
                lblOutput.text = "${obj.data[0].last} USD"
            }
        }

        currenciesAPI.getPrices("https://api.bluelytics.com.ar/v2/latest") { result ->

            val obj = Json{ignoreUnknownKeys=true}.decodeFromString<ApiBlue>(result)
            Log.d("info", obj.toString())
            Log.d("info", "oficial: " + obj.oficial.value_avg.toString())
            Log.d("info", "blue: " + obj.blue.value_avg.toString())

            activity?.runOnUiThread {
                Log.d("currenciesAPI", "from infoFragment ")
                val lblOutputOf: TextView = requireView().findViewById(R.id.lblAmountOficial)
                lblOutputOf.text = "${obj.oficial.value_avg.toString()} ARS"
                val lblOutputBl: TextView = requireView().findViewById(R.id.lblAmountBlue)
                lblOutputBl.text = "${obj.blue.value_avg.toString()} ARS"
            }
        }

//        lifecycleScope.launch {
//            try {
//                val result = currenciesAPI.getPrices()
//                Log.d("InfoFrag", result)
//                // Process the result on the main thread, for example, update UI
//                activity?.runOnUiThread {
//                    // Your UI update code here
//                    // Log.d("currenciesAPI", "from InfoFragment $result")
//                }
//            } catch (e: Exception) {
//                // Handle the exception, if needed
//                e.printStackTrace()
//            }
//        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


