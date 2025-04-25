package com.example.homesweethome

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserProfileActivity : AppCompatActivity() {

    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var editProfileButton: TextView
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile)  // Ensure this matches your XML file name

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Initialize views
        usernameTextView = findViewById(R.id.username)
        emailTextView = findViewById(R.id.email)
        editProfileButton = findViewById(R.id.edit_button)

        editProfileButton.setOnClickListener {
            val intent = Intent(this, CreateListingActivity::class.java)
            startActivity(intent)
        }

        // Fetch user data from Firebase
        val currentUser: FirebaseUser? = mAuth.currentUser
        currentUser?.let {
            // Assuming username is not set, using email as username for now
            usernameTextView.text = it.displayName ?: "Username is your email"
            emailTextView.text = it.email
        }
    }
}

