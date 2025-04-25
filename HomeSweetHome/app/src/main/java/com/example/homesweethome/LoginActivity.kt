package com.example.homesweethome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var eUsername: EditText
    private lateinit var ePassword: EditText
    private lateinit var bLogin: Button
    private lateinit var bSignUp: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginactivity) // Ensure this matches your XML file name

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Initialize views
        eUsername = findViewById(R.id.username)
        ePassword = findViewById(R.id.password)
        bLogin = findViewById(R.id.login_b) // Ensure this matches the button ID in XML
        bSignUp = findViewById(R.id.signUpButton)

        // Set up click listeners
        bSignUp.setOnClickListener {
            val email = eUsername.text.toString()
            val password = ePassword.text.toString()
            signUp(email, password)
        }

        bLogin.setOnClickListener {
            val email = eUsername.text.toString()
            val password = ePassword.text.toString()
            signIn(email, password)
        }
    }

    private fun signUp(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "User creation failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, UserProfileActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        // Create an Intent to navigate to MainActivity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Optional: call finish() if you want to close LoginActivity
    }
}
