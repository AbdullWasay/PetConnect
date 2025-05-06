package com.binarybeats.petconnect

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class PetAdapter(private val petList: List<PetData>, private var context: Context) :
    RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.your_pets_column,
            parent,false)
        return PetViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {

        val currentitem = petList[position]

        holder.firstName.text = currentitem.name
        Picasso.get().load(currentitem.imageUrl).into(holder.petImage)

        holder.viewPet.setOnClickListener {
            val intent = Intent(context, Profile::class.java).apply {
                putExtra("id", currentitem.id)
                putExtra("name", currentitem.name)
                putExtra("price", currentitem.price)
                putExtra("type", currentitem.type)
                putExtra("breed", currentitem.breed)
                putExtra("description", currentitem.description)
                putExtra("owner", currentitem.owner)
                putExtra("price", currentitem.price)
                putExtra("size", currentitem.size)
                putExtra("species", currentitem.species)
                putExtra("weight", currentitem.weight)
                putExtra("startDate", currentitem.startDate)
                putExtra("endDate", currentitem.endDate)
                putExtra("imageUrl", currentitem.imageUrl)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return petList.size
    }


    class PetViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val viewPet: LinearLayout = itemView.findViewById(R.id.viewPet)
        val firstName : TextView = itemView.findViewById(R.id.textViewName)
        val petImage : ImageView = itemView.findViewById(R.id.profileImg)

    }
}
