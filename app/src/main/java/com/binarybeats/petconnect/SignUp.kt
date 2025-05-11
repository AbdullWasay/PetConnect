package com.binarybeats.petconnect

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException
import java.util.UUID

class SignUp : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val logintext = findViewById<TextView>(R.id.login)

        logintext.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        val profileImg = findViewById<ImageView>(R.id.editProfileImgBtn)
        profileImg.setOnClickListener {
            launchGallery()
        }



        val emailEditText = findViewById<EditText>(R.id.Email)
        val passwordEditText = findViewById<EditText>(R.id.Password)
        val nameEditText = findViewById<EditText>(R.id.Name)
        val phoneEditText = findViewById<EditText>(R.id.Phone)

        val signupButton: Button = findViewById(R.id.SignupButton)
        signupButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val name = nameEditText.text.toString()
            val phone = phoneEditText.text.toString()
            uploadImage()

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // User creation success
                        val user = firebaseAuth.currentUser
                        val userId = user?.uid

                        // Store user data in Firebase Realtime Database
                        val database = FirebaseDatabase.getInstance()
                        val usersRef = database.getReference("users")
                        val userData = hashMapOf(
                            "name" to name,
                            "Phone" to phone,
                            // Add more user data fields as needed
                        )
                        userId?.let {
                            usersRef.child(it).setValue(userData)
                                .addOnSuccessListener {
                                    // Data written successfully, navigate to verification page
                                    val intent = Intent(this, Login::class.java)
                                    startActivity(intent)
                                    finish() // Finish this activity to prevent user from coming back with back button
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Failed to write data to database: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        
                        val errorMessage = (task.exception?.message ?: "Unknown error occurred")
                        Toast.makeText(this, "Sign up failed: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }
    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
            && data != null && data.data != null )
        {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                val profileImg = findViewById<CircleImageView>(R.id.profileImg1)
                profileImg.setImageBitmap(bitmap)
            }
            catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    private fun uploadImage() {
        if(filePath != null) {
            val ref = firebaseStorage.reference.child("images/" + UUID.randomUUID().toString())
            ref.putFile(filePath!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { uri ->
                        val userId = firebaseAuth.currentUser?.uid
                        if (userId != null) {
                            val userRef = firebaseDatabase.getReference("users/$userId")
                            userRef.child("profileImg").setValue(uri.toString())
                        }
                    }
                    Toast.makeText(this@SignUp, "Uploaded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this@SignUp, "Failed "+e.message, Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress = 100.0*taskSnapshot.bytesTransferred/taskSnapshot
                        .totalByteCount
                }
        }
    }
}
