package com.example.finalproject_kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject_kotlin.Adapter.RestrantAdapter
import com.example.finalproject_kotlin.Maps.Maps_Rest_Activity
import com.example.finalproject_kotlin.databinding.ActivityAdminBinding
import com.example.finalproject_kotlin.modele.Restrant
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class Admin_Activity : AppCompatActivity() {

    private var firebaseAuth: FirebaseAuth? = null
    private var firestore: FirebaseFirestore? = null
    private var mRecyclerView: RecyclerView? = null
    private val fab: FloatingActionButton? = null
    private var adapter: RestrantAdapter? = null
    private var list: ArrayList<Restrant>? = null
    private var query: Query? = null
    private var listenerRegistration: ListenerRegistration? = null
    lateinit var binding: ActivityAdminBinding
//    private var usersList: List<Users>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // code
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        mRecyclerView = binding.RestrantList
        list = arrayListOf()
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.setLayoutManager(
            LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false
            )
        )
        //..... get data
        runBlocking {
            getRest()
        }
        binding.btnBackAdmin.setOnClickListener {
            val i = Intent(this, Sgin_In_Activity::class.java)
            startActivity(i)
            finish()
        }
        binding.btnAdd.setOnClickListener {
            val i = Intent(this, Insert_Activity::class.java)
            startActivity(i)
            finish()
        }
        binding.btnLocation.setOnClickListener {
            val i = Intent(this, Maps_Rest_Activity::class.java)
            startActivity(i)
        }
    } //end OnCreate

    // Function For Get Rest in firestore
    fun getRest() {
        binding.prog.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            firestore!!.collection("restaurant").get()
                .addOnSuccessListener {
                    if (!it.isEmpty) {
                        for (data in it.documents) {
                            val restrant: Restrant? = data.toObject(Restrant::class.java)
                            if (restrant != null) {
                                list!!.add(restrant)
                            }
                        }
                        adapter = RestrantAdapter(this@Admin_Activity, list!!)
                        mRecyclerView!!.adapter = adapter
                        binding.prog.visibility = View.INVISIBLE
                    } else {
                        binding.prog.visibility = View.INVISIBLE
                        Toast.makeText(applicationContext, "not data", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, "error get", Toast.LENGTH_SHORT).show()
                    binding.prog.visibility = View.INVISIBLE
                }
                .await()
        }
    }

    //.. back
    override fun onBackPressed() {
        super.onBackPressed()
        var i = Intent(this@Admin_Activity, First_Screen::class.java)
        startActivity(i)
        finish()
    }
}