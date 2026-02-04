package com.example.gardeningcsisapp.ui.plant

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gardeningcsisapp.domain.model.PlantsSearch
import com.example.gardeningcsisapp.ui.authentication.AuthRepository

class PlantViewModel(application: Application) : AndroidViewModel(application) {
   //Learn about View Models and how they bridge between fragments and repos (functions)
    private val repository = PlantRepository(application)
    private val authRepository = AuthRepository(application)

    //val plants: LiveData<List<PlantsSearch>> = repository.loadAllPlant()

    fun getToken(): String?{
        return authRepository.getToken()
    }

    fun saveToken(token: String){
        return authRepository.saveToken(token)
    }



}