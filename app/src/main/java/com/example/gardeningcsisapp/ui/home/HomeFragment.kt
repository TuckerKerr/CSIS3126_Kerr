package com.example.gardeningcsisapp.ui.home

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gardeningcsisapp.R
import com.example.gardeningcsisapp.databinding.FragmentHomeBinding
import com.example.gardeningcsisapp.domain.model.PlantAdapter
import com.example.gardeningcsisapp.domain.model.PlantsSearch
import com.example.gardeningcsisapp.ui.plant.PlantAddingActivity
import com.example.gardeningcsisapp.ui.plant.PlantViewModel
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var plantAdapter: PlantAdapter
    private val plantViewModel: PlantViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                homeViewModel.fetchLocation()
            }
        }
    private lateinit var addBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = requireActivity().intent.getStringExtra("token").toString()

        addBtn = view.findViewById<Button>(R.id.btnAdd)

        addBtn.setOnClickListener {
            val intent = Intent(requireContext(), PlantAddingActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }

        if(!token.isNullOrBlank()){
            homeViewModel.saveToken(token)
            homeViewModel.getToken()
        }

        homeViewModel.getPlantTime()

        val recyclerView: RecyclerView = view.findViewById(R.id.viewWaterPlants)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        plantAdapter = PlantAdapter(emptyList()){
                plant -> clickedPlants(plant)
        }
        recyclerView.adapter = plantAdapter

        // Observe the plants LiveData
        homeViewModel.plants.observe(viewLifecycleOwner) { plantsList ->
            Log.e("myapp", "Plants received: ${plantsList.size}")
            plantAdapter.updatePlants(plantsList)
        }

        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        homeViewModel.location.observe(viewLifecycleOwner) { (lat, lng) ->
            Log.e("myapp", "Lat: $lat, Lng: $lng")
        }

        homeViewModel.messages.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            homeViewModel.clearMessage()
        }

        homeViewModel.weatherString.observe(viewLifecycleOwner) { icon ->
            Log.e("myapp", "setting icon: $icon")
            val binding = _binding ?: return@observe
            Log.e("myapp", "binding is not null, setting image")
            binding.weatherIcon.setImageResource(icon)
            Log.e("myapp", "image set")

        }

    }

    fun clickedPlants(plant: PlantsSearch){
        Log.e("myapp", plant.id)
        homeViewModel.plantWatered(plant.id)
    }

}