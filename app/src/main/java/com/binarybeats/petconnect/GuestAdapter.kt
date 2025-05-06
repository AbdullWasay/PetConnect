package com.binarybeats.petconnect

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class GuestAdapter(private val petList: List<PetData>, private var context: Context) : RecyclerView.Adapter<GuestAdapter.DashViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pet,
            parent,false)
        return DashViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: DashViewHolder, position: Int) {

        val currentitem = petList[position]

        holder.firstName.text = currentitem.name
        holder.price.text = currentitem.price
        holder.saletype.text = currentitem.type
        Picasso.get().load(currentitem.imageUrl).into(holder.petImage)

        holder.viewPet.setOnClickListener {
            val intent = Intent(context, Login::class.java)
            Toast.makeText(context, "Please Login to use this functionality", Toast.LENGTH_SHORT).show()
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return petList.size
    }


    class DashViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val viewPet: ConstraintLayout = itemView.findViewById(R.id.viewPet)
        val firstName : TextView = itemView.findViewById(R.id.petName)
        val price : TextView = itemView.findViewById(R.id.petPrice)
        val saletype : TextView = itemView.findViewById(R.id.type)
        val petImage : ImageView = itemView.findViewById(R.id.petimg)

    }
}
