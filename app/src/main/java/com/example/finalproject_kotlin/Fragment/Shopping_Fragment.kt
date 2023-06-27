package com.example.finalproject_kotlin.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject_kotlin.Adapter.Food_Cart_Adapter
import com.example.finalproject_kotlin.databinding.FragmentShoppingBinding
import com.example.finalproject_kotlin.modele.Food_Cart_Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Shopping_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Shopping_Fragment : Fragment() {
    lateinit var binding: FragmentShoppingBinding
    private var firebaseAuth: FirebaseAuth? = null
    private var firestore: FirebaseFirestore? = null
    private var mRecyclerView: RecyclerView? = null
    private var adapter: Food_Cart_Adapter? = null
    private var list: ArrayList<Food_Cart_Item>? = null
    private var query: Query? = null
    private var listenerRegistration: ListenerRegistration? = null
    lateinit var nameRest: String
    lateinit var id_rest: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentShoppingBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        mRecyclerView = binding.cartRecycler
        list = arrayListOf()
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.setLayoutManager(
            LinearLayoutManager(
                requireActivity(), LinearLayoutManager.HORIZONTAL, false
            )
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalScope.launch {
            val users_cart = getFood_Cart_FromFirestore()
            // Process the retrieved users as needed
            withContext(Dispatchers.Main) {
                adapter = Food_Cart_Adapter(requireActivity(), users_cart)
                mRecyclerView!!.adapter = adapter
                adapter!!.notifyDataSetChanged()
            }
        }
    }

    suspend fun getFood_Cart_FromFirestore(): MutableList<Food_Cart_Item> =
        withContext(Dispatchers.IO) {
            binding!!.progCart.visibility = View.VISIBLE
            val firestore = FirebaseFirestore.getInstance()
            val usersCollection =
                firestore.collection("Users").document(firebaseAuth!!.currentUser!!.uid)
                    .collection("Cart")
            val usersSnapshot = usersCollection.get().await()
            val RestList = mutableListOf<Food_Cart_Item>()
            for (data in usersSnapshot.documents) {
                var id = data.get("id_cart")
                var image = data.get("image")
                var name = data.get("name_food_cart")
                var rate = data.get("rate_food_cart")
                var desc = data.get("description_food_cart")
                var price_cart = data.get("price_cart")
                var price_food = data.get("price_food")
                var numItem = data.get("num_item")
                var foodItem = Food_Cart_Item(
                    id.toString(),
                    image.toString(),
                    name.toString(),
                    rate.toString(),
                    price_cart.toString(),
                    price_food.toString(),
                    numItem.toString()
                )
                RestList.add(foodItem)
                Log.d("yaz", Thread.currentThread().name)
                binding!!.progCart.visibility = View.INVISIBLE
            }
            return@withContext RestList
        }

}