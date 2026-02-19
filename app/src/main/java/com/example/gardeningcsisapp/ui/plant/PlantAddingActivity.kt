package com.example.gardeningcsisapp.ui.plant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gardeningcsisapp.R
import com.example.gardeningcsisapp.domain.model.PlantAdapter
import com.example.gardeningcsisapp.domain.model.PlantsSearch
import com.example.gardeningcsisapp.ui.MenuActivity
import com.example.gardeningcsisapp.ui.authentication.AuthRepository
import org.json.JSONObject

class PlantAddingActivity : AppCompatActivity() {
    private lateinit var backBtn: Button
    private lateinit var plantSearch: SearchView
    private lateinit var adapter: PlantAdapter
    private lateinit var recyclerView: RecyclerView

    // private val plantViewModel = PlantViewModel(application)

    private val plantList = mutableListOf<PlantsSearch>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_plantadding)
        readJSONFromFile()
        loadPlants()



        recyclerView = findViewById(R.id.viewAllPlants)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = PlantAdapter(plantList){ item ->
            clickedPlants(item)
        }

        recyclerView.adapter = adapter

        backBtn = findViewById<Button>(R.id.btnBack)
        backBtn.setOnClickListener ( View.OnClickListener { view ->
            finish()
        })

        plantSearch = findViewById<SearchView>(R.id.searchPlant)
        plantSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(search: String?): Boolean {
                searchPlants(search)
                return true
            }

            override fun onQueryTextSubmit(search: String?): Boolean {
                searchPlants(search)
                return true
            }
        })

    }

    fun readJSONFromFile():String? {
        var json: String? = null
        try{
            val inputStream = assets.open("Keys.json")
            json = inputStream.bufferedReader().use { it.readText()}
        }catch (ex: Exception){
            ex.printStackTrace()
            return null
        }
        //Log.e("myapp", json)
        return json
    }


    fun clickedPlants(plant: PlantsSearch){
        //either intent to another page for adding the plant or do it in this and just change the view
        val intent = Intent(this, PlantDataActivity::class.java)
        intent.putExtra("plant_species", plant.plant_species)
        startActivity(intent)
        Log.e("myapp", "Plants ID: ${plant.plant_species} & ${plant.id}")
    }

    fun searchPlants(search: String?){
        var result = ArrayList<PlantsSearch>()
        val apiKeys = readJSONFromFile()
        val token = JSONObject(apiKeys).getString("trefleAPIKey")
        val queue = Volley.newRequestQueue(this)


        val clicked =
            JsonObjectRequest(Request.Method.GET,
                "https://trefle.io/api/v1/plants/search?token=$token&q=$search&filter_not[common_name]=null",
                null,
                {
                        data ->

                    val dataArray = data.getJSONArray("data")
                    var count = dataArray.length()
                    Log.e("MyApp", "$count")

                    for(i in 0 until count){
                        val plantObj = dataArray.getJSONObject(i)

                        val id = plantObj.optString("id")
                        val plant_species = plantObj.optString("scientific_name")
                        val plant_name = plantObj.optString("common_name")
                        val imgURL = plantObj.optString("image_url")


                        Log.e("MyApp","Plant Data: $id, $plant_species, $plant_name, $imgURL")
                        result.add(PlantsSearch("$id","$plant_species","$plant_name","$imgURL"))
                    }

                    val recyclerView: RecyclerView = findViewById(R.id.viewAllPlants)
                    recyclerView.layoutManager = LinearLayoutManager(this)

                    val plantAdapter = PlantAdapter(
                        result, {plant -> clickedPlants(plant)})
                    recyclerView.adapter = plantAdapter

                },
                { Log.e("MyApp", "Did not receive network data"); })

        clicked.setShouldCache(false)
        queue.add(clicked);
    }

    fun loadPlants(){
        var result = ArrayList<PlantsSearch>()
        val apiKeys = readJSONFromFile()
        val token = JSONObject(apiKeys).getString("trefleAPIKey")

        val queue = Volley.newRequestQueue(this)
        val url = "https://trefle.io/api/v1/plants?token=$token"


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

                            val id = plantObj.optString("id")
                            val plant_species = plantObj.optString("scientific_name")
                            val plant_name = plantObj.optString("common_name")
                            val imgURL = plantObj.optString("image_url")


                            Log.e("MyApp","Plant Data: $id, $plant_species, $plant_name, $imgURL")
                            result.add(PlantsSearch("$id","$plant_name","$plant_species","$imgURL"))
                        }

                        val recyclerView: RecyclerView = findViewById(R.id.viewAllPlants)
                        recyclerView.layoutManager = LinearLayoutManager(this)

                        val plantAdapter = PlantAdapter(
                            result, {plant -> clickedPlants(plant)})
                        recyclerView.adapter = plantAdapter

                },
                { Log.e("MyApp", "Did not receive network data"); })

        clicked.setShouldCache(false)
        queue.add(clicked);
    }
}