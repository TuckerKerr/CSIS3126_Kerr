package com.example.gardeningcsisapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.gardeningcsisapp.R
import com.example.gardeningcsisapp.ui.authentication.AuthRepository
import com.example.gardeningcsisapp.ui.plant.PlantViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.getValue

class MenuActivity : AppCompatActivity(){

    private val viewModel: PlantViewModel by viewModels()

    //This will be the main menu which holds all the plants that you have currently
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        val token = intent.getStringExtra("token").toString()
        viewModel.saveToken(token)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)
    }
}