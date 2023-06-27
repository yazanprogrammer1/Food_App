package com.example.finalproject_kotlin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject_kotlin.databinding.ActivityInsertBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class Insert_Activity : AppCompatActivity() {

    lateinit var binding: ActivityInsertBinding
    private var mImageUri: Uri? = null
    private var storageReference: StorageReference? = null
    lateinit var database: FirebaseFirestore
    lateinit var storage: FirebaseStorage
    lateinit var auth: FirebaseAuth
    private var Uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //... code
        storageReference = FirebaseStorage.getInstance().reference
        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        Uid = auth.currentUser!!.uid

        binding.btnDone.setOnClickListener {
            runBlocking {
                addRest(it)
            }
        }
        binding.imageInsert.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK)
            gallery.setType("image/*")
            startActivityForResult(
                gallery, 111
            )
        }
        binding.btnCancle.setOnClickListener {
            val i = Intent(this, Admin_Activity::class.java)
            startActivity(i)
            finish()
        }

        //.... Coroutines with Firebase


    }//end onCreate

    suspend fun addRest(it: View) {
        var name = binding.nameAdd.text.toString()
        var price = binding.rateAdd.text.toString()
        var desc = binding.descAdd.text.toString()
        var location = binding.locationAdd.text.toString()
        var id = UUID.randomUUID().toString()
        if (binding.nameAdd.text.isNotEmpty() && binding.rateAdd.text.isNotEmpty() && binding.descAdd.text.isNotEmpty() && binding.locationAdd.text.isNotEmpty() && mImageUri != null) {
            binding.prog.visibility = View.VISIBLE
            GlobalScope.launch(Dispatchers.IO) {
                var storage = storageReference!!.child("food_image")
                    .child(FieldValue.serverTimestamp().toString() + ".jpg")
                storage.putFile(mImageUri!!).addOnCompleteListener {
                    if (it.isSuccessful) {
                        storage.downloadUrl.addOnSuccessListener {
                            val map = HashMap<String, Any>()
                            map.put("id", id)
                            map.put("image", it.toString())
                            map.put("name", name)
                            map.put("rate", price)
                            map.put("desc", desc)
                            map.put("location", location)
                            database.collection("restaurant").document(id).set(map)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        binding!!.prog.visibility = View.INVISIBLE
                                        Toast.makeText(
                                            applicationContext,
                                            "restaurant Added Successfully ${Thread.currentThread().name} !!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent =
                                            Intent(applicationContext, Admin_Activity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            it.exception.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        binding!!.prog.visibility = View.INVISIBLE
                                    }
                                }
                        }
                    } else {
                        Toast.makeText(
                            applicationContext, it.exception!!.message, Toast.LENGTH_SHORT
                        ).show()
                        binding!!.prog.visibility = View.INVISIBLE
                    }
                }
            }

        } else {
            val snack = Snackbar.make(it, "Please Add Image", Snackbar.LENGTH_LONG)
                .setAction("Ok") {

                }
            snack.show()
        }
    }

    // هذا الريكويست كود هو ال 100 الي حطيناها قبل
    // هنا هذا الانتنت الموجود خو الذي يرجع بيانات الصورة
    override fun onActivityResult(
        requestCode: Int, resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        // نتاكد بان العملية قد نجحت منخلال التالي
        if (resultCode == Activity.RESULT_OK && requestCode == 111) {
            mImageUri = data!!.data
            binding.imageInsert.setImageURI(data!!.data)
        } else if (resultCode == Activity.RESULT_OK && requestCode == 112) {
            val bitmap = data!!.extras!!.get("data")
            binding.imageInsert.setImageBitmap(bitmap as Bitmap)
        }
    }


}