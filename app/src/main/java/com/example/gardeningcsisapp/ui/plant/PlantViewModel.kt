package com.example.gardeningcsisapp.ui.plant

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.gardeningcsisapp.domain.model.PlantsSearch

class PlantViewModel(application: Application) : AndroidViewModel(application) {
   //Learn about View Models and how they bridge between fragments and repos (functions)
    private val repository = PlantRepository(application)

    val plants: LiveData<List<PlantsSearch>> = repository.loadAllPlant()
}