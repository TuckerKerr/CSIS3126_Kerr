package com.example.gardeningcsisapp.ui.home

import androidx.lifecycle.ViewModel
import com.example.gardeningcsisapp.ui.plant.PlantUpdateRepository

class HomeViewModel() : ViewModel() {
    //this will handle the logic so this will call to the repository file which handles the API Calls
    private val repository = PlantUpdateRepository()

}