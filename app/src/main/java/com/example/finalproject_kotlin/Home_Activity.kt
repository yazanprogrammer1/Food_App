package com.example.finalproject_kotlin

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.finalproject_kotlin.Fragment.Favorite_Fragment
import com.example.finalproject_kotlin.Fragment.HomeFragment
import com.example.finalproject_kotlin.Fragment.Profile_Fragment
import com.example.finalproject_kotlin.Fragment.Shopping_Fragment
import com.example.finalproject_kotlin.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home_Activity : AppCompatActivity() {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //.......................برمجة العناصر

//        val toolbar = binding.toolbarHome
//        toolbar.setTitle("Home")
//        toolbar.setNavigationIcon(R.drawable.ic_list)
//        //toolbar.menu.add(R.menu.toolbar_item)
//        toolbar.inflateMenu(R.menu.toolbar_item)
//
        select_fragment(HomeFragment())
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home_item -> {
                    select_fragment(HomeFragment())
//                    toolbar.setTitle("Home")
                }
                R.id.favorite_item -> {
                    select_fragment(Favorite_Fragment())
//                    toolbar.setTitle("Favorite")
                }
                R.id.shopping_item -> {
                    select_fragment(Shopping_Fragment())
//                    toolbar.setTitle("Shopping")
                }
                R.id.profile_item -> {
                    select_fragment(Profile_Fragment())
//                    toolbar.setTitle("Profile")
                }
            }
            true
        }

    }//end onCreate

    private fun select_fragment(fragment: androidx.fragment.app.Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_contaner, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shopping -> Toast.makeText(this, "shopping", Toast.LENGTH_SHORT).show()
            R.id.notification -> Toast.makeText(this, "noti", Toast.LENGTH_SHORT).show()
        }
        return true
    }


}