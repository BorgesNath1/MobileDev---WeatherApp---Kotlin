package com.example.weatherapp.model

import android.graphics.Bitmap

data class Weather(
    val date: String,
    val desc: String,
    val temp: Double,
    val imgUrl: String,
    val bitmap: Bitmap? = null
)
{   companion object{
    val LOADING = Weather(date = "Loading",
        desc = "Loading",
        temp = -1.0,
        imgUrl = "Loading",
        bitmap = null
        )
    }
}