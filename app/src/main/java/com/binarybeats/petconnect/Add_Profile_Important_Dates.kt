package com.binarybeats.petconnect

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class Add_Profile_Important_Dates : AppCompatActivity() {
    private lateinit var name: String
    private lateinit var breed: String
    private lateinit var species: String
    private lateinit var imageUrl: String
    private lateinit var selectedSize: String
    private lateinit var startdate: String
    private lateinit var enddate: String
    private lateinit var petWeight: String
    private var view: View? = null
    private var selected: String? = null
    private lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_profile_important_dates)
        val startLayout = findViewById<LinearLayout>(R.id.start_)
        val endLayout = findViewById<LinearLayout>(R.id.end_)
        view = findViewById(R.id.view)
        selected = "Start"

        name = intent.getStringExtra("name") ?: ""
        breed = intent.getStringExtra("breed") ?: ""
        species = intent.getStringExtra("species") ?: ""
        imageUrl = intent.getStringExtra("imageUrl") ?: ""
        selectedSize = intent.getStringExtra("size") ?: ""
        petWeight = intent.getStringExtra("petWeight") ?: ""
        startdate = intent.getStringExtra("StartDate") ?:""
        enddate = intent.getStringExtra("EndDate") ?:""

        val dateButton = findViewById<TextView>(R.id.Date)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        // Set listener for date changes
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            date = dateFormat.format(selectedDate.time)

            Toast.makeText(this@Add_Profile_Important_Dates, "Selected Date: $date", Toast.LENGTH_SHORT).show()
        }


        dateButton.setOnClickListener {
            addAnimal(name, breed, species, imageUrl, petWeight, startdate, enddate, date, selectedSize)
        }


        startLayout.setOnClickListener {
            setViewConstraintsStart()
            selected = "Start"
        }

        endLayout.setOnClickListener {
            setViewConstraintsEnd()
            selected = "End"
        }


    }

    private fun setViewConstraintsStart() {
        // Set constraints for view to match view2's constraints

        // Make view visible
        findViewById<View>(R.id.view).visibility = View.VISIBLE
        // Make view2 invisible
        findViewById<View>(R.id.view2).visibility = View.INVISIBLE
    }

    private fun setViewConstraintsEnd() {
        // Set constraints for view2 to match view's constraints
         // Make view2 visible
        findViewById<View>(R.id.view2).visibility = View.VISIBLE
        // Make view invisible
        findViewById<View>(R.id.view).visibility = View.INVISIBLE
    }

    private fun addAnimal(name: String, breed: String, species: String, imageUrl: String, petWeight: String, start: String, end: String, Date: String, size: String) {
        // Generate unique ID for the pet
        val intent = Intent(this, Add_Pet_Appointment::class.java)
        intent.putExtra("name", name)
        intent.putExtra("breed", breed)
        intent.putExtra("species", species)
        intent.putExtra("imageUrl", imageUrl)
        intent.putExtra("size", size)
        intent.putExtra("petWeight", petWeight)
        if (selected == "Start") {
            intent.putExtra("StartDate", Date)
            intent.putExtra("EndDate", end)
        } else {
            intent.putExtra("EndDate", Date)
            intent.putExtra("StartDate", start)
        }
        startActivity(intent)
        finish()
    }

}