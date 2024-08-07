package com.example.weatherforecast.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.widget.SearchView.VISIBLE
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import com.example.weatherforecast.R
import com.example.weatherforecast.api.ApiResponse
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.model.current.CurrentWeatherModel
import com.example.weatherforecast.util.UiUtil
import com.example.weatherforecast.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    lateinit var mainViewModel: MainViewModel

    lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainParentLayout.setBackgroundResource(R.drawable.gradient_background_day)
        mainViewModel = ViewModelProvider(this)[MainViewModel :: class.java]
        mainViewModel.getCurrentWeather("Kathmandu")

        mainViewModel.currentWeather.observe(this) {
            when (it) {
                is ApiResponse.ApiLoading -> {
                    binding.progressIndicator.visibility = VISIBLE
                }

                is ApiResponse.ApiError -> {
                    binding.progressIndicator.visibility = INVISIBLE
                    UiUtil.showToast(this, it.message ?: getString(R.string.something_went_wrong))
                }

                is ApiResponse.ApiSuccess -> {
                    setLocationData(it.data)
                }

            }
        }

        binding.searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                mainViewModel.getCurrentWeather(query?:"Kathmandu")
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
        
       
    }

    private fun setLocationData(data: CurrentWeatherModel?) {
       data?.let {
           if(it.current.is_day==0){
               binding.mainParentLayout.setBackgroundResource(R.drawable.gradient_background_night)
           }else{
               binding.mainParentLayout.setBackgroundResource(R.drawable.gradient_background_day)
           }

           binding.progressIndicator.visibility = INVISIBLE
           Glide.with(this).load(imageUrl(it.current.condition.icon)).into(binding.imageView)
           binding.dateTextview.text = it.location.localtime.split(" ")[0]
           binding.timeTextview.text = it.location.localtime.split(" ")[1]
           binding.placeTextview.text = it.location.name
           binding.conditionTextview.text = it.current.condition.text
           binding.uvTextview.text = it.current.uv.toString()
           binding.percipationTextview.text =getString(R.string.unit_precipitation, it.current.precip_mm.toString())

           binding.temperatureCelsiusTextview.text =
               getString(R.string.unit_degree_celsius, it.current.temp_c.toString())
           binding.humidityTextview.text = getString(R.string.unit_percent, it.current.humidity.toString())
           binding.windSpeedTextview.text = getString(R.string.unit_kph, it.current.wind_kph.toString())

       }
    }

    private fun imageUrl(url : String) : String = "https:"+url.replace("64x64","128x128")


}


