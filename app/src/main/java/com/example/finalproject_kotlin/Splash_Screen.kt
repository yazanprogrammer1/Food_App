package com.example.finalproject_kotlin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Splash_Screen : AppCompatActivity() {

    private val splash_screen: Int = 5000
    var top: Animation? = null
    var bpttom: Animation? = null
    var imagelogo: ImageView? = null
    var logotext: TextView? = null
    var distext: TextView? = null

    var outh: FirebaseAuth? = null
    var store: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val shared = getSharedPreferences("user_data", MODE_PRIVATE)
        val com = shared.getBoolean("isSign", false)
        if (com == true) {
            Handler().postDelayed(Runnable() {
                val i = Intent(this, Home_Activity::class.java)
                startActivity(i)
                finish()
            }, splash_screen.toLong())
        } else {
            val i = Intent(this, First_Screen::class.java)
            startActivity(i)
            finish()
        }

        top = AnimationUtils.loadAnimation(this, R.anim.firstanim)
        bpttom = AnimationUtils.loadAnimation(this, R.anim.secandanim)

        imagelogo = findViewById<View>(R.id.imageView) as ImageView
        logotext = findViewById<View>(R.id.textView1) as TextView
        distext = findViewById<View>(R.id.textView2) as TextView

        imagelogo!!.animation = top
        logotext!!.animation = bpttom
        distext!!.animation = bpttom

    }
}