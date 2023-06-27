package com.example.finalproject_kotlin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject_kotlin.Adapter.FoodAdapter
import com.example.finalproject_kotlin.databinding.ActivityDisplayFoodAdminBinding
import com.example.finalproject_kotlin.modele.FoodItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Display_Food_Admin : AppCompatActivity() {

    private var firebaseAuth: FirebaseAuth? = null
    private var firestore: FirebaseFirestore? = null
    private var mRecyclerView: RecyclerView? = null
    private val fab: FloatingActionButton? = null
    private var adapter: FoodAdapter? = null
    private var list: ArrayList<FoodItem>? = null
    private var query: Query? = null
    private var listenerRegistration: ListenerRegistration? = null
    lateinit var binding: ActivityDisplayFoodAdminBinding
    lateinit var nameRest: String
    lateinit var id_rest: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayFoodAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // code get food here
        nameRest = intent.getStringExtra("name_rest").toString()
        id_rest = intent.getStringExtra("id_rest").toString()
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        mRecyclerView = binding.foodList
        list = arrayListOf()
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        //.. toolbar code
        var toolbar = binding.toolbar
        binding.nameFoodTool.text = nameRest

        binding.btnCancle.setOnClickListener {
            finish()
        }

        //..... get Food
        get_Food_Of_Rest()
    }

    private fun get_Food_Of_Rest() = CoroutineScope(Dispatchers.Default).launch {
        binding.prog.visibility = View.VISIBLE
        firestore!!.collection("restaurant").document(id_rest.toString())
            .collection(nameRest.toString()).get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    for (data in it.documents) {
                        val restrant: FoodItem? = data.toObject(FoodItem::class.java)
                        if (restrant != null) {
                            list!!.add(restrant)
                        }
                    }
                    adapter = FoodAdapter(this@Display_Food_Admin, list!!)
                    mRecyclerView!!.adapter = adapter
                    binding.prog.visibility = View.INVISIBLE
                } else {
                    binding.prog.visibility = View.INVISIBLE
                    Toast.makeText(applicationContext, "not data", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "error get", Toast.LENGTH_SHORT)
                    .show()
                binding.prog.visibility = View.INVISIBLE
            }
    }

}