package com.binarybeats.petconnect

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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

class Menu : AppCompatActivity() {
    private lateinit var userImg: String
    private lateinit var userName: String
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userArrayList : ArrayList<PetData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("Pets")

        val userId = intent.getStringExtra("user") ?: ""
        userImg = intent.getStringExtra("imageUrl") ?: ""
        userName = intent.getStringExtra("username") ?: ""

        loadData(userImg, userName)

        val openProfile = findViewById<ImageView>(R.id.profileImg3)
        val login = findViewById<LinearLayout>(R.id.logoutButton)
        val edit = findViewById<LinearLayout>(R.id.edit)

        userRecyclerview = findViewById(R.id.recyclerView)
        userRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        userRecyclerview.setHasFixedSize(true)

        userArrayList = arrayListOf<PetData>()
        getUserData()

        openProfile.setOnClickListener {
            val intent = Intent(this, AddPetProfile::class.java)
            intent.putExtra("user", userId)
            intent.putExtra("imageUrl", userImg)
            intent.putExtra("username", userName)
            startActivity(intent)
        }

        edit.setOnClickListener {
            val intent = Intent(this, UpdateProfile::class.java)
            intent.putExtra("user", userId)
            intent.putExtra("imageUrl", userImg)
            intent.putExtra("username", userName)
            startActivity(intent)
        }

        login.setOnClickListener {
            //firebaseAuth.signOut()
            Toast.makeText(this , "Logged Out Of Account" , Toast.LENGTH_SHORT ).show()
            val intent = Intent(this@Menu, Login::class.java)
            startActivity(intent)
            finish()

        }

        val backbtn = findViewById<ImageView>(R.id.close)
        backbtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }

        val dash = findViewById<LinearLayout>(R.id.dashboard)
        dash.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }

        val booked = findViewById<LinearLayout>(R.id.booked)
        booked.setOnClickListener {
            val intent = Intent(this, BookedAppointments::class.java)
            startActivity(intent)
        }


    }

    private fun loadData(imageUrl: String, username: String) {
        val profileImageView = findViewById<ImageView>(R.id.dp)
        val profileName = findViewById<TextView>(R.id.username)
        Glide.with(this@Menu)
            .load(imageUrl)
            .error(R.drawable.profile) // Load a default image in case of error
            .into(profileImageView)

        profileName.text = username
    }



    private fun getUserData() {
        val currentUserUid = firebaseAuth.currentUser?.uid ?: return // Get the current user's UID

        dbref = FirebaseDatabase.getInstance().getReference("Pets")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userArrayList.clear() // Clear the list before populating it again
                    for (userSnapshot in snapshot.children) {
                        val pet = userSnapshot.getValue(PetData::class.java)
                        if (pet?.owner == currentUserUid) { // Check if the pet's owner matches the current user's UID
                            userArrayList.add(pet)
                        }
                    }
                    userRecyclerview.adapter = PetAdapter(userArrayList, this@Menu)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event if needed
            }
        })
    }


}

