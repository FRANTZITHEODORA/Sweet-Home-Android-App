package com.example.homesweethome

import java.io.Serializable
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide

data class RealHome(
    val type: String = "",
    val size: String = "",
    val location: String = "",
    val price: String = "",
    val floor: String = "",
    val rooms: String = "",
    val bathrooms: String = "",
    val details: String = "",
    val description: String = "",
    val agency: String = "",
    val contactPhone: String = "",
    val email: String = "",
    val website: String = "",
    val pricePerSqm: String = "",
    val levels: String = "",
    val kitchens: String = "",
    val livingRooms: String = "",
    val heatingSystem: String = "",
    val energyClass: String = "",
    val yearBuilt: String = "",
    val yearRenovated: String = "",
    val systemCode: String = "",
    val propertyCode: String = "",
    val availability: String = "",
    val postDate: String = "",
    val lastUpdate: String = "",
    val internalFeatures: String = "",
    val externalFeatures: String = "",
    val propertySuitability: String = "",
    val additionalInfo: String = "",
    var homeImage: String = "",
    var homeImage2: String = "",
    var homeImage3: String = "",
    var homeImage4: String = "",
    var homeImage5: String = "",
    var homeImage6: String = ""
) : Serializable


class RealHomeAdapter (private val context: Context, private val realHomes: MutableList<RealHome>) : RecyclerView.Adapter<RealHomeAdapter.RealHomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealHomeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_real_home, parent, false)
        return RealHomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RealHomeViewHolder, position: Int) {
        val realHome = realHomes[position]
        holder.bind(realHome)
    }

    override fun getItemCount(): Int {
        return realHomes.size
    }

    inner class RealHomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val homeImagesViewPager: ViewPager2 = itemView.findViewById(R.id.realHomeImagesViewPager)
        private val homePrice: TextView = itemView.findViewById(R.id.realHomePrice)
        private val homeType: TextView = itemView.findViewById(R.id.realHomeType)
        private val homeSize: TextView = itemView.findViewById(R.id.realHomeSize)
        private val homeLocation: TextView = itemView.findViewById(R.id.realHomeLocation)
        private val homeFloor: TextView = itemView.findViewById(R.id.realHomeFloor)
        private val homeRooms: TextView = itemView.findViewById(R.id.realHomeRooms)
        private val homeBathrooms: TextView = itemView.findViewById(R.id.realHomeBathrooms)
        private val homeDescription: TextView = itemView.findViewById(R.id.realHomeDescription)
        private val homeContact: TextView = itemView.findViewById(R.id.realHomeContact)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val realHome = realHomes[position]
                    val intent = Intent(context, HomeDetailActivity::class.java)
                    intent.putExtra("realHome", realHome)
                    context.startActivity(intent)
                }
            }
        }

        fun bind(realHome: RealHome) {
            homePrice.text = realHome.price
            homeType.text = realHome.type
            homeSize.text = "Size: ${realHome.size}"
            homeLocation.text = realHome.location
            homeFloor.text = "Floor: ${realHome.floor}"
            homeRooms.text = "Rooms: ${realHome.rooms}"
            homeBathrooms.text = "Bathrooms: ${realHome.bathrooms}"
            homeDescription.text = realHome.description
            homeContact.text = "Contact: ${realHome.contactPhone}"

            // Set up ViewPager2 for images
            val images = listOf(
                realHome.homeImage, realHome.homeImage2, realHome.homeImage3,
                realHome.homeImage4, realHome.homeImage5, realHome.homeImage6
            ).filter { it.isNotEmpty() }

            val imagePagerAdapter = ImagePagerAdapter(context, images)
            homeImagesViewPager.adapter = imagePagerAdapter
        }
    }
}