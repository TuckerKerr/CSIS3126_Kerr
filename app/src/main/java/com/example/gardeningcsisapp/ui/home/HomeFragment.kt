package com.example.gardeningcsisapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gardeningcsisapp.R
import com.example.gardeningcsisapp.ui.plant.PlantAddingActivity

class HomeFragment : Fragment(R.layout.fragment_home) {
    // TODO: Rename and change types of parameters

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var addBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
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

        homeViewModel.openPlantScreen.observe(viewLifecycleOwner){

        }


    }

}