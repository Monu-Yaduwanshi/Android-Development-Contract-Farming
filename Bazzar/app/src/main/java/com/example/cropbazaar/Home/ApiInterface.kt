package com.example.cropbazaar.Home

import retrofit2.http.GET

// Data class for Weather News response
data class WeatherNews(
    val title: String,
    val description: String,
    val imageUrl: String
)

// Retrofit API interface
interface WeatherApiService {
    @GET("weathernews") // Replace with the correct endpoint
    suspend fun getWeatherNews(): List<WeatherNews>
}
