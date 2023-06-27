package com.example.finalproject_kotlin.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject_kotlin.Display_Food_Admin
import com.example.finalproject_kotlin.Insert_Admin_Food
import com.example.finalproject_kotlin.R
import com.example.finalproject_kotlin.databinding.EachResrtrantBinding
import com.example.finalproject_kotlin.modele.Restrant
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RestrantAdapter(var activity: Activity, var arrayList: ArrayList<Restrant>) :
    RecyclerView.Adapter<RestrantAdapter.Holder>() {

    class Holder(var binding: EachResrtrantBinding) : RecyclerView.ViewHolder(binding.root)

    lateinit var db: FirebaseFirestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var root = EachResrtrantBinding.inflate(activity.layoutInflater, parent, false)
        return Holder(root)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.restaurantNameTv.text = arrayList[position].name
        holder.binding.restaurantLocationTv.text = arrayList[position].location
        holder.binding.rateFood.rating = arrayList[position].rate.toString().toFloat()
        holder.binding.restaurantDescTv.text = arrayList[position].desc
        Glide.with(activity).load(arrayList[position].image)
            .into(holder.binding.restaurantImage)
        db = Firebase.firestore

        holder.binding.root.setOnClickListener {
            val alertDialog = AlertDialog.Builder(activity)
            alertDialog.setIcon(R.drawable.restaurant)
            alertDialog.setMessage("Food Manager")
            alertDialog.setTitle("Display Food")
            alertDialog.setCancelable(true)
            alertDialog.setPositiveButton("Add Food") { p, d ->
                val i = Intent(activity, Insert_Admin_Food::class.java)
                i.putExtra("name_rest", arrayList[position].name)
                i.putExtra("id_rest", arrayList[position].id)
                activity.startActivity(i)
            }
            alertDialog.setNegativeButton("Display Food") { n, d ->
                val i = Intent(activity, Display_Food_Admin::class.java)
                i.putExtra("name_rest", arrayList[position].name)
                i.putExtra("id_rest", arrayList[position].id)
                activity.startActivity(i)
            }
            alertDialog.create().show()
        }

        holder.binding.restaurantDelete.setOnClickListener {
            val alertDialog = AlertDialog.Builder(activity)
            alertDialog.setIcon(R.drawable.delete)
            alertDialog.setMessage("Are you sure to delete ?")
            alertDialog.setTitle("Delete")
            alertDialog.setCancelable(true)
            alertDialog.setPositiveButton("Delete") { p, d ->
                db.collection("restaurant").document(arrayList[position].id.toString())
                    .delete()
                    .addOnSuccessListener {
                        arrayList.removeAt(position)
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