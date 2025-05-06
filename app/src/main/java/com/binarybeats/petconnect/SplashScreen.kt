package com.binarybeats.petconnect

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        // Enable Firebase persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        Handler().postDelayed(Runnable {
            startActivity(Intent(this, Login::class.java))
            finish()
        }, 2000)

    }
}