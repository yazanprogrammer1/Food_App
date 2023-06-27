package com.example.finalproject_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject_kotlin.databinding.ActivitySplashBinding

class First_Screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //......................... برمجة العناصر
        binding.signInButton.setOnClickListener {
            val i = Intent(this,Sgin_In_Activity::class.java)
            startActivity(i)
        }

        binding.registerButton.setOnClickListener {
            val i = Intent(this,Register_Activity::class.java)
            startActivity(i)
        }
    }
}