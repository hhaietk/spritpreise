package com.example.spritpreise.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spritpreise.model.StationDetail
import com.example.spritpreise.retrofit.ApiFactory
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class DetailViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO

    private val scope = CoroutineScope(coroutineContext)

    val stationDetailLiveData = MutableLiveData<StationDetail>()

    fun fetchStationDetail(id: String) {

        scope.launch {

            // Doing network call on IO Thread
            val response = ApiFactory.stationApi.getStationDetail(UUID.fromString(id)).await()
            val station = response.station

            // update the value on Main Thread
            withContext(Dispatchers.Main) {
                stationDetailLiveData.value = station
            }
        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()
}