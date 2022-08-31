package com.example.maptemp

import com.google.gson.annotations.SerializedName

data class Main(
    val temp: Float
)

data class Weather(
    @SerializedName("main")
    val main: Main
)