package com.example.homesweethome
import com.example.homesweethome.Home
import com.example.homesweethome.ImagePagerAdapter
import com.example.homesweethome.R

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

public class HomeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_detail)

        val home = intent.getSerializableExtra("home") as Home

        val homeImagesViewPager: ViewPager2 = findViewById(R.id.homeImagesViewPager)
        val homePrice: TextView = findViewById(R.id.homePrice)
        val homeType: TextView = findViewById(R.id.homeType)
        val homeSize: TextView = findViewById(R.id.homeSize)
        val homeLocation: TextView = findViewById(R.id.homeLocation)
        val homeFloor: TextView = findViewById(R.id.homeFloor)
        val homeRooms: TextView = findViewById(R.id.homeRooms)
        val homeBathrooms: TextView = findViewById(R.id.homeBathrooms)
        val homeDate: TextView = findViewById(R.id.homeDate)
        val homeDescription: TextView = findViewById(R.id.homeDescription)
        val homeMap: TextView = findViewById(R.id.homeMap) // Corrected ID

        // Set map point text (assuming this is a TextView in your activity_home_detail.xml layout)
        homeMap.text = home.map

        // Set other details
        homePrice.text = home.price
        homeType.text = home.type
        homeSize.text = "Size: ${home.size} sq.m."
        homeLocation.text = home.location
        homeFloor.text = "Floor: ${home.floor}"
        homeRooms.text = "Rooms: ${home.rooms}"
        homeBathrooms.text = "Bathrooms: ${home.bathrooms}"
        homeDate.text = "Updated: ${home.date}"
        homeDescription.text = home.description

        // Load images into ViewPager2
        val images = listOf(
            home.homeImage, home.homeImage2, home.homeImage3,
            home.homeImage4, home.homeImage5, home.homeImage6
        ).filter { it.isNotEmpty() }

        val imagePagerAdapter = ImagePagerAdapter(this, images)
        homeImagesViewPager.adapter = imagePagerAdapter
    }
}