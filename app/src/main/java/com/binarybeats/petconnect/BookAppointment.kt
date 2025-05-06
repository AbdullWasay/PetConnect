package com.binarybeats.petconnect

import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class BookAppointment : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var petName: String
    private lateinit var owner: String
    private lateinit var petid: String
    private lateinit var imageUrl: String
    private lateinit var selectedDate: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appointment)

// Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("bookings")

        // Retrieve user name from intent extras
        owner = intent.getStringExtra("owner") ?: ""
        petName = intent.getStringExtra("pet") ?: ""
        petid = intent.getStringExtra("petId") ?: ""
        imageUrl = intent.getStringExtra("imageUrl") ?: ""


        // Initialize CalendarView and set OnDateChangeListener
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
        }


    }

    fun book_appointment(view: View) {
        if (::selectedDate.isInitialized ) {
            // Save booking information to Firebase Realtime Database
            val bookingData = mapOf(
                "petName" to petName,
                "selectedDate" to selectedDate,
                "owner" to owner,
                )
            databaseReference.push().setValue(bookingData)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Booking successful
                        Toast.makeText(this, "Appointment booked successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        // Booking failed
                        Toast.makeText(this, "Failed to book appointment", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            // No date selected
            Toast.makeText(this, "Please select a date and time", Toast.LENGTH_SHORT).show()
        }
    }

}