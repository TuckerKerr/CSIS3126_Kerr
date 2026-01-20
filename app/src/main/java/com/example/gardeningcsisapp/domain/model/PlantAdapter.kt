package com.example.gardeningcsisapp.domain.model

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.gardeningcsisapp.R
import com.squareup.picasso.Picasso

class PlantAdapter(private var list: List<PlantsSearch> = emptyList()):RecyclerView.Adapter<PlantAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_plants, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.txtPlantID.text = item.id
        holder.txtPlantname.text = item.plant_name
        holder.txtPlantspecies.text = item.plant_species

        val imageURL = item.imgURL

        if(!imageURL.isNullOrBlank()){
            Picasso.with(holder.itemView.context)
                .load(item.imgURL)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(holder.imgPlant)
        }
        else{
            holder.imgPlant.setImageResource(R.drawable.placeholder_image)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newList: List<PlantsSearch>) {
        list = newList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val txtPlantID: TextView = itemView.findViewById(R.id.txtID)

        val txtPlantname: TextView = itemView.findViewById(R.id.txtPlantname)
        val txtPlantspecies: TextView = itemView.findViewById(R.id.txtPlantspecies)
        val imgPlant: ImageView = itemView.findViewById(R.id.imgPlant)
    }
}
