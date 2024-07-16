package com.example.weatherforecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.weatherforecast.api.ApiResponse
import com.example.weatherforecast.model.current.CurrentWeatherModel
import com.example.weatherforecast.repository.WeatherRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :ViewModel() {

     fun getCurrentWeather( query : String){
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getCurrentWeather(query)
        }
    }

    val currentWeather : LiveData<ApiResponse<CurrentWeatherModel>>
        get() {
           return weatherRepository.currentWeather
        }
}