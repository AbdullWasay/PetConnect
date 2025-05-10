package com.binarybeats.petconnect

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Add_Pet_Appointment : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var name: String
    private lateinit var breed: String
    private lateinit var species: String
    private lateinit var imageUrl: String
    private lateinit var selectedSize: String
    private lateinit var startdate: String
    private lateinit var enddate: String
    private lateinit var petWeight: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet_appointment)


        val backBtn = findViewById<ImageView>(R.id.back)
        val start = findViewById<RelativeLayout>(R.id.relativeLayout1)
        val end = findViewById<RelativeLayout>(R.id.relativeLayout2)

        name = intent.getStringExtra("name") ?: ""
        breed = intent.getStringExtra("breed") ?: ""
        species = intent.getStringExtra("species") ?: ""
        imageUrl = intent.getStringExtra("imageUrl") ?: ""
        selectedSize = intent.getStringExtra("size") ?: ""
        petWeight = intent.getStringExtra("petWeight") ?: ""
        startdate = intent.getStringExtra("StartDate") ?:"Not Selected"
        enddate = intent.getStringExtra("EndDate") ?:"Not Selected"

        database = FirebaseDatabase.getInstance().getReference("Pets")
        loadPetImage(imageUrl)

        backBtn.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        start.setOnClickListener {
            val intent = Intent(this, Add_Profile_Important_Dates::class.java)
            addAnimal(intent, name, breed, species, imageUrl, petWeight, startdate, enddate, selectedSize)
        }

        end.setOnClickListener {
            val intent = Intent(this, Add_Profile_Important_Dates::class.java)
            addAnimal(intent, name, breed, species, imageUrl, petWeight, startdate, enddate, selectedSize)
        }

        val addPetButton = findViewById<AppCompatButton>(R.id.bookedbtn)
        addPetButton.setOnClickListener {
            val intent = Intent(this, Add_method::class.java)
            addAnimal(intent, name, breed, species, imageUrl, petWeight, startdate, enddate, selectedSize)
        }

        val startDateTextView = findViewById<TextView>(R.id.textView8)
        val endDateTextView = findViewById<TextView>(R.id.textView10)

        // Set the text of the TextViews with the received dates
        startDateTextView.text = startdate
        endDateTextView.text = enddate

//back
        val backbtn = findViewById<ImageView>(R.id.back)
        backbtn.setOnClickListener {
            val intent = Intent(this, Add_Profile_size::class.java)
            startActivity(intent)
        }

    }

    //load
    private fun loadPetImage(imageUrl: String) {
        val profileImageView = findViewById<ImageView>(R.id.profileImg)
        Glide.with(this@Add_Pet_Appointment)
            .load(imageUrl)
            .error(R.drawable.img_photo_place_image) // Load a default image in case of error
            .into(profileImageView)
    }

    private fun addAnimal(intent: Intent, name: String, breed: String, species: String, imageUrl: String, petWeight: String, startdate: String, enddate: String, size: String) {
        // Generate unique ID for the pet
        intent.putExtra("name", name)
        intent.putExtra("breed", breed)
        intent.putExtra("species", species)
        intent.putExtra("imageUrl", imageUrl)
        intent.putExtra("size", size)
        intent.putExtra("petWeight", petWeight)
        intent.putExtra("StartDate", startdate)
        intent.putExtra("EndDate", enddate)
        startActivity(intent)
        finish()
    }
}