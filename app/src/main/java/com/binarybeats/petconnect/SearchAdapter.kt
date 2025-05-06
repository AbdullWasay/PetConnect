package com.binarybeats.petconnect

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class SearchAdapter(private val petList: List<PetData>, private var context: Context) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private var filteredList: List<PetData> = petList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pet, parent, false)
        return SearchViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val currentItem = filteredList[position]

        holder.petName.text = currentItem.name
        holder.type.text = currentItem.type
        holder.petPrice.text = currentItem.price

        // Load pet image using Glide library
        Picasso.get().load(currentItem.imageUrl).into(holder.petImg)

        holder.viewPet.setOnClickListener {
            val intent = Intent(context, Profile::class.java).apply {
                putExtra("id", currentItem.id)
                putExtra("name", currentItem.name)
                putExtra("price", currentItem.price)
                putExtra("type", currentItem.type)
                putExtra("breed", currentItem.breed)
                putExtra("description", currentItem.description)
                putExtra("owner", currentItem.owner)
                putExtra("price", currentItem.price)
                putExtra("size", currentItem.size)
                putExtra("species", currentItem.species)
                putExtra("weight", currentItem.weight)
                putExtra("startDate", currentItem.startDate)
                putExtra("endDate", currentItem.endDate)
                putExtra("imageUrl", currentItem.imageUrl)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun filter(text: String) {
        filteredList = if (text.isEmpty()) {
            petList
        } else {
            petList.filter { pet ->
                pet.name?.contains(text, true) ?: false
            }
        }
        notifyDataSetChanged()
    }

    fun filterBySpecies(species: String) {
        filteredList = if (species.isEmpty()) {
            petList
        } else {
            petList.filter { pet ->
                pet.species?.contains(species, true) ?: false
            }
        }
        notifyDataSetChanged()
    }

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewPet: ConstraintLayout = itemView.findViewById(R.id.viewPet)
        val petName: TextView = itemView.findViewById(R.id.petName)
        val type: TextView = itemView.findViewById(R.id.type)
        val petPrice: TextView = itemView.findViewById(R.id.petPrice)
        val petImg: CircleImageView = itemView.findViewById(R.id.petimg)
    }
}

