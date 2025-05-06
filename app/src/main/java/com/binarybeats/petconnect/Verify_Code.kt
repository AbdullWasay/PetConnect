package com.binarybeats.petconnect

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class Verify_Code : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_code)

        val resend = findViewById<TextView>(R.id.resend)
        val signupButton = findViewById<AppCompatButton>(R.id.SignupButton)

        resend.setOnClickListener {
            val intent = Intent(this, Verify_Code::class.java)
            startActivity(intent)
        }

        signupButton.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }
    }
}