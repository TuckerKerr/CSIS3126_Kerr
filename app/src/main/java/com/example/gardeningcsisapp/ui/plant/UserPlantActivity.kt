package com.example.gardeningcsisapp.ui.plant

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gardeningcsisapp.R
import com.example.gardeningcsisapp.domain.model.Plants
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.getValue

class UserPlantActivity: AppCompatActivity()  {
    private val viewModel: PlantViewModel by viewModels()

    private lateinit var plantImg: ImageView
    private lateinit var plantName: TextView
    private lateinit var plantSpecies: TextView
    private lateinit var plantWater: TextView
    private lateinit var plantLight: TextView
    private lateinit var plantWatering: TextView
    private lateinit var plantDug: TextView
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_userplant)
        val plant_id = intent.getStringExtra("plant_id").toString()
        val userToken = intent.getStringExtra("token").toString()

        backBtn = findViewById<Button>(R.id.backBtn)
        plantName = findViewById<TextView>(R.id.plantNameTxt)
        plantSpecies = findViewById<TextView>(R.id.plantSpeciesTxt)
        plantWater = findViewById<TextView>(R.id.waterTxt)
        plantLight = findViewById<TextView>(R.id.lightTxt)
        plantWatering = findViewById<TextView>(R.id.showerTxt)
        plantDug = findViewById<TextView>(R.id.plantedTxt)
        plantImg = findViewById<ImageView>(R.id.plantImg)

        backBtn.setOnClickListener(View.OnClickListener { view ->
            finish()
        })

        Log.e("myapp", plant_id)
        loadPlantData(plant_id, userToken)

    }

    fun loadPlantData(plant_id: String?, userToken: String){
        val url = "http://10.0.2.2:8888/RootedGardening/getUserPlants.php?userToken=$userToken&plant_id=$plant_id&action=getPlant"
        val queue = Volley.newRequestQueue(application)
        Log.e("myapp", url)

        val request =
            JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    Log.e("myapp", response.optString("plants"))
                    val plantsData = response.getJSONArray("plants")
                    val plantsJson = plantsData.getJSONObject(0)
                    Log.e("myapp", "plants: ${plantsJson}")
                    Log.e("myapp", "plant Name: ${plantsJson.optString("plant_name")}")
                    try {
                        val planted_date = plantsJson.optString("last_watered").substring(0,10)
                        Log.e("myapp",planted_date)
                        val plants_interval = plantsJson.optLong("plant_watering_days")
                        val plant_last_watered = plantsJson.optString("plant_watering_days")

                        plantWater.text = plant_last_watered + " days"
                        plantLight.text = plantsJson.optString("plant_desired_sunlight")
                        plantName.text = plantsJson.optString("plant_name")
                        plantSpecies.text = plantsJson.optString("plant_species")
                        plantDug.text = plantsJson.optString("planted")

                        val plant_base_img = plantsJson.optString("plant_base_image")

                        val imageBytes = Base64.decode(plant_base_img, Base64.NO_WRAP)
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        plantImg.setImageBitmap(bitmap)

                        nextWaterDay(planted_date, plants_interval, plant_last_watered)

                    } catch (e: Exception) {
                        Log.e("myapp", "Error parsing response: ${e.message}")
                    }
                },
                { error ->
                    Log.e("myapp", "$error")
                }
            )
        queue.add(request);
    }

    fun nextWaterDay(lastWatered: String, intervalDays: Long, last_watered: String): LocalDate{
        //code that counts and allows me to put that into the text
        val startDate = LocalDate.parse(lastWatered)
        val todaysDate = LocalDate.now()

        if(startDate != todaysDate){
            val daysBetween = ChronoUnit.DAYS.between(startDate,todaysDate)
            val daysLeft = daysBetween % intervalDays
            val daysUntilWater = if (daysLeft == 0L) 0 else intervalDays - daysLeft

            plantWatering.text = daysUntilWater.toString()
        }
        else{
            plantWatering.text = last_watered
        }

        return (todaysDate.plusDays(0))
    }

    
}