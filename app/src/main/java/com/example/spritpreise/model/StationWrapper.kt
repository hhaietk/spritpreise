package com.example.spritpreise.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class StationWrapper(

    @Json(name = "ok")
    val ok : Boolean,
    @Json(name = "license")
    val license: String,
    @Json(name = "data")
    val data : String,
    @Json(name = "status")
    val status : String,
    @Json(name = "stations")
    val stations : MutableList<Station>,
    @Json(name = "message")
    val message : String
)
