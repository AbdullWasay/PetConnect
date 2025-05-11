package com.binarybeats.petconnect

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class AddPetProfile : AppCompatActivity() {

    private lateinit var selectedSpecies: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet_profile)

        // Get references to the ImageViews representing different species
        val catImageView: ImageView = findViewById(R.id.cat)
        val dogImageView: ImageView = findViewById(R.id.dog)
        val birdImageView: ImageView = findViewById(R.id.bird)
        val fishImageView: ImageView = findViewById(R.id.fish)
        val addPet = findViewById<AppCompatButton>(R.id.postMsg)

        val backbtn = findViewById<ImageView>(R.id.back)
        backbtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }

        // Set click listeners for each species ImageView
        catImageView.setOnClickListener {
            handleSpecies("Cat")
        }

        dogImageView.setOnClickListener {
            handleSpecies("Dog")
        }

        birdImageView.setOnClickListener {
            handleSpecies("Bird")
        }

        fishImageView.setOnClickListener {
            handleSpecies("Fish")
        }

        addPet.setOnClickListener {
            navigateToAddNameActivity(selectedSpecies)
        }
    }


    private fun navigateToAddNameActivity(species: String) {
        // Create an Intent to start the AddNameActivity
        val intent = Intent(this, Add_name::class.java)
        // Pass the species name as an argument to the next activity
        intent.putExtra("species", species)
        // Start the next activity
        startActivity(intent)
    }

    private fun handleSpecies(species: String) {
        selectedSpecies = species

        
        findViewById<ImageView>(R.id.cat).setBackgroundResource(0)
        findViewById<ImageView>(R.id.dog).setBackgroundResource(0)
        findViewById<ImageView>(R.id.bird).setBackgroundResource(0)
        findViewById<ImageView>(R.id.fish).setBackgroundResource(0)

        // Set background for the selected image view
        val selectedImageView = when (species) {
            "Cat" -> findViewById<ImageView>(R.id.cat)
            "Dog" -> findViewById<ImageView>(R.id.dog)
            "Bird" -> findViewById<ImageView>(R.id.bird)
            "Fish" -> findViewById<ImageView>(R.id.fish)
            else -> null
        }

        selectedImageView?.setBackgroundResource(R.drawable.runded_gray_bg)
    }
}
