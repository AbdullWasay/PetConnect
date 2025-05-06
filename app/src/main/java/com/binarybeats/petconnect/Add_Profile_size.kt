package com.binarybeats.petconnect

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Add_Profile_size : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var name: String
    private lateinit var breed: String
    private lateinit var species: String
    private lateinit var imageUrl: String
    private lateinit var selectedSize: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_profile_size)

        // Get pet data from intent extras
        name = intent.getStringExtra("name") ?: ""
        breed = intent.getStringExtra("breed") ?: ""
        species = intent.getStringExtra("species") ?: ""
        imageUrl = intent.getStringExtra("imageUrl") ?: ""

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("Pets")

        // Load pet image from Realtime Database
        loadPetImage(imageUrl)

        // Set onClick listeners for size selection
        findViewById<ImageView>(R.id.small).setOnClickListener {
            handleSizeSelection("small")
            selectedSize = "Small"
        }

        findViewById<ImageView>(R.id.medium).setOnClickListener {
            handleSizeSelection("medium")
            selectedSize = "Medium"
        }

        findViewById<ImageView>(R.id.large).setOnClickListener {
            handleSizeSelection("large")
            selectedSize = "Large"
        }

        // Set onClick listener for addPet button
        val addPetButton = findViewById<AppCompatButton>(R.id.postMsg)
        addPetButton.setOnClickListener {
            val petWeight = findViewById<EditText>(R.id.pet_weight).text.toString()
            addAnimal(name, breed, species, imageUrl, petWeight, selectedSize)
        }

        val backbtn = findViewById<ImageView>(R.id.back)
        backbtn.setOnClickListener {
            val intent = Intent(this, Add_name::class.java)
            startActivity(intent)
        }
    }


    private fun loadPetImage(imageUrl: String) {
        val profileImageView = findViewById<ImageView>(R.id.profileImg)
        Glide.with(this@Add_Profile_size)
            .load(imageUrl)
            .error(R.drawable.img_photo_place_image) // Load a default image in case of error
            .into(profileImageView)
    }

    private fun handleSizeSelection(size: String) {


        // Reset backgrounds for all image views
        findViewById<ImageView>(R.id.small).setBackgroundResource(0)
        findViewById<ImageView>(R.id.medium).setBackgroundResource(0)
        findViewById<ImageView>(R.id.large).setBackgroundResource(0)

        // Set background for the selected image view
        val selectedImageView = when (size) {
            "small" -> findViewById<ImageView>(R.id.small)
            "medium" -> findViewById<ImageView>(R.id.medium)
            "large" -> findViewById<ImageView>(R.id.large)
            else -> null
        }

        selectedImageView?.setBackgroundResource(R.drawable.runded_gray_bg)
    }

    private fun addAnimal(name: String, breed: String, species: String, imageUrl: String, petWeight: String, size: String) {
        // Generate unique ID for the pet
        val intent = Intent(this, Add_Pet_Appointment::class.java)
        intent.putExtra("name", name)
        intent.putExtra("breed", breed)
        intent.putExtra("species", species)
        intent.putExtra("imageUrl", imageUrl)
        intent.putExtra("size", size)
        intent.putExtra("petWeight", petWeight)
        startActivity(intent)
        finish()
    }
}
