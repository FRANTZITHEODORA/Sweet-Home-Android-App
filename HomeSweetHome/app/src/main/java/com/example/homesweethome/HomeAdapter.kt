package com.example.homesweethome

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import java.io.Serializable

data class Home(
    val bathrooms: String = "",
    val date: String = "",
    val floor: String = "",
    var homeImage: String = "",
    var homeImage2: String = "",
    var homeImage3: String = "",
    var homeImage4: String = "",
    var homeImage5: String = "",
    var homeImage6: String = "",
    val location: String = "",
    val price: String = "",
    val rooms: String = "",
    val size: String = "",
    val type: String = "",
    val description: String = "",
    val map: String = "",

) : Serializable

class HomeAdapter(private val context: Context, private val homes: MutableList<Home>) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.itemhome, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val home = homes[position]
        holder.bind(home)
    }

    override fun getItemCount(): Int {
        return homes.size
    }

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val homeImagesViewPager: ViewPager2 = itemView.findViewById(R.id.homeImagesViewPager)
        private val homePrice: TextView = itemView.findViewById(R.id.homePrice)
        private val homeType: TextView = itemView.findViewById(R.id.homeType)
        private val homeSize: TextView = itemView.findViewById(R.id.homeSize)
        private val homeLocation: TextView = itemView.findViewById(R.id.homeLocation)
        private val homeFloor: TextView = itemView.findViewById(R.id.homeFloor)
        private val homeRooms: TextView = itemView.findViewById(R.id.homeRooms)
        private val homeBathrooms: TextView = itemView.findViewById(R.id.homeBathrooms)
        private val homeDate: TextView = itemView.findViewById(R.id.homeDate)
        private val homeMap: TextView = itemView.findViewById(R.id.homeMap) // Corrected ID

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val home = homes[position]
                    val intent = Intent(context, HomeDetailActivity::class.java)
                    intent.putExtra("home", home)
                    context.startActivity(intent)
                }
            }
        }

        fun bind(home: Home) {
            homePrice.text = home.price
            homeType.text = home.type
            homeSize.text = "Size: ${home.size} sq.m."
            homeLocation.text = home.location
            homeFloor.text = "Floor: ${home.floor}"
            homeRooms.text = "Rooms: ${home.rooms}"
            homeBathrooms.text = "Bathrooms: ${home.bathrooms}"
            homeDate.text = "Updated: ${home.date}"
            homeMap.text = home.map

            // Set up ViewPager2 for images
            val images = listOf(
                home.homeImage, home.homeImage2, home.homeImage3,
                home.homeImage4, home.homeImage5, home.homeImage6
            ).filter { it.isNotEmpty() }

            val imagePagerAdapter = ImagePagerAdapter(context, images)
            homeImagesViewPager.adapter = imagePagerAdapter
        }
    }
}
