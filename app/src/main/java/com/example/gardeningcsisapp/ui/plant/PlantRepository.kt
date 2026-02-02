package com.example.gardeningcsisapp.ui.plant

import android.app.Application
import android.util.Log
import androidx.core.content.res.TypedArrayUtils.getString

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gardeningcsisapp.domain.model.PlantsSearch

class PlantRepository(private val application: Application) {
//this class will hold all the calls to the backend for calling your personal plants and such
//From there the plants will be clickable to open up and see specific data!

    /*
    fun loadAllPlant(): LiveData<List<PlantsSearch>> {

        val liveData = MutableLiveData<List<PlantsSearch>>()
        val url = "https://perenual.com/api/v2/species-list?page=1&key=sk-w3Ap696f910ccf45c14440"

        val queue = Volley.newRequestQueue(application)

        val request =
            JsonObjectRequest(Request.Method.GET,
                url,
                null,
                { response ->
                    var result = mutableListOf<PlantsSearch>()

                    try{
                        // The API returns an object with a "data" array
                        val dataArray = response.getJSONArray("data")

                        for (i in 0 until dataArray.length()) {
                            val plantObj = dataArray.getJSONObject(i)

                            // Get the default_image object and extract the thumbnail URL
                            val defaultImage = plantObj.optJSONObject("default_image")
                            val imageUrl = defaultImage?.optString("small_url") ?: ""

                            val dirty_plant_species = plantObj.optString("scientific_name")
                            val plant_check = dirty_plant_species.substring(2, dirty_plant_species.length -2)


                            Log.e("myapp","$plant_check")

                            val plants = PlantsSearch(
                                id = plantObj.optString("id"),
                                plant_species = plantObj.optString("scientific_name").substring(2, plantObj.optString("scientific_name").length - 2),
                                plant_name = plantObj.optString("common_name"),
                                imgURL = imageUrl
                            )

                            result.add(plants)
                        }
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
     */
}