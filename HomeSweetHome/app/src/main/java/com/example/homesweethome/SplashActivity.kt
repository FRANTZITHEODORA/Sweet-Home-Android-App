package com.example.homesweethome

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private val splashDisplayLength: Long = 6000 // Duration for the splash screen (3 seconds)
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val link_owner: TextView = findViewById(R.id.link_owner)
        link_owner.setOnClickListener {
            val mainIntent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(mainIntent)
        }

        val tentant_button: Button = findViewById(R.id.tentant_button)
        tentant_button.setOnClickListener {
            handler.removeCallbacks(runnable) // Remove the delayed task
            finish() // Close the SplashActivity
            val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(mainIntent)
        }

        handler = Handler()
        runnable = Runnable {
            val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }

        // Start the handler to open MainActivity after the delay
        handler.postDelayed(runnable, splashDisplayLength)

        // Find the close button and set its click listener
        val closeButton: ImageButton = findViewById(R.id.closeButton)
        closeButton.setOnClickListener {
            handler.removeCallbacks(runnable) // Remove the delayed task
            finish() // Close the SplashActivity
            val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(mainIntent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable) // Ensure the runnable is removed when activity is destroyed
    }
}
