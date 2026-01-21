package com.example.gardeningcsisapp.ui.plant

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gardeningcsisapp.R
import com.example.gardeningcsisapp.domain.model.PlantAdapter
import com.example.gardeningcsisapp.domain.model.PlantsSearch
import com.google.android.material.bottomnavigation.BottomNavigationView

class PlantAddingActivity : AppCompatActivity() {
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_plantadding)
        loadPlants()

        backBtn = findViewById<Button>(R.id.btnBack)

        backBtn.setOnClickListener ( View.OnClickListener { view ->
            finish()
        })
    }

    fun loadPlants(){
        var result = ArrayList<PlantsSearch>()

        val queue = Volley.newRequestQueue(this)
        val url = "https://perenual.com/api/v2/species-list?page=1&key=sk-w3Ap696f910ccf45c14440"


        val clicked =
            JsonObjectRequest(Request.Method.GET,
                url,
                null,
                {
                        data ->

                        val dataArray = data.getJSONArray("data")
                        var count = dataArray.length()
                        Log.e("MyApp", "$count")

                        for(i in 0 until count){
                            val plantObj = dataArray.getJSONObject(i)

                            val defaultImage = plantObj.optJSONObject("default_image")
                            val imageUrl = defaultImage?.optString("small_url") ?: ""

                            val id = plantObj.optString("id")
                            val plant_species = plantObj.optString("scientific_name").substring(2, plantObj.optString("scientific_name").length - 2)
                            val plant_name = plantObj.optString("common_name")
                            val imgURL = imageUrl


                            Log.e("MyApp","Plant Data: $id, $plant_species, $plant_name, $imgURL")
                            result.add(PlantsSearch("$id","$plant_name","$plant_species","$imgURL"))
                        }

                        val recyclerView: RecyclerView = findViewById(R.id.viewAllPlants)
                        recyclerView.layoutManager = LinearLayoutManager(this)

                        val plantAdapter = PlantAdapter(result)
                        recyclerView.adapter = plantAdapter

                },
                { Log.e("MyApp", "Did not receive network data"); })

        clicked.setShouldCache(false)
        queue.add(clicked);
    }

}