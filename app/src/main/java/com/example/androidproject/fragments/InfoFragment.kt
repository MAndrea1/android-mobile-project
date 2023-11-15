package com.example.androidproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.androidproject.R
import com.example.androidproject.api.CurrenciesAPI
import com.example.androidproject.api.HttpClientSingleton
import kotlinx.coroutines.launch

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

            activity?.runOnUiThread {
                Log.d("currenciesAPI", "from infoFragment ")
            }
        }

        currenciesAPI.getPrices("https://api.bluelytics.com.ar/v2/latest") { result ->

            activity?.runOnUiThread {
                Log.d("currenciesAPI", "from infoFragment ")
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