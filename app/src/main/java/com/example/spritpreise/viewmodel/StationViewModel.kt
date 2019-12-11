package com.example.spritpreise.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spritpreise.model.Station
import com.example.spritpreise.retrofit.ApiFactory
import com.example.spritpreise.retrofit.StationRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.math.log

class StationViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository: StationRepository = StationRepository(ApiFactory.stationApi)

    val stationsLiveData = MutableLiveData<MutableList<Station>>()

    fun fetchStations() {
        scope.launch {
            val stations = repository.getStations(52.521f, 13.440946f, 1.5f, "all", "dist")
            stationsLiveData.postValue(stations)
        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()

}