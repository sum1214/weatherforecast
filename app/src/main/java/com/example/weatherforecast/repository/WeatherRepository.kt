package com.example.weatherforecast.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.api.ApiResponse
import com.example.weatherforecast.api.ApiService
import com.example.weatherforecast.model.current.CurrentWeatherModel
import javax.inject.Inject

class WeatherRepository @Inject constructor (private val apiService: ApiService) {

    private val _currentWeatherLiveData = MutableLiveData<ApiResponse<CurrentWeatherModel>>()
    val currentWeather : LiveData<ApiResponse<CurrentWeatherModel>>
        get() {
           return _currentWeatherLiveData
        }

    suspend fun getCurrentWeather(query: String) {
        _currentWeatherLiveData.postValue(ApiResponse.ApiLoading())
        try{
            val result  = apiService.getCurrent(query);
            if(result.body()!=null){
                _currentWeatherLiveData.postValue(ApiResponse.ApiSuccess(result.body()))
            }else{
                throw Exception("Something went wrong")
            }
        }catch (e : Exception){
            _currentWeatherLiveData.postValue(ApiResponse.ApiError(e.localizedMessage))
        }
    }
}