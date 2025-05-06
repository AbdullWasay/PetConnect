package com.binarybeats.petconnect

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class BookedAppointments : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booked_appointments)

        val backBtn = findViewById<ImageView>(R.id.back)




        backBtn.setOnClickListener {
            val intent = Intent(this, BookAppointment::class.java)
            startActivity(intent)
        }
    }
}