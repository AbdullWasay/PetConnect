package com.binarybeats.petconnect

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class Dashboard : AppCompatActivity() {
    private lateinit var userImg: String
    private lateinit var userName: String
    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userArrayList : ArrayList<PetData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        userRecyclerview = findViewById(R.id.petRecyclerView)
        userRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        userRecyclerview.setHasFixedSize(true)

        val profileImg = findViewById<ImageView>(R.id.profileImg4)
        val openMenu = findViewById<ImageView>(R.id.menuIcon)
        val openSearch = findViewById<ImageView>(R.id.search)
        // Get the logged in user's id
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        openMenu.setOnClickListener() {
            val intent = Intent(this, Menu::class.java)
            intent.putExtra("user", userId)
            intent.putExtra("imageUrl", userImg)
            intent.putExtra("username", userName)
            startActivity(intent)
        }

        openSearch.setOnClickListener() {
            val intent = Intent(this, SearchPet::class.java)
            intent.putExtra("user", userId)
            intent.putExtra("imageUrl", userImg)
            intent.putExtra("username", userName)
            startActivity(intent)
        }

        if (userId != null) {
            // Get the reference to the user's data in Firebase Realtime Database
            val userRef = FirebaseDatabase.getInstance().getReference("users/$userId")
            // Read the user's data
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get the profileImg attribute which contains the URL of the image
                    val imageUrl = dataSnapshot.child("profileImg").getValue(String::class.java)
                    userImg = imageUrl.toString()
                    // Load the image using Glide
                    Glide.with(this@Dashboard)
                        .load(imageUrl)
                        .error(R.drawable.ic_launcher_foreground) // Load a default image in case of error
                        .into(profileImg)

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

        userArrayList = arrayListOf<PetData>()
        getUserData()

        val addprofile = findViewById<Button>(R.id.postMsg)
        addprofile.setOnClickListener {
            val intent = Intent(this, AddPetProfile::class.java)
            startActivity(intent)
        }
    }

    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("Pets")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val pet = userSnapshot.getValue(PetData::class.java)
                        userArrayList.add(pet!!)
                    }
                    userRecyclerview.adapter = Dashboard_Adapter(userArrayList, this@Dashboard)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }
}