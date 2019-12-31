package com.example.spritpreise.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenTime(
    @Json(name = "text")
    val day : String,
    @Json(name = "start")
    val start : String,
    @Json(name = "end")
    val end : String
)
