package com.example.spritpreise.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spritpreise.model.Station
import com.example.spritpreise.retrofit.ApiFactory
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class StationViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO

    private val scope = CoroutineScope(coroutineContext)

    val stationsLiveData = MutableLiveData<List<Station>>()

    fun fetchStations() {

        scope.launch {

            // Doing network call on IO Thread
            val response = ApiFactory.stationApi.getNearbyStations(52.521f, 13.440946f, 1.5f, "all", "dist")
                .await()
            val stations = response.stations

            // update the value on Main Thread
            withContext(Dispatchers.Main) {
                stationsLiveData.value = stations
            }
        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()

}