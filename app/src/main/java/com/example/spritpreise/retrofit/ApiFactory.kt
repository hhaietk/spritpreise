package com.example.spritpreise.retrofit

import com.example.spritpreise.AppConstants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiFactory {

    private const val BASE_URL = "https://creativecommons.tankerkoenig.de/json/"

    // Create Auth interceptor to add api_key query in front of all the requests
    private val authInterceptor = Interceptor { chain ->

        val newUrl = chain.request().url
            .newBuilder()
            //.addQueryParameter("api_key", AppConstants.API_KEY)
            // TODO: why dafuq didn't work
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)

    }

    private val stationClient = OkHttpClient().newBuilder()
        .addInterceptor(authInterceptor)
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