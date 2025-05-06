package com.binarybeats.petconnect

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Add_method : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var name: String
    private lateinit var breed: String
    private lateinit var species: String
    private lateinit var imageUrl: String
    private lateinit var selectedSize: String
    private lateinit var petWeight: String
    private lateinit var startdate: String
    private lateinit var enddate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_method)

        name = intent.getStringExtra("name") ?: ""
        breed = intent.getStringExtra("breed") ?: ""
        species = intent.getStringExtra("species") ?: ""
        imageUrl = intent.getStringExtra("imageUrl") ?: ""
        selectedSize = intent.getStringExtra("size") ?: ""
        petWeight = intent.getStringExtra("petWeight") ?: ""
        startdate = intent.getStringExtra("StartDate") ?:""
        enddate = intent.getStringExtra("EndDate") ?:""

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("Pets")

        loadPetImage(imageUrl)

        val backbtn = findViewById<ImageView>(R.id.back)
        backbtn.setOnClickListener {
            val intent = Intent(this, Add_Pet_Appointment::class.java)
            startActivity(intent)
        }

        val addPetButton = findViewById<AppCompatButton>(R.id.postMsg)
        addPetButton.setOnClickListener {
            val type = findViewById<EditText>(R.id.adopt_sell).text.toString()
            val price = findViewById<EditText>(R.id.price).text.toString()
            val description = findViewById<EditText>(R.id.desc).text.toString()
            val currentUser = FirebaseAuth.getInstance().currentUser

            if (currentUser != null) {
                addAnimal(name, breed, species, imageUrl, petWeight, selectedSize, type, price, description, currentUser.uid, startdate, enddate)
            }
        }
    }

    private fun loadPetImage(imageUrl: String) {
        val profileImageView = findViewById<ImageView>(R.id.profileImg)
        Glide.with(this@Add_method)
            .load(imageUrl)
            .error(R.drawable.img_photo_place_image) // Load a default image in case of error
            .into(profileImageView)
    }

    private fun addAnimal(
        name: String,
        breed: String,
        species: String,
        imageUrl: String,
        petWeight: String,
        selectedSize: String,
        type: String,
        price: String,
        description: String,
        currentUser: String,
        startDate: String,
        endDate: String
    ) {
        // Generate unique ID for the pet
        val animalKey = database.push().key ?: ""

        // Create data map for the pet
        val animalData = hashMapOf(
            "name" to name,
            "breed" to breed,
            "species" to species,
            "imageUrl" to imageUrl,
            "size" to selectedSize,
            "weight" to petWeight,
            "type" to type,
            "price" to price,
            "description" to description,
            "owner" to currentUser,
            "startDate" to startDate,
            "endDate" to endDate
        )

        // Check if animalKey is not empty
        if (animalKey.isNotEmpty()) {
            // Add pet data to the Firebase Realtime Database
            database.child(animalKey).setValue(animalData)
                .addOnSuccessListener {
                    // Pet added successfully
                    Toast.makeText(this, "Pet Added Successfully", Toast.LENGTH_SHORT).show()
                    // Navigate to the next activity
                    val intent = Intent(this, Dashboard::class.java)
                    intent.putExtra("imageUrl", imageUrl)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    // Failed to add pet
                    Toast.makeText(this, "Failed to add pet", Toast.LENGTH_SHORT).show()
                }
        } else {
            // animalKey is empty
            Toast.makeText(this, "Failed to generate pet ID", Toast.LENGTH_SHORT).show()
        }
    }
}
