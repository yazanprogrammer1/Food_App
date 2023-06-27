package com.example.finalproject_kotlin.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject_kotlin.Food_Rest_Activity
import com.example.finalproject_kotlin.Maps.Maps_User_Activity
import com.example.finalproject_kotlin.R
import com.example.finalproject_kotlin.modele.Restrant
import com.google.android.gms.location.LocationServices

class RestrantAdapter_Home(var activity: Activity, var arrayList: List<Restrant>) :
    RecyclerView.Adapter<RestrantAdapter_Home.Holder>() {


    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvname: TextView = itemView.findViewById(R.id.food_title)
        val tvlocatio: TextView = itemView.findViewById(R.id.txt_price)
        val tvrate: RatingBar = itemView.findViewById(R.id.rating)
        val image: ImageView = itemView.findViewById(R.id.food_img)
        val tvdesc: TextView = itemView.findViewById(R.id.txt_desc)
        val foodRest: ImageView = itemView.findViewById(R.id.food_rest)
        val rest_locatoin: ImageView = itemView.findViewById(R.id.btn_location)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.food_holder, parent, false)
        return Holder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.tvname.text = arrayList[position].name
        holder.tvlocatio.text = arrayList[position].location
        holder.tvrate.rating = arrayList[position].rate.toString().toFloat()
        holder.tvdesc.text = arrayList[position].desc
        Glide.with(activity).load(arrayList[position].image)
            .into(holder.image)

        holder.foodRest.setOnClickListener {
            val i = Intent(activity, Food_Rest_Activity::class.java)
            i.putExtra("id", arrayList[position].id)
            i.putExtra("name", arrayList[position].name)
            i.putExtra("desc", arrayList[position].desc)
            activity.startActivity(i)
        }
        holder.rest_locatoin.setOnClickListener {
            val location = LocationServices.getFusedLocationProviderClient(activity)
            location.lastLocation
                .addOnSuccessListener { loc ->
                    if (loc != null) {
                        val latude = loc.latitude
                        val lngtude = loc.longitude
                        val i = Intent(activity, Maps_User_Activity::class.java)
                        i.putExtra("name", arrayList[position].name)
                        i.putExtra("lat", latude)
                        i.putExtra("long", lngtude)
                        activity.startActivity(i)
                    }else{
                        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Log.e("yazan", it.message.toString())
                }

        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

}