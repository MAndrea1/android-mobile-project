package com.example.androidproject.api
// Singletons.kt
import okhttp3.OkHttpClient

object HttpClientSingleton {
    val instance: OkHttpClient by lazy { OkHttpClient() }
}
