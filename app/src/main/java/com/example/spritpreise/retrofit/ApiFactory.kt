package com.example.spritpreise.retrofit

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiFactory {

    private const val BASE_URL = "https://creativecommons.tankerkoenig.de/json/"
    private const val API_KEY = "0b563adf-11cd-3f08-a4f9-4b6d0297d32f"

    // Create Auth interceptor to add api_key query in front of all the requests
    private val authInterceptor = Interceptor { chain ->

        val newUrl = chain.request().url
            .newBuilder()
            .addQueryParameter("apikey", API_KEY)
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)

    }

    private val loggingInterceptor = HttpLoggingInterceptor()
        .apply { level = HttpLoggingInterceptor.Level.BODY }

    private val stationClient = OkHttpClient().newBuilder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private fun retrofit() : Retrofit = Retrofit.Builder()
        .client(stationClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    // add more apis here
    val stationApi : StationApi = retrofit().create(StationApi::class.java)
}