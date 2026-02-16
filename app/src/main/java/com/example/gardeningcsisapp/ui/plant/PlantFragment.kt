package com.example.gardeningcsisapp.ui.plant

import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gardeningcsisapp.R
import com.example.gardeningcsisapp.domain.model.PlantAdapter
import com.example.gardeningcsisapp.domain.model.PlantsSearch
import kotlinx.coroutines.selects.SelectInstance

class PlantFragment : Fragment(R.layout.fragment_plant) {
    // TODO: Rename and change types of parameters
    private val viewModel: PlantViewModel by viewModels()
    private lateinit var plantAdapter: PlantAdapter
    private lateinit var plantRepository: PlantRepository

     override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstance: Bundle?
    ): View{
        val view = inflater.inflate(R.layout.fragment_plant, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("myapp", "Plant Page loaded")

        val recyclerView: RecyclerView = view.findViewById(R.id.viewUserPlants)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        plantAdapter = PlantAdapter(emptyList()){
            plant -> clickedPlants(plant)
        }
        recyclerView.adapter = plantAdapter

        // Observe the plants LiveData
        viewModel.plants.observe(viewLifecycleOwner) { plantsList ->
            Log.e("myapp", "Plants received: ${plantsList.size}")
            plantAdapter.updatePlants(plantsList)
        }

        val token = viewModel.getToken()
        viewModel.loadUserPlants(token)
    }

    private fun clickedPlants(plant: PlantsSearch) {
        Log.e("myapp", "Clicked: ${plant.plant_name}")
    }

}