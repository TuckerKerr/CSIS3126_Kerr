package com.example.gardeningcsisapp.domain.model

data class Plants (
    val id: String,
    val plant_name: String,
    val plant_species: String,
    val plant_age: String,
    val plant_watering: String,
    val plant_photo: String,
    val plant_sunlight: String,
    val plant_flowering_season: String,
    val imgURL: String,
    val plant_description: String
    //Maybe more things? Find this out as I go more into it and get user testing
)
