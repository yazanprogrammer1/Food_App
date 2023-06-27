package com.example.finalproject_kotlin.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject_kotlin.Details_Activity
import com.example.finalproject_kotlin.databinding.FoodLayoutHomeBinding
import com.example.finalproject_kotlin.modele.FoodItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Food_Home_Adapter(var activity: Activity, var arrayList: List<FoodItem>) :
    RecyclerView.Adapter<Food_Home_Adapter.Holder>() {

    class Holder(var binding: FoodLayoutHomeBinding) : RecyclerView.ViewHolder(binding.root)

    lateinit var db: FirebaseFirestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var root = FoodLayoutHomeBinding.inflate(activity.layoutInflater, parent, false)
        return Holder(root)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.nameFood.text = arrayList[position].name_food
//        holder.binding.priceFood.text = arrayList[position].pric_food
        holder.binding.rateFood.rating =
            arrayList[position].rating_food.toString().toFloat()
//        holder.binding.descFood.text = arrayList[position].desc_food
        Glide.with(activity).load(arrayList[position].image_food)
            .into(holder.binding.imageFood)
        db = Firebase.firestore

        var id_rest = activity.intent.getStringExtra("id_rest")
        var name_rest = activity.intent.getStringExtra("name_rest")

        holder.binding.foodDetails.setOnClickListener {
            var i = Intent(activity, Details_Activity::class.java)
            i.putExtra("name_food", arrayList[position].name_food)
            i.putExtra("price_food", arrayList[position].pric_food)
            i.putExtra("desc", arrayList[position].desc_food)
            i.putExtra("rating", arrayList[position].rating_food)
            i.putExtra("image_food", arrayList[position].image_food)
            i.putExtra("id_food", arrayList[position].id_food)
            activity.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun setFilterList(listFilter: ArrayList<FoodItem>) {
        this.arrayList = listFilter
        notifyDataSetChanged()
    }
}