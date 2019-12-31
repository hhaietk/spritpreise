package com.example.spritpreise.retrofit

import com.example.spritpreise.model.Station
import com.example.spritpreise.model.StationDetailWrapper
//import com.example.spritpreise.model.StationResponse
import com.example.spritpreise.model.StationWrapper
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface IStationApi {

    @GET("list.php")
    fun getNearbyStations(
        @Query("lat") lat : Float,
        @Query("lng") lng : Float,
        @Query("rad") rad : Float,
        @Query("type") type : String,
        @Query("sort") sort : String) : Deferred<StationWrapper>


    @GET("detail.php")
    fun getStationDetail(@Query("id") id: UUID) : Deferred<StationDetailWrapper>
}