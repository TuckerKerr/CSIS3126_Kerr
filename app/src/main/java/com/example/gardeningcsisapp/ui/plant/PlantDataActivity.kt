package com.example.gardeningcsisapp.ui.plant

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gardeningcsisapp.R
import com.example.gardeningcsisapp.domain.model.PlantAdapter
import com.example.gardeningcsisapp.domain.model.Plants
import com.squareup.picasso.Picasso
import org.json.JSONObject
import org.w3c.dom.Text
import kotlin.String

class PlantDataActivity: AppCompatActivity() {

    private lateinit var plantImg: ImageView
    private lateinit var plantName: TextView
    private lateinit var plantSpecies: TextView
    private lateinit var plantWater: TextView
    private lateinit var plantLight: TextView
    private lateinit var plantFlowering: TextView
    private lateinit var plantDescription: TextView
    private lateinit var backBtn: Button
    private lateinit var addBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_plantdata)
        val apiToken = "sk-rbVb697a55c86a75914567"
        val plant_species = intent.getStringExtra("plant_species").toString()

        backBtn = findViewById<Button>(R.id.backBtn)
        addBtn = findViewById<Button>(R.id.addBtn)
        plantName = findViewById<TextView>(R.id.plantNameTxt)
        plantSpecies = findViewById<TextView>(R.id.plantSpeciesTxt)
        plantWater = findViewById<TextView>(R.id.waterTxt)
        plantLight = findViewById<TextView>(R.id.lightTxt)
        plantFlowering = findViewById<TextView>(R.id.flowerTxt)
        plantDescription = findViewById<TextView>(R.id.descriptionTxt)
        plantImg = findViewById<ImageView>(R.id.plantImg)

        findPlantID(plant_species,apiToken)

        backBtn.setOnClickListener ( View.OnClickListener { view ->
            finish()
        })


    }

    fun findPlantID(plant_species: String, token: String){
        val url = "https://perenual.com/api/v2/species-list?q=$plant_species&key=$token"

        val queue = Volley.newRequestQueue(application)

        val request =
            JsonObjectRequest(Request.Method.GET,
                url,
                null,
                { response ->

                    try{
                        // The API returns an object with a "data" array
                        val dataArray = response.getJSONArray("data")

                        for (i in 0 until dataArray.length()) {
                            val plantObj = dataArray.getJSONObject(i)

                            val plant_id = plantObj.optString("id")

                            Log.e("myapp","$plant_id")

                            LoadPlantData(plant_id, token)
                        }

                    } catch (e: Exception){
                        Log.e("myapp", "Error parsing response: ${e.message}")
                    }
                },
                {
                    Log.e("myapp","Could not find ID")
                }
            )
        queue.add(request);
    }

    fun LoadPlantData(plant_id: String, token: String) : LiveData<List<Plants>> {
        val liveData = MutableLiveData<List<Plants>>()
        val url = "https://perenual.com/api/v2/species/details/$plant_id?key=$token"


        Log.e("myapp", "$url")
        val queue = Volley.newRequestQueue(application)

        val request =
            JsonObjectRequest(Request.Method.GET,
                url,
                null,
                { response ->
                    var result = mutableListOf<Plants>()

                    try{
                        // The API returns an object with a "data" array
                        // Get the default_image object and extract the thumbnail URL
                        val defaultImage = response.optJSONObject("default_image")
                        val imageUrl = defaultImage?.optString("small_url") ?: ""

                        val plant_watering_days = response.optString("watering_general_benchmark")
                        val waterJson = JSONObject(plant_watering_days)
                        val waterValue = waterJson.optString("value")
                        val waterUnit = waterJson.optString("unit")
                        val rawWater = waterValue.trim('"')
                        val plants_watering = "$rawWater $waterUnit"

                        val plant_light = response.getJSONArray("sunlight")
                        val FinalLight = (0 until plant_light.length()).joinToString(", "){plant_light.getString(it)}

                        val flowering = response.getJSONArray("pruning_month")
                        val flowering_season = (0 until flowering.length()).joinToString(", "){flowering.getString(it)}


                        Log.e("myapp","light: $FinalLight")

                        val plants = Plants(
                            id = response.optString("id"),
                            plant_name = response.optString("common_name"),
                            plant_species = response.optString("scientific_name").substring(2, response.optString("scientific_name").length - 2),
                            plant_age = "null",
                            plant_watering = "null",
                            plant_photo = "null",
                            plant_sunlight = FinalLight,
                            plant_flowering_season = flowering_season,
                            imgURL = imageUrl
                        )

                        plantName.text = response.optString("common_name")
                        plantSpecies.text = response.optString("scientific_name").substring(2, response.optString("scientific_name").length - 2)
                        plantWater.text = plants_watering
                        plantLight.text = FinalLight
                        plantFlowering.text = flowering_season
                        Picasso.with(this)
                            .load(imageUrl)
                            .into(plantImg)
                        plantDescription.text = response.optString("description")

                        Log.e("myapp", "$plants")

                        result.add(plants)

                        liveData.value = result

                    } catch (e: Exception){
                        Log.e("myapp", "Error parsing response: ${e.message}")
                        liveData.value = emptyList()
                    }
                },
                {
                        error -> liveData.value = emptyList()
                    Log.e("myapp","$error")
                }
            )
        queue.add(request);
        return liveData
    }
}