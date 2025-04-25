package com.example.homesweethome

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class CreateListingActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private lateinit var editTextDescription: EditText
    private lateinit var editTextPrice: EditText
    private lateinit var editTextType: EditText
    private lateinit var editTextSize: EditText
    private lateinit var editTextLocation: EditText
    private lateinit var editTextFloor: EditText
    private lateinit var editTextRooms: EditText
    private lateinit var editTextBathrooms: EditText
    private lateinit var editTextDate: EditText
    private lateinit var editTextMap: EditText // Added for map

    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView
    private lateinit var imageView4: ImageView
    private lateinit var imageView5: ImageView
    private lateinit var imageView6: ImageView

    private var imageUris: MutableList<Uri?> = mutableListOf()

    private val pickImageContracts =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let {
                    imageUris.add(it)
                    displaySelectedImage(imageUris.size - 1, it)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_listing)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        editTextDescription = findViewById(R.id.editTextDescription)
        editTextPrice = findViewById(R.id.editTextPrice)
        editTextType = findViewById(R.id.editTextType)
        editTextSize = findViewById(R.id.editTextSize)
        editTextLocation = findViewById(R.id.editTextLocation)
        editTextFloor = findViewById(R.id.editTextFloor)
        editTextRooms = findViewById(R.id.editTextRooms)
        editTextBathrooms = findViewById(R.id.editTextBathrooms)
        editTextDate = findViewById(R.id.editTextDate)
        editTextMap = findViewById(R.id.editTextMap) // Added for map

        imageView1 = findViewById(R.id.imageView1)
        imageView2 = findViewById(R.id.imageView2)
        imageView3 = findViewById(R.id.imageView3)
        imageView4 = findViewById(R.id.imageView4)
        imageView5 = findViewById(R.id.imageView5)
        imageView6 = findViewById(R.id.imageView6)

        setupImageClickListeners()

        val buttonSave: Button = findViewById(R.id.buttonSave)
        buttonSave.setOnClickListener {
            saveListing()
        }
    }

    private fun setupImageClickListeners() {
        imageView1.setOnClickListener { openImagePicker() }
        imageView2.setOnClickListener { openImagePicker() }
        imageView3.setOnClickListener { openImagePicker() }
        imageView4.setOnClickListener { openImagePicker() }
        imageView5.setOnClickListener { openImagePicker() }
        imageView6.setOnClickListener { openImagePicker() }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        pickImageContracts.launch(intent)
    }

    private fun displaySelectedImage(imageIndex: Int, uri: Uri) {
        when (imageIndex) {
            0 -> imageView1.setImageURI(uri)
            1 -> imageView2.setImageURI(uri)
            2 -> imageView3.setImageURI(uri)
            3 -> imageView4.setImageURI(uri)
            4 -> imageView5.setImageURI(uri)
            5 -> imageView6.setImageURI(uri)
        }
    }

    private fun saveListing() {
        val description = editTextDescription.text.toString().trim()
        val price = editTextPrice.text.toString().trim()
        val type = editTextType.text.toString().trim()
        val size = editTextSize.text.toString().trim()
        val location = editTextLocation.text.toString().trim()
        val floor = editTextFloor.text.toString().trim()
        val rooms = editTextRooms.text.toString().trim()
        val bathrooms = editTextBathrooms.text.toString().trim()
        val date = editTextDate.text.toString().trim()
        val map = editTextMap.text.toString().trim() // Added for map

        if (validateInputs(description, price, type, size, location, floor, rooms, bathrooms, date, map)) {
            uploadListingToFirebase(description, price, type, size, location, floor, rooms, bathrooms, date, map)
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(
        description: String,
        price: String,
        type: String,
        size: String,
        location: String,
        floor: String,
        rooms: String,
        bathrooms: String,
        date: String,
        map: String
    ): Boolean {
        return description.isNotEmpty() && price.isNotEmpty() && type.isNotEmpty() &&
                size.isNotEmpty() && location.isNotEmpty() && floor.isNotEmpty() &&
                rooms.isNotEmpty() && bathrooms.isNotEmpty() && date.isNotEmpty() &&
                map.isNotEmpty() // Added for map
    }

    private fun uploadListingToFirebase(
        description: String,
        price: String,
        type: String,
        size: String,
        location: String,
        floor: String,
        rooms: String,
        bathrooms: String,
        date: String,
        map: String
    ) {
        val imageUrls = mutableListOf<String>()
        val uploadTasks = mutableListOf<Task<Uri>>()

        // Upload images to Firebase Storage
        for (uri in imageUris) {
            uri?.let {
                val imageName = "image_${UUID.randomUUID()}"
                val imageRef = storage.reference.child(imageName) // Changed path to root directory

                val uploadTask = imageRef.putFile(uri).continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    imageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.let { downloadUri ->
                            Log.d(TAG, "Image uploaded successfully: $downloadUri")
                            imageUrls.add(downloadUri.toString())
                        }
                    } else {
                        Log.e(TAG, "Error uploading image", task.exception)
                        Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                    }
                }

                uploadTasks.add(uploadTask)
            }
        }

        Tasks.whenAllComplete(uploadTasks).addOnCompleteListener {
            Log.d(TAG, "All images uploaded, saving listing data to Firestore.")
            saveListingDataToFirestore(description, price, type, size, location, floor, rooms, bathrooms, date, map, imageUrls)
        }
    }

    private fun saveListingDataToFirestore(
        description: String,
        price: String,
        type: String,
        size: String,
        location: String,
        floor: String,
        rooms: String,
        bathrooms: String,
        date: String,
        map: String,
        imageUrls: List<String>
    ) {
        val home = hashMapOf(
            "description" to description,
            "price" to price,
            "type" to type,
            "size" to size,
            "location" to location,
            "floor" to floor,
            "rooms" to rooms,
            "bathrooms" to bathrooms,
            "date" to date,
            "map" to map, // Added for map
            "homeImage" to (imageUrls.getOrNull(0) ?: ""),
            "homeImage2" to (imageUrls.getOrNull(1) ?: ""),
            "homeImage3" to (imageUrls.getOrNull(2) ?: ""),
            "homeImage4" to (imageUrls.getOrNull(3) ?: ""),
            "homeImage5" to (imageUrls.getOrNull(4) ?: ""),
            "homeImage6" to (imageUrls.getOrNull(5) ?: "")
        )

        db.collection("homes")
            .add(home)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(this, "Listing saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding document", e)
                Toast.makeText(this, "Failed to save listing", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val TAG = "CreateListingActivity"
    }
}
