package com.example.finalproject_kotlin.Fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject_kotlin.Adapter.RestrantAdapter_Home
import com.example.finalproject_kotlin.Data_Base.Data_Base_Holder
import com.example.finalproject_kotlin.databinding.FragmentHomeBinding
import com.example.finalproject_kotlin.modele.Restrant
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    var binding: FragmentHomeBinding? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var firestore: FirebaseFirestore? = null
    private var mRecyclerView: RecyclerView? = null
    private val fab: FloatingActionButton? = null
    private var adapter: RestrantAdapter_Home? = null
    private var list: ArrayList<Restrant>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val data_base = Data_Base_Holder(requireActivity())

//        val name = requireArguments().getString("name_food")
//        val price = requireArguments().getDouble("price_food")
//        val category = requireArguments().getString("category_food")
        // Add Food Item Code


        // Inflate the layout for this fragment
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        mRecyclerView = binding!!.recyclerFoodeItem
        list = arrayListOf()
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.setLayoutManager(
            LinearLayoutManager(
                requireActivity(), LinearLayoutManager.HORIZONTAL, false
            )
        )
        GlobalScope.launch {
            val users = getRestFromFirestore()
            // Process the retrieved users as needed
            withContext(Dispatchers.Main) {
                adapter = RestrantAdapter_Home(requireActivity(), users)
                mRecyclerView!!.adapter = adapter
                adapter!!.notifyDataSetChanged()
            }
        }
        GlobalScope.launch {
            getImage_UserFromFirestore()
        }
    }

    suspend fun getRestFromFirestore(): List<Restrant> = withContext(Dispatchers.IO) {
        binding!!.prog.visibility = View.VISIBLE
        val firestore = FirebaseFirestore.getInstance()
        val usersCollection = firestore.collection("restaurant")
        val usersSnapshot = usersCollection.get().await()
        val RestList = mutableListOf<Restrant>()
        for (document in usersSnapshot.documents) {
            val user = document.toObject(Restrant::class.java)
            user?.let {
                RestList.add(it)
                Log.d("yaz", Thread.currentThread().name)
                binding!!.prog.visibility = View.INVISIBLE
            }
        }
        return@withContext RestList
    }

    suspend fun getImage_UserFromFirestore() = withContext(Dispatchers.IO) {
        val firestore = FirebaseFirestore.getInstance()
        val usersCollection =
            firestore.collection("Users").document(firebaseAuth!!.currentUser!!.uid)
        val usersSnapshot = usersCollection.get()
            .addOnSuccessListener {
                if (it != null) {
                    val image_user = it.data!!["image"].toString()
                    Glide.with(requireActivity()).load(image_user)
                        .into(binding!!.imageHomeUser)
                    Log.d("yaz", Thread.currentThread().name)
                }
            }
            .await()
    }


}
