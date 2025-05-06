package com.binarybeats.petconnect

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class Dashboard_Guest : AppCompatActivity() {
    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userArrayList : ArrayList<PetData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_guest)

        userRecyclerview = findViewById(R.id.petRecyclerView)
        userRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        userRecyclerview.setHasFixedSize(true)

        val openMenu = findViewById<ImageView>(R.id.menuIcon)
        val openSearch = findViewById<ImageView>(R.id.search)
        // Get the logged in user's id
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        openMenu.setOnClickListener() {
            val intent = Intent(this, Login::class.java)
            Toast.makeText(this, "Please Login to use this functionality", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

        openSearch.setOnClickListener() {
            val intent = Intent(this, SearchPet::class.java)
            startActivity(intent)
        }

        userArrayList = arrayListOf<PetData>()
        getUserData()

        val addprofile = findViewById<Button>(R.id.postMsg)
        addprofile.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            Toast.makeText(this, "Please Login to use this functionality", Toast.LENGTH_SHORT).show()
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
                    userRecyclerview.adapter = GuestAdapter(userArrayList, this@Dashboard_Guest)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }
}