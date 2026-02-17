package com.example.gardeningcsisapp.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gardeningcsisapp.domain.model.PlantsSearch
import com.example.gardeningcsisapp.ui.authentication.AuthRepository
import com.example.gardeningcsisapp.ui.plant.PlantUpdateRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    //this will handle the logic so this will call to the repository file which handles the API Calls
    private val homeRepository = HomeRepository(application)
    private val authRepository = AuthRepository(application)
    val plants: LiveData<List<PlantsSearch>> = homeRepository.plants


    private val _openPlantScreen = MutableLiveData<Unit>()
    val openPlantScreen: LiveData<Unit> = _openPlantScreen

    fun addPlant(){
        _openPlantScreen.value = Unit
        //function with call repo, update live data
    }

    fun saveToken(token: String){
        authRepository.saveToken(token)
    }

    fun getToken(): String?{
        return authRepository.getToken()
    }

    fun getPlantTime(){
        val token = authRepository.getToken()
        homeRepository.checkPlantsDates(token)
    }

    fun plantWatered(plant_id: String){
        val token = authRepository.getToken()
        homeRepository.plantWatered(token, plant_id)
    }

}