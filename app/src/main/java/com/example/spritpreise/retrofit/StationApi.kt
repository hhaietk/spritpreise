package com.example.spritpreise.retrofit

import com.example.spritpreise.model.Station
//import com.example.spritpreise.model.StationResponse
import com.example.spritpreise.model.StationWrapper
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StationApi {

    @GET("list.php")
    fun getNearbyStations(
        @Query("apikey") key : String,
        @Query("lat") lat : Float,
        @Query("lng") lng : Float,
        @Query("rad") rad : Float,
        @Query("type") type : String,
        @Query("sort") sort : String) : Deferred<StationWrapper>

}