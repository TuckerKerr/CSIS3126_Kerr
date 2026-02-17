package com.example.gardeningcsisapp.ui.home

import android.app.Application
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gardeningcsisapp.domain.model.PlantsSearch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class HomeRepository (private val application: Application) {

    private val _plants = MutableLiveData<List<PlantsSearch>>()
    val plants: LiveData<List<PlantsSearch>> = _plants
    @RequiresApi(Build.VERSION_CODES.O)
    fun checkPlantsDates(userToken: String?){
        val url = "http://10.0.2.2:8888/RootedGardening/getUserPlants.php?userToken=$userToken&action=getTime"
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
                    try {
                        val count = plantsData.length()
                        var result = ArrayList<PlantsSearch>()

                        for(i in 0 until count){
                            val plantsJson = plantsData.getJSONObject(i)
                            Log.e("myapp", "plants data: ${plantsJson}")
                            val plantsId = plantsJson.optString("id")
                            val plant_name = plantsJson.optString("plant_name")
                            val plant_species = plantsJson.optString("plant_species")
                            val plant_img = plantsJson.optString("plant_base_image")
                            val plantsLastWater = plantsJson.optString("last_watered").substring(0,10)
                            val plantsWaterInterval = plantsJson.optString("plant_watering_days").toLong()

                            Log.e("myapp", "plants data: $plantsLastWater")
                            nextWaterDay(plantsId, plant_name, plant_species, plant_img, plantsLastWater, plantsWaterInterval, result)
                        }
                        _plants.value = result
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun nextWaterDay(plantId: String, plant_name: String, plant_species: String, plant_img: String,lastWatered: String, intervalDays: Long, result: ArrayList<PlantsSearch>): LocalDate{
        val startDate = LocalDate.parse(lastWatered)
        val todaysDate = LocalDate.now()

        val daysBetween = ChronoUnit.DAYS.between(startDate,todaysDate)
        val daysLeft = daysBetween % intervalDays
        val daysUntilWater = if (daysLeft == 0L) 0L else intervalDays - daysLeft
        Log.e("myapp", "Days between: $daysBetween | days left: $daysLeft |  Days until Water: $daysUntilWater")


        if(daysUntilWater == 0L && startDate != todaysDate){
            Log.e("myapp", "Plant need water: $plantId")
            result.add(PlantsSearch("$plantId","$plant_species","$plant_name","$plant_img"))
        }
        else{
            Log.e("myapp", "Plant does NOT need water: $plantId")
        }

        return (todaysDate.plusDays(daysUntilWater))
    }


    fun plantWatered(userToken: String?, plant_id: String){
        val url = "http://10.0.2.2:8888/RootedGardening/waterPlants.php?user_token=$userToken&plant_id=$plant_id"
        val queue = Volley.newRequestQueue(application)
        Log.e("myapp", url)

        val request =
            JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    val successvalue = response.optInt("success")

                    if(successvalue == 1){
                        try {
                            Log.e("myapp", "Function Worked: ${response.optString("errormessage")}")
                            removePlant(plant_id)
                        } catch (e: Exception) {
                            Log.e("myapp", "Error parsing response: ${e.message}")
                        }
                    }
                    else{
                        Log.e("myapp", "Error parsing response: ${response.optString("errormessage")}")
                    }

                },
                { error ->
                    Log.e("myapp", "$error")
                }
            )
        queue.add(request);
    }

    fun removePlant(plant_id: String){
        val newList = _plants.value?.toMutableList() ?: return
        newList.removeIf { it.id == plant_id }
        _plants.value = newList
    }
}