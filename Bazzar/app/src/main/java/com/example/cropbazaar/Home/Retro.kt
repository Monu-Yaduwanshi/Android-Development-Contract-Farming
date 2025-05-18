package com.example.cropbazaar.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherNewsViewModel : ViewModel() {
    private val _weatherNews = MutableStateFlow<List<WeatherNews>>(emptyList())
    val weatherNews: StateFlow<List<WeatherNews>> = _weatherNews

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.example.com/") // Replace with the correct base URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(WeatherApiService::class.java)

    init {
        fetchWeatherNews()
    }

    private fun fetchWeatherNews() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _weatherNews.value = apiService.getWeatherNews()
            } catch (e: Exception) {
                e.printStackTrace() // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
}
