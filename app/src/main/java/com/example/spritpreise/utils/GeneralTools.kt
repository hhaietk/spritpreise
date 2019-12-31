package com.example.spritpreise.utils

import java.util.*

object GeneralTools {

    fun formatStreetName(street : String) : String {
        // 1.Case: MARGARETE-SOMMER-STR. 2. We do split by "-"
        var streetName = street.split("-")
        if (streetName.size == 1) {
            // 2.Case: Blaubeurer str. We do split by " "
            streetName = street.split(" ")
            if (streetName.size == 1) {
                return street.toLowerCase().capitalize()
            }
            // 2.Case
            val joiner2 = StringJoiner(" ")
            streetName.forEach { word -> joiner2.add(word.toLowerCase().capitalize()) }
            return joiner2.toString()
        }
        // 1.Case
        val joiner1 = StringJoiner("-")
        streetName.forEach { word -> joiner1.add(word.toLowerCase().capitalize()) }
        return joiner1.toString()
    }
}