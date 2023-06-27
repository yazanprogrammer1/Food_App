package com.example.finalproject_kotlin.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject_kotlin.R
import com.example.finalproject_kotlin.databinding.FoodLayoutCartBinding
import com.example.finalproject_kotlin.modele.Food_Cart_Item
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Food_Cart_Adapter(var activity: Activity, var arrayList: MutableList<Food_Cart_Item>) :
    RecyclerView.Adapter<Food_Cart_Adapter.Holder>() {

    class Holder(var binding: FoodLayoutCartBinding) : RecyclerView.ViewHolder(binding.root)

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var root = FoodLayoutCartBinding.inflate(activity.layoutInflater, parent, false)
        return Holder(root)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.nameFood.text = arrayList[position].name_food
        holder.binding.priceFood.text = arrayList[position].pric_cart
        holder.binding.rateFood.rating = arrayList[position].rating_food.toString().toFloat()
        holder.binding.numItem.text = arrayList[position].numItem
        Glide.with(activity).load(arrayList[position].image_food).into(holder.binding.imageFood)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        var num_item = arrayList[position].numItem.toString().toInt()
        var price = arrayList[position].pric_food.toString().toInt()
        holder.binding.plural.setOnClickListener {
            if (num_item > 0) {
                num_item++
                holder.binding.numItem.text = num_item.toString()
                holder.binding.priceFood.text =
                    (holder.binding.priceFood.text.toString().toInt() + price.toInt()).toString()
                holder.binding.btnUpdate.visibility = View.VISIBLE
            }
        }
        holder.binding.Subtract.setOnClickListener {
            if (num_item > 1) {
                num_item--
                holder.binding.numItem.text = num_item.toString()
                holder.binding.priceFood.text =
                    (holder.binding.priceFood.text.toString().toInt() - price.toInt()).toString()
                holder.binding.btnUpdate.visibility = View.VISIBLE
            }
        }
        holder.binding.deleteCart.setOnClickListener {
            val alertDialog = AlertDialog.Builder(activity)
            alertDialog.setIcon(R.drawable.delete)
            alertDialog.setMessage("Are you sure to delete ?")
            alertDialog.setTitle("Delete")
            alertDialog.setCancelable(true)
            alertDialog.setPositiveButton("Delete") { p, d ->
                db.collection("Users").document(auth.currentUser!!.uid)
                    .collection("Cart")
                    .document(arrayList[position].id_food.toString())
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(activity, "delete", Toast.LENGTH_SHORT).show()
                        arrayList.removeAt(position)
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

        var t = 0
        for (i in 0..arrayList.size - 1) {
            t += arrayList[i].pric_cart!!.toString().toInt()
        }
        var total_price = activity.findViewById<TextView>(R.id.total_price)
        total_price.text = t.toString()

        holder.binding.btnUpdate.setOnClickListener {
            var hashMap = HashMap<String, Any>()
            hashMap.put("price_cart", holder.binding.priceFood.text)
            hashMap.put("num_item", holder.binding.numItem.text)
            db.collection("Users").document(auth.currentUser!!.uid).collection("Cart")
                .document(arrayList[position].id_food.toString()).update(hashMap)
                .addOnCompleteListener(
                    OnCompleteListener<Void?> { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                activity,
                                "Price Updated",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            holder.binding.btnUpdate.visibility = View.INVISIBLE
                        } else {
                            Toast.makeText(
                                activity,
                                task.exception.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
        }
    }


    override fun getItemCount(): Int {
        return arrayList.size
    }
}