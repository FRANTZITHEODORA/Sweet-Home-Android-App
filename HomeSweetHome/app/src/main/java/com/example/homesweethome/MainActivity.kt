package com.example.homesweethome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class MainActivity : ComponentActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var realHomeAdapter: RealHomeAdapter
    private lateinit var realHomeList: MutableList<RealHome>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainactivity)

        mAuth = FirebaseAuth.getInstance()

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        realHomeList = mutableListOf()
        realHomeAdapter = RealHomeAdapter(this, realHomeList)
        recyclerView.adapter = realHomeAdapter

        // Fetch data from Firestore
        fetchHomes()

        val loginButton: ImageButton = findViewById(R.id.login_b)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Example of creating a new user
        val email = "test"
        val password = "password123"

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    // User created successfully
                } else {
                    // Handle failure
                }
            }

        // Example of signing in an existing user
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    // User signed in successfully
                } else {
                    // Handle failure
                }
            }

        // Delete existing test home and add a new one
        deleteAndAddTestHome()
    }

    private fun fetchHomes() {
        val db = FirebaseFirestore.getInstance()
        db.collection("realhomes")
            .get()
            .addOnSuccessListener { result ->
                realHomeList.clear() // Καθαρίζουμε την υπάρχουσα λίστα πριν προσθέσουμε νέα δεδομένα
                for (document in result) {
                    if (document.exists()) {
                        val realHome = RealHome(
                            type = document.getString("type") ?: "",
                            size = document.getString("size") ?: "",
                            location = document.getString("location") ?: "",
                            price = document.getString("price") ?: "",
                            floor = document.getString("floor") ?: "",
                            rooms = document.getString("rooms") ?: "",
                            bathrooms = document.getString("bathrooms") ?: "",
                            details = document.getString("details") ?: "",
                            description = document.getString("description") ?: "",
                            agency = document.getString("agency") ?: "",
                            contactPhone = document.getString("contactPhone") ?: "",
                            email = document.getString("email") ?: "",
                            website = document.getString("website") ?: "",
                            pricePerSqm = document.getString("pricePerSqm") ?: "",
                            levels = document.getString("levels") ?: "",
                            kitchens = document.getString("kitchens") ?: "",
                            livingRooms = document.getString("livingRooms") ?: "",
                            energyClass = document.getString("energyClass") ?: "",
                            yearBuilt = document.getString("yearBuilt") ?: "",
                            yearRenovated = document.getString("yearRenovated") ?: "",
                            systemCode = document.getString("systemCode") ?: "",
                            propertyCode = document.getString("propertyCode") ?: "",
                            heatingSystem = document.getString("heatingSystem") ?: "",
                            availability = document.getString("availability") ?: "",
                            postDate = document.getString("postDate") ?: "",
                            lastUpdate = document.getString("lastUpdate") ?: "",
                            internalFeatures = document.getString("internalFeatures") ?: "",
                            externalFeatures = document.getString("externalFeatures") ?: "",
                            propertySuitability = document.getString("propertySuitability") ?: "",
                            additionalInfo = document.getString("additionalInfo") ?: "",
                            homeImage = document.getString("homeImage") ?: "",
                            homeImage2 = document.getString("homeImage2") ?: "",
                            homeImage3 = document.getString("homeImage3") ?: "",
                            homeImage4 = document.getString("homeImage4") ?: "",
                            homeImage5 = document.getString("homeImage5") ?: "",
                            homeImage6 = document.getString("homeImage6") ?: ""
                        )

                        // Προσθήκη του σπιτιού στη λίστα
                        realHomeList.add(realHome)

                        // Φόρτωση των εικόνων από το Firebase Storage αν χρειαστεί
                        val imageUrls = listOf(
                            realHome.homeImage,
                            realHome.homeImage2,
                            realHome.homeImage3,
                            realHome.homeImage4,
                            realHome.homeImage5,
                            realHome.homeImage6
                        )
                        for ((index, imageRef) in imageUrls.withIndex()) {
                            if (imageRef.isNotEmpty()) {
                                val storageRef = FirebaseStorage.getInstance().reference.child(imageRef)
                                storageRef.downloadUrl.addOnSuccessListener { uri ->
                                    when (index) {
                                        0 -> realHome.homeImage = uri.toString()
                                        1 -> realHome.homeImage2 = uri.toString()
                                        2 -> realHome.homeImage3 = uri.toString()
                                        3 -> realHome.homeImage4 = uri.toString()
                                        4 -> realHome.homeImage5 = uri.toString()
                                        5 -> realHome.homeImage6 = uri.toString()
                                    }
                                    realHomeAdapter.notifyDataSetChanged() // Ενημέρωση του adapter μετά την αλλαγή των εικόνων
                                }.addOnFailureListener { exception ->
                                    Log.w("MainActivity", "Error getting image URL: ", exception)
                                }
                            }
                        }
                    }
                }
                realHomeAdapter.notifyDataSetChanged() // Ενημέρωση του adapter μετά την αλλαγή των δεδομένων
            }
            .addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting documents: ", exception)
            }
    }

    private fun deleteAndAddTestHome() {
        val db = FirebaseFirestore.getInstance()

        // Check if the test home already exists and delete it
        db.collection("homes")
            .whereEqualTo("price", "€550.000")
            .whereEqualTo("type", "Διαμέρισμα")
            .whereEqualTo("size", "168τ.μ.")
            .whereEqualTo("location", "Κολωνάκι (Κολωνάκι - Λυκαβηττός)")
            .whereEqualTo("floor", "1ος")
            .whereEqualTo("rooms", "4 υ/δ")
            .whereEqualTo("bathrooms", "1 μπτ")
            .whereEqualTo("date", "26/06/2024")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    db.collection("homes").document(document.id).delete()
                        .addOnSuccessListener {
                            Log.d("MainActivity", "Test home deleted")
                        }
                        .addOnFailureListener { e ->
                            Log.w("MainActivity", "Error deleting test home", e)
                        }
                }
                // Add a new test home after deleting the old one
//                addNewTestHome(db)
            }
            .addOnFailureListener { e ->
                Log.w("MainActivity", "Error checking test home existence", e)
            }
    }

//    private fun addNewTestHome(db: FirebaseFirestore) {
//        val newHome = RealHome(
//            price = "€550.000",
//            type = "Διαμέρισμα",
//            size = "168τ.μ.",
//            location = "Κολωνάκι (Κολωνάκι - Λυκαβηττός)",
//            floor = "1ος",
//            rooms = "4 υ/δ",
//            bathrooms = "1 μπτ",
//        )
//
//        db.collection("homes")
//            .add(newHome)
//            .addOnSuccessListener { documentReference ->
//                Log.d("MainActivity", "Test home added with ID: ${documentReference.id}")
//                // Optionally, fetch the homes again to see the new entry
//                fetchHomes()
//            }
//            .addOnFailureListener { e ->
//                Log.w("MainActivity", "Error adding test home", e)
//            }
//    }
}
