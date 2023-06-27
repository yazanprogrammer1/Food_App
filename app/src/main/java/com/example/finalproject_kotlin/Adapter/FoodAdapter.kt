package com.example.finalproject_kotlin.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.os.Build
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject_kotlin.R
import com.example.finalproject_kotlin.databinding.ProductsDesignBinding
import com.example.finalproject_kotlin.modele.FoodItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FoodAdapter(var activity: Activity, var arrayList: List<FoodItem>) :
    RecyclerView.Adapter<FoodAdapter.Holder>() {

    class Holder(var binding: ProductsDesignBinding) : RecyclerView.ViewHolder(binding.root)

    lateinit var db: FirebaseFirestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var root = ProductsDesignBinding.inflate(activity.layoutInflater, parent, false)
        return Holder(root)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.nameFood.text = arrayList[position].name_food
        holder.binding.priceFood.text = arrayList[position].pric_food
        holder.binding.restaurantRateTvFood.rating =
            arrayList[position].rating_food.toString().toFloat()
        holder.binding.descFood.text = arrayList[position].desc_food
        Glide.with(activity).load(arrayList[position].image_food)
            .into(holder.binding.imageFood)
        db = Firebase.firestore

        var id_rest = activity.intent.getStringExtra("id_rest")
        var name_rest = activity.intent.getStringExtra("name_rest")

        holder.binding.restaurantDelete.setOnClickListener {
            val alertDialog = AlertDialog.Builder(activity)
            alertDialog.setIcon(R.drawable.delete)
            alertDialog.setMessage("Are you sure to delete ?")
            alertDialog.setTitle("Delete")
            alertDialog.setCancelable(true)
            alertDialog.setPositiveButton("Delete") { p, d ->
                db.collection("restaurant").document(id_rest.toString())
                    .collection(name_rest.toString())
                    .document(arrayList[position].id_food.toString())
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(activity, "delete", Toast.LENGTH_SHORT).show()
                        notifyDataSetChanged()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(activity, exception.message, Toast.LENGTH_SHORT).show()
                    }
            }
            alertDialog.setNegativeButton("No") { n, d ->

            }
            alertDialog.create().show()
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}