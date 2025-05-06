package com.binarybeats.petconnect

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class DashBoard_Empty : AppCompatActivity() {
    private lateinit var userImg: String
    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board_empty)

        val profileImg = findViewById<ImageView>(R.id.profileImg4)
        val openMenu = findViewById<ImageView>(R.id.menuIcon)

        // Get the logged in user's id
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Get the reference to the user's data in Firebase Realtime Database
        val userRef = FirebaseDatabase.getInstance().getReference("users/$userId")

        openMenu.setOnClickListener() {
            val intent = Intent(this, Menu::class.java)
            intent.putExtra("user", userId)
            intent.putExtra("imageUrl", userImg)
            intent.putExtra("username", userName)
            startActivity(intent)
        }

        // Read the user's data
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get the profileImg attribute which contains the URL of the image
                val imageUrl = dataSnapshot.child("profileImg").getValue(String::class.java)
                userImg = imageUrl.toString()
                // Load the image using Glide
                Glide.with(this@DashBoard_Empty)
                    .load(imageUrl)
                    .error(R.drawable.ic_launcher_foreground) // Load a default image in case of error
                    .into(profileImg)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
            }
        })

        if (userId != null) {
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val username = dataSnapshot.child("name").getValue(String::class.java)
                    userName = username.toString()
                    val usernameTextView = findViewById<TextView>(R.id.username)
                    usernameTextView.text = username
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle possible errors.
                }
            })
        }

        val addprofile = findViewById<Button>(R.id.postMsg)
        addprofile.setOnClickListener {
            val intent = Intent(this, AddPetProfile::class.java)
            startActivity(intent)
        }
    }
}