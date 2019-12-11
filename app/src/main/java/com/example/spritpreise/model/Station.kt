package com.example.spritpreise.model

data class Station(
    val name : String,
    val brand : String,
    val street : String,
    val lat : Float,
    val lng : Float,
    val dist : Float,
    val diesel : Float,
    val e5 : Float,
    val e10 : Float,
    val isOpen : Boolean,
    val houseNumber : String,
    val postCode : String
)

data class StationResponse(val stations: List<Station>)