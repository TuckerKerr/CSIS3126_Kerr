package com.example.gardeningcsisapp.ui.home

import android.Manifest
import com.example.gardeningcsisapp.R
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gardeningcsisapp.domain.model.PlantsSearch
import com.example.gardeningcsisapp.ui.authentication.AuthRepository
import com.example.gardeningcsisapp.ui.plant.PlantUpdateRepository
import com.google.android.gms.location.LocationServices

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    //this will handle the logic so this will call to the repository file which handles the API Calls
    private val homeRepository = HomeRepository(application)
    private val authRepository = AuthRepository(application)
    val plants: LiveData<List<PlantsSearch>> = homeRepository.plants

    val messages: LiveData<String> = homeRepository.toastMessage
    private val _weatherIcon = MutableLiveData<Int>()

    val weatherString: LiveData<Int> = _weatherIcon
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    private val _location = MutableLiveData<Pair<Double, Double>>()
    val location: LiveData<Pair<Double, Double>> = _location
    private val _openPlantScreen = MutableLiveData<Unit>()
    val openPlantScreen: LiveData<Unit> = _openPlantScreen


    fun addPlant() {
        _openPlantScreen.value = Unit
        //function with call repo, update live data
    }

    fun saveToken(token: String) {
        authRepository.saveToken(token)
    }

    fun getToken(): String? {
        return authRepository.getToken()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPlantTime() {
        val token = authRepository.getToken()
        homeRepository.checkPlantsDates(token)
    }

    fun plantWatered(plant_id: String) {
        val token = authRepository.getToken()
        homeRepository.plantWatered(token, plant_id)
    }

    fun clearMessage() {
        homeRepository.clearMessage()
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun fetchLocation() {
        val token = authRepository.getToken()
        fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
            loc?.let {
                _location.value = Pair(it.latitude, it.longitude)

                homeRepository.getWeather(it.latitude, it.longitude) { weatherType ->
                    Log.e("myapp", "weather in VM: ${weatherType?.lowercase()}")

                    val mappedWeather = when (weatherType?.lowercase()) {
                        "clouds" -> R.drawable.ic_cloud
                        "mist" -> R.drawable.ic_cloud
                        "rain" -> R.drawable.ic_rain
                        "clear" -> R.drawable.ic_light
                        else -> R.drawable.ic_light
                    }

                    if (weatherType?.lowercase().equals("rain")) {
                        homeRepository.rainyWeatherWatering(token)
                    }

                    _weatherIcon.postValue(mappedWeather)
                }
            }
        }
    }
}