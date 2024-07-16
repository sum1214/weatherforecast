package com.example.weatherforecast

import android.app.Application
import dagger.Component
import dagger.hilt.android.HiltAndroidApp
import com.example.weatherforecast.di.module.NetworkModule
import com.example.weatherforecast.view.MainActivity
import javax.inject.Singleton

@HiltAndroidApp
class WeatherApplication : Application() {
}