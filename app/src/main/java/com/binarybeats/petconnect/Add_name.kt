package com.binarybeats.petconnect

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class Add_name : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_name)

        database = FirebaseDatabase.getInstance().getReference("Pets")
        storage = FirebaseStorage.getInstance()

        val backBtn = findViewById<ImageView>(R.id.back)
        val addPet = findViewById<AppCompatButton>(R.id.postMsg)
        val nameEditText = findViewById<EditText>(R.id.adopt_sell)
        val breedEditText = findViewById<EditText>(R.id.price)
        val profileImage = findViewById<ImageView>(R.id.profileImg)

        backBtn.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        profileImage.setOnClickListener {
            selectImageFromGallery()
        }

        val backbtn = findViewById<ImageView>(R.id.back)
        backbtn.setOnClickListener {
            val intent = Intent(this, AddPetProfile::class.java)
            startActivity(intent)
        }

        addPet.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val breed = breedEditText.text.toString().trim()
            val species = intent.getStringExtra("species")

            if (name.isNotEmpty() && breed.isNotEmpty() && species != null && ::selectedImageUri.isInitialized) {
                val imageName = UUID.randomUUID().toString()
                uploadImageToStorage(imageName, name, breed, species)
            } else {
                Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun uploadImageToStorage(imageName: String, name: String, breed: String, species: String) {
        val storageRef = storage.reference.child("petImages/$imageName.jpg")
        storageRef.putFile(selectedImageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Get the download URL from the task snapshot
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Now you have the download URL, you can pass it to addAnimal function
                    val downloadUrl = uri.toString()
                    addAnimal(name, breed, species, downloadUrl)
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to get download URL", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addAnimal(name: String, breed: String, species: String, imageUrl: String) {
        //val animalKey = database.push().key
        //Toast.makeText(this, "Pet Added Successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Add_Profile_size::class.java)
        intent.putExtra("name", name)
        intent.putExtra("breed", breed)
        intent.putExtra("species", species)
        intent.putExtra("imageUrl", imageUrl)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            selectedImageUri = data?.data!!
            findViewById<ImageView>(R.id.profileImg).setImageURI(selectedImageUri)
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
}
