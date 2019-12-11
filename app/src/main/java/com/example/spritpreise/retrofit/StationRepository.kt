package com.example.spritpreise.retrofit

import com.example.spritpreise.model.Station

class StationRepository(private val api : StationApi) : BaseRepository() {

    suspend fun getStations(lat : Float, lng : Float, rad : Float, type : String, sort : String) : MutableList<Station>? {

        val stationResponse = safeApiCall(
            call = { api.getNearbyStations(lat, lng, rad, type, sort).await() },
            errorMessage = "error fetching nearby stations"
        )


        return stationResponse?.stations?.toMutableList()
    }
}