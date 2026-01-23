package com.example.gardeningcsisapp.ui.plant

import android.os.Bundle
import android.text.Layout
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
        val token = arguments?.getString("token")

        viewModel.getToken()
        /*
        val recycler = view.findViewById<RecyclerView>(R.id.viewAllPlants)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val adapter = PlantAdapter()
        recycler.adapter = adapter

        viewModel.plants.observe(viewLifecycleOwner){ list ->
           adapter.updateList(list)
        }
        */



    }

}