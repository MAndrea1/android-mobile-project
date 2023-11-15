package com.example.androidproject.api

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class CurrenciesAPI(private val client: OkHttpClient, private val lifecycleScope: CoroutineScope) {

    // This doesn't work, because it blocks the main thread.
    fun getPrices1(): String {
        val url = "https://cex.io/api/tickers/BTC/USD"

        val request = Request.Builder()
            .url(url)
            .build()

        return try {
            val response = client.newCall(request).execute()
            response.body?.string() ?: "No response body"
        } catch (e: Exception) {
            e.printStackTrace()
            "Error: ${e.message}"
        }
    }

    private suspend fun fetchPrices(url: String): String = suspendCoroutine { continuation ->
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string() ?: "No response body"

                // Deliver the result to the calling code
                continuation.resume(responseBody)
            } catch (e: Exception) {
                // Handle the exception or pass it to the calling code
                continuation.resume("Error: ${e.message}")
            }
        }
    }

    suspend fun getUrlPrices(): String {
        val result1 = fetchPrices("https://cex.io/api/tickers/BTC/USD")
        val result2 = fetchPrices("https://api.bluelytics.com.ar/v2/latest")

        val processedData = processCurrencies(result1, result2)

        return processedData
    }

    private fun processCurrencies(result1: String, result2: String): String {





        return "Processed data: $result1 $result2"
    }

    fun getPrices(string: String, onResult: (String) -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(string)
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string() ?: "No response body"

                onResult(responseBody)
            } catch (e: Exception) {
                onResult("Error: ${e.message}")
            }
        }
    }

}
