package com.binarybeats.petconnect

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")
        val price = intent.getStringExtra("price")
        val type = intent.getStringExtra("type")
        val breed = intent.getStringExtra("breed")
        val description = intent.getStringExtra("description")
        val owner = intent.getStringExtra("owner")
        val size = intent.getStringExtra("size")
        val species = intent.getStringExtra("species")
        val weight = intent.getStringExtra("weight")
        val startDate = intent.getStringExtra("startDate")
        val endDate = intent.getStringExtra("endDate")
        val imageUrl = intent.getStringExtra("imageUrl")

        val chat = findViewById<ImageView>(R.id.chat)
        val book = findViewById<AppCompatButton>(R.id.book)

        book.setOnClickListener {
            val intent = Intent(this, BookAppointment::class.java)
            intent.putExtra("owner", owner)
            intent.putExtra("pet", name)
            intent.putExtra("petId", id)
            intent.putExtra("imageUrl", imageUrl)
            startActivity(intent)
        }

        chat.setOnClickListener() {

            val currentUser = FirebaseAuth.getInstance().currentUser
            val userName = if (currentUser?.uid == "Xprl0SIw28Sk3fuFnnpVCLAAMfe2") {
                "Muhammad Ahmad"
            } else {
                "Hamid Ishaq"
            }

            val userID = currentUser?.uid ?: ""
            val chatKey = if (userID < "Xprl0SIw28Sk3fuFnnpVCLAAMfe2") {
                "$userID-Xprl0SIw28Sk3fuFnnpVCLAAMfe2"
            } else {
                "Xprl0SIw28Sk3fuFnnpVCLAAMfe2-$userID"
            }

            val intent = Intent(this, Chat::class.java)
            intent.putExtra("username", userName)
            startActivity(intent)

        }

        // Find views by their IDs
        val profileImg = findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.profileImg)
        val nameTextView = findViewById<TextView>(R.id.name)
        val speciesTextView = findViewById<TextView>(R.id.species)
        val breedTextView = findViewById<TextView>(R.id.breed)
        val descriptionTextView = findViewById<TextView>(R.id.description)
        val sizeTextView = findViewById<TextView>(R.id.size)
        val weightTextView = findViewById<TextView>(R.id.weight)
        val priceTextView = findViewById<TextView>(R.id.price)
        val dateTextView = findViewById<TextView>(R.id.date)

        // Set the retrieved values to the appropriate views
        nameTextView.text = name
        speciesTextView.text = species
        breedTextView.text = breed
        descriptionTextView.text = description
        sizeTextView.text = size
        weightTextView.text = weight
        priceTextView.text = price + " Rs"
        dateTextView.text = startDate

        // Load the image using Picasso
        Picasso.get().load(imageUrl).into(profileImg)

        val backbtn = findViewById<ImageView>(R.id.back)
        backbtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }
    }
}