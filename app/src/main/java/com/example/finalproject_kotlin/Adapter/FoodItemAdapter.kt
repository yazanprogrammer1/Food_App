package com.example.finalproject_kotlin.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject_kotlin.R
import com.example.finalproject_kotlin.databinding.FoodHolderBinding
import com.example.finalproject_kotlin.modele.FoodItem

class FoodItemAdapter(var activity: Activity, var arrayList: ArrayList<FoodItem>) :
    RecyclerView.Adapter<FoodItemAdapter.Holder>() {
    class Holder(var binding: FoodHolderBinding) : RecyclerView.ViewHolder(binding.root)

    var selectedItem: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val root = FoodHolderBinding.inflate(activity.layoutInflater, parent, false)
        return Holder(root)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.txtPrice.setText(arrayList.get(position).pric_food.toString())
//        holder.binding.foodImg.setImageResource(arrayList.get(position).image_food)
        holder.binding.foodTitle.setText(arrayList.get(position).name_food)
        holder.binding.rating.setRating(arrayList.get(position).rating_food.toString().toFloat())

        if (selectedItem == position) {
            holder.binding.foodBackground.animate().scaleX(1.1f)
            holder.binding.foodBackground.animate().scaleY(1.1f)
            holder.binding.foodTitle.setTextColor(Color.WHITE)
            holder.binding.txtPrice.setTextColor(Color.WHITE)
            holder.binding.foodBackground.setBackgroundResource(R.drawable.bg_design_item)
        } else {
            holder.binding.foodBackground.animate().scaleX(1f)
            holder.binding.foodBackground.animate().scaleY(1f)
            holder.binding.foodTitle.setTextColor(Color.WHITE)
            holder.binding.txtPrice.setTextColor(Color.WHITE)

            holder.binding.foodBackground.setBackgroundResource(R.drawable.bg_design_item2)
        }
        holder.binding.foodBackground.setOnClickListener {
            selectedItem = position
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


}