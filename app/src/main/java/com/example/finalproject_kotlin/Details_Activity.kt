package com.example.finalproject_kotlin

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.finalproject_kotlin.databinding.ActivityDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class Details_Activity : AppCompatActivity() {

    lateinit var binding: ActivityDetailsBinding
    private var mImageUri: Uri? = null
    private var storageReference: StorageReference? = null
    lateinit var database: FirebaseFirestore
    lateinit var storage: FirebaseStorage
    lateinit var auth: FirebaseAuth
    private var Uid: String? = null
    lateinit var nameRest: String
    lateinit var id_food_cart: String
    lateinit var name: String
    lateinit var price: String
    lateinit var desc: String
    lateinit var rate: String
    lateinit var image: String
    var num_item: Int = 1

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //........................................ code
        storageReference = FirebaseStorage.getInstance().reference
        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        Uid = auth.currentUser!!.uid
        name = intent.getStringExtra("name_food").toString()
        price = intent.getStringExtra("price_food").toString()
        desc = intent.getStringExtra("desc").toString()
        rate = intent.getStringExtra("rating").toString()
        image = intent.getStringExtra("image_food").toString()
        id_food_cart = intent.getStringExtra("id_food").toString()
        //.....
        binding.numItem.text = num_item.toString()
        binding.txtNameDetails.text = name
        binding.rateFood.rating = rate.toString().toFloat()
        binding.txtDescreption.text = desc
        binding.price.text = price
        binding.nameFoodTool.text = name
        Glide.with(this@Details_Activity).load(image).into(binding.imageDetails)

        binding.imgBackArrow.setOnClickListener {
            finish()
        }
        binding.imgFavorite.setOnClickListener {

        }
        binding.btnDetails.setOnClickListener {
            addToCart()
        }
        binding.plural.setOnClickListener {
            if (num_item > 0) {
                num_item++
                binding.numItem.text = num_item.toString()
                binding.price.text =
                    (binding.price.text.toString().toInt() + price.toInt()).toString()
            }
        }
        binding.Subtract.setOnClickListener {
            if (num_item > 1) {
                num_item--
                binding.numItem.text = num_item.toString()
                binding.price.text =
                    (binding.price.text.toString().toInt() - price.toInt()).toString()
            }
        }
    }//end OnCreate

    private fun addToCart() {
        var id = UUID.randomUUID().toString()
        binding!!.prog.visibility = View.VISIBLE
        val map = HashMap<String, Any>()
        map.put("id_cart", id)
        map.put("num_item", binding.numItem.text)
        map.put("id_food_cart", id_food_cart)
        map.put("rate_food_cart", rate)
        map.put("name_food_cart", name)
        map.put("description_food_cart", desc)
        map.put("price_cart", binding.price.text)
        map.put("price_food", price)
        map.put("image", image)
        database.collection("Users").document(Uid!!).collection("Cart").document(id).set(map)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.prog.visibility = View.INVISIBLE
                    Toast.makeText(
                        applicationContext, "Add To Cart", Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        applicationContext, it.exception.toString(), Toast.LENGTH_SHORT
                    ).show()
                    binding.prog.visibility = View.INVISIBLE
                }
            }
    }
}