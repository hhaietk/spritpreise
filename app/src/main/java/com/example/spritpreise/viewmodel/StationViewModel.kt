package com.example.spritpreise.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spritpreise.AppConstants
import com.example.spritpreise.model.Station
import com.example.spritpreise.retrofit.ApiFactory
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class StationViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO

    private val scope = CoroutineScope(coroutineContext)

    val stationsLiveData = MutableLiveData<MutableList<Station>>()

    fun fetchStations() {
        scope.launch {

            withContext(Dispatchers.Main) {

                val response = ApiFactory.stationApi.getNearbyStations(AppConstants.API_KEY,52.521f, 13.440946f, 1.5f, "all", "dist")
                    .await()
                val stations = response.stations
                stationsLiveData.value = stations

            }
        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()

}