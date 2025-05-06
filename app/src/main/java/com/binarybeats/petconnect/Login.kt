package com.binarybeats.petconnect

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        firebaseAuth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.Email)
        val passwordEditText = findViewById<EditText>(R.id.Password2)
        val loginButton = findViewById<AppCompatButton>(R.id.LoginButton)
        val guest = findViewById<AppCompatButton>(R.id.GuestButton)

        guest.setOnClickListener {
            val intent = Intent(this, Dashboard_Guest::class.java)
            startActivity(intent)
            finish()
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, navigate to Home activity
                        val intent = Intent(this, Dashboard::class.java)
                        startActivity(intent)
                        finish() // Finish this activity to prevent user from coming back with back button
                    } else {
                        // Sign in failed
                        val errorMessage = (task.exception?.message ?: "Unknown error occurred")
                        Toast.makeText(this, "Login failed: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
        }


        val signUpTextView = findViewById<TextView>(R.id.createAcc)
        signUpTextView.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }
}