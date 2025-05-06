package com.binarybeats.petconnect

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateProfile : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var editButton: Button
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        // Initialize Firebase Auth and Database reference
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        // Initialize views
        nameEditText = findViewById(R.id.Name)
        phoneEditText = findViewById(R.id.Phone)
        editButton = findViewById(R.id.Editbtn)

        // Fetch and display current user data
        loadUserProfile()

        // Set up button click listener to update user profile
        editButton.setOnClickListener {
            updateUserProfile()
        }
    }

    private fun loadUserProfile() {
        val currentUser = auth.currentUser
        currentUser?.let {
            databaseReference.child(it.uid).get().addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val name = dataSnapshot.child("name").value.toString()
                    val phone = dataSnapshot.child("Phone").value.toString()

                    nameEditText.setText(name)
                    phoneEditText.setText(phone)
                } else {
                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUserProfile() {
        val currentUser = auth.currentUser
        val name = nameEditText.text.toString().trim()
        val phone = phoneEditText.text.toString().trim()


        currentUser?.let {
            val userUpdates = hashMapOf<String, Any>(
                "name" to name,
                "Phone" to phone
            )
            databaseReference.child(it.uid).updateChildren(userUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Profile update failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}