package com.example.gardeningcsisapp.ui.plant

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.core.content.res.TypedArrayUtils.getString

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gardeningcsisapp.R
import com.example.gardeningcsisapp.domain.model.PlantAdapter
import com.example.gardeningcsisapp.domain.model.PlantsSearch

class PlantRepository(private val application: Application) {
//this class will hold all the calls to the backend for calling your personal plants and such
//From there the plants will be clickable to open up and see specific data!
private val _plants = MutableLiveData<List<PlantsSearch>>()
val plants: LiveData<List<PlantsSearch>> = _plants

    fun loadAllUserPlants(token: String?) {
        Log.e("myapp", "Repo ran")
        Log.e("myapp", "token ${token}")
        val result = ArrayList<PlantsSearch>()
        val url = "http://10.0.2.2:8888/RootedGardening/getPlants.php?user_token=$token"

        val queue = Volley.newRequestQueue(application)

        val request =
            JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    val successValue = response.get("success")
                    Log.e("myapp","success ${successValue} ran")
                    Log.e("myapp","Data: ${response}")
                    if(successValue == 1){
                        // The API returns an object with a "data" array
                        val plantsArray = response.getJSONArray("plants")

                        for (i in 0 until plantsArray.length()) {
                            val plantObj = plantsArray.getJSONObject(i)

                            Log.e("myapp", "$plantObj")

                            // Get the default_image object and extract the thumbnail URL
                            val plant_id = plantObj.optString("id")
                            val defaultImage = plantObj.optString("plant_base_image")
                            val plant_species = plantObj.optString("plant_species")
                            val plant_name = plantObj.optString("plant_name")
                            val plantsCleanSpecies = plant_species.replace("\\", "")


                            Log.e("MyApp","Plant Data: $plant_id, $defaultImage, $plant_species, $plant_name")
                            result.add(PlantsSearch("$plant_id","$plantsCleanSpecies","$plant_name","$defaultImage"))
                        }
                        _plants.value = result
                    }

                   },
                { error ->
                    Log.e("myapp", "$error")
                }
            )
        queue.add(request);
    }
}