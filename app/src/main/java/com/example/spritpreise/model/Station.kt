package com.example.spritpreise.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Station(

    @Json(name = "name")
    val name : String,
    @Json(name = "brand")
    val brand : String,
    @Json(name = "street")
    val street : String,
    @Json(name = "lat")
    val lat : Float,
    @Json(name = "lng")
    val lng : Float,
    @Json(name = "dist")
    val dist : Float,
    @Json(name = "diesel")
    val diesel : Float,
    @Json(name = "e5")
    val e5 : Float,
    @Json(name = "e10")
    val e10 : Float,
    @Json(name = "isOpen")
    val isOpen : Boolean,
    @Json(name = "houseNumber")
    val houseNumber : String,
    @Json(name = "postCode")
    val postCode : String
)