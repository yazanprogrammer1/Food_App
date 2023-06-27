package com.example.finalproject_kotlin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject_kotlin.Adapter.Food_Home_Adapter
import com.example.finalproject_kotlin.databinding.ActivityFoodRestBinding
import com.example.finalproject_kotlin.modele.FoodItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class Food_Rest_Activity : AppCompatActivity() {

    private var firebaseAuth: FirebaseAuth? = null
    private var firestore: FirebaseFirestore? = null
    private var mRecyclerView: RecyclerView? = null
    private val fab: FloatingActionButton? = null
    private var adapter: Food_Home_Adapter? = null
    private var list: ArrayList<FoodItem>? = null
    private var query: Query? = null
    private var listenerRegistration: ListenerRegistration? = null
    lateinit var nameRest: String
    lateinit var id_rest: String
    lateinit var description: String
    lateinit var binding: ActivityFoodRestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodRestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // code food rest
        nameRest = intent.getStringExtra("name").toString()
        id_rest = intent.getStringExtra("id").toString()
        description = intent.getStringExtra("desc").toString()
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
        //.. back button
        binding.imgBackArrow.setOnClickListener {
            finish()
        }
        //.. toolbar code
        var toolbar_name = binding.nameRest
        toolbar_name.text = nameRest
        // description Restrant
        binding.descRest.text = description

        //.. search food
        //................... Search code
        binding.searchHome.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterList(newText)
                return false
            }
        })
        binding.searchHome.clearFocus()


        //..... get Food
        get_Food_Of_Rest()

    }//end OnCreate

    private fun filterList(text: String?) {
        if (text != null) {
            val list_filter: java.util.ArrayList<FoodItem> = java.util.ArrayList<FoodItem>()
            for (item in list!!) {
                if (item.name_food!!.toLowerCase()
                        .contains(text.lowercase(Locale.getDefault()))
                ) {
                    list_filter.add(item)
                }
            }
            if (list_filter.isEmpty()) {
                Toast.makeText(this@Food_Rest_Activity, "No Food Found", Toast.LENGTH_SHORT)
                    .show()
            } else {
                adapter!!.setFilterList(list_filter)
            }
        }
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
                    adapter = Food_Home_Adapter(this@Food_Rest_Activity, list!!)
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