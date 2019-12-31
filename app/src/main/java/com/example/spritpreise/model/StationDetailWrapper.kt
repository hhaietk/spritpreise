package com.example.spritpreise.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StationDetailWrapper(

    @Json(name = "ok")
    val ok : Boolean,
    @Json(name = "license")
    val license: String,
    @Json(name = "data")
    val data : String,
    @Json(name = "status")
    val status : String,
    @Json(name = "station")
    val station : StationDetail
)