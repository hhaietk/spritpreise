package com.example.spritpreise.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StationDetail(

    @Json(name = "openingTimes")
    val openingTimes: List<OpenTime>
)
