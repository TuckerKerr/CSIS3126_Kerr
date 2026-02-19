package com.example.gardeningcsisapp.ui.home

import android.app.Application
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gardeningcsisapp.R
import com.example.gardeningcsisapp.domain.model.PlantsSearch
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class HomeRepository (private val application: Application) {
    private val _plants = MutableLiveData<List<PlantsSearch>>()
    val plants: LiveData<List<PlantsSearch>> = _plants

    private val _message = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _message


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
                            val plantsCleanSpecies = plant_species.replace("\\", "")

                            Log.e("myapp", "plants data: $plantsCleanSpecies")
                            nextWaterDay(plantsId, plant_name, plantsCleanSpecies, plant_img, plantsLastWater, plantsWaterInterval, result)
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
        _message.value = "Plant Watered!"
    }

    fun getWeather(latitude: Double, longitude: Double, result: (String?) -> Unit){
        val alltoken = readJSONFromFile()
        val token = JSONObject(alltoken).getString("openWeatherAPIKey")
        //Log.e("myapp", "token for openweatherapp: $token")
        //check the token to make sure you got the right one then implement into the other files
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$token"
        val queue = Volley.newRequestQueue(application)
        Log.e("myapp", url)
        val request =
            JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    Log.e("myapp", "getWeather Response: $response")
                    val weather = response.getJSONArray("weather")
                    val weatherJson = weather.getJSONObject(0)
                    val weatherType = weatherJson.getString("main")
                    Log.e("myapp", "Weather: $weatherType")
                    result(weatherType)
                },
                { error ->
                    Log.e("myapp", "error: $error")
                }
            )
        queue.add(request)
    }

    fun rainyWeatherWatering(userToken: String?){
        Log.e("myapp", "user token: $userToken")
        val url = "http://10.0.2.2:8888/RootedGardening/rainyDayWatering.php?user_token=$userToken"
        val queue = Volley.newRequestQueue(application)
        Log.e("myapp", url)

        val request =
            JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    val successvalue = response.optInt("success")
                    Log.e("myapp", "Success Value: $successvalue")

                    if(successvalue == 1){
                        try {
                            Log.e("myapp", "Function Worked: ${response.optString("errormessage")}")
                            _plants.value = emptyList()
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

    fun clearMessage(){
        _message.value = null
    }

    fun readJSONFromFile():String? {
        val json = try{
            application.assets.open("Keys.json")
            .bufferedReader().use { it.readText()}
        }catch (ex: Exception){
            ex.printStackTrace()
            return null
        }
        //Log.e("myapp", json)
        return json
    }
}

