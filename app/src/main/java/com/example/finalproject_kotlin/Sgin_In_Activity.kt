package com.example.finalproject_kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject_kotlin.Dialog.Dialog_sign
import com.example.finalproject_kotlin.databinding.ActivitySginInBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class Sgin_In_Activity : AppCompatActivity() {

    lateinit var outh: FirebaseAuth
    lateinit var binding: ActivitySginInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySginInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //... code sign in app in firebase

//        val d = Data_Base_Holder(this)

        var b: Boolean = false
        outh = FirebaseAuth.getInstance()
        //............................. برمجة العناصر
        binding.back2.setOnClickListener {
            val i = Intent(this, First_Screen::class.java)
            startActivity(i)
        }
        binding.register2.setOnClickListener { view ->
            if (binding.emailSign.text.toString()
                    .isNotEmpty() && binding.password2.text.toString().isNotEmpty()
            ) {
                val email = binding.emailSign.text.toString().trim()
                val pass = binding.password2.text.toString().trim()
//            binding.prog.visibility = View.VISIBLE
                val loading = Dialog_sign(this)
                loading.start_Loding()
                outh.signInWithEmailAndPassword(email, pass).addOnSuccessListener {

                    if (outh.currentUser!!.uid == "fsnWztgeYIO7COstDCXy8NEbgnh2") {
                        val snack = Snackbar.make(view, "Welcome Admin 👋", Snackbar.LENGTH_LONG)
                            .setAction("Hi") {

                            }
                        snack.show()
                        var i = Intent(applicationContext, Admin_Activity::class.java)
                        binding.prog.visibility = View.INVISIBLE
                        startActivity(i)
                        finish()
                    } else {
                        var i = Intent(applicationContext, Home_Activity::class.java)
                        val shared = getSharedPreferences("user_data", MODE_PRIVATE)
                        val editor = shared.edit()
                        editor.putBoolean("isSign", true)
                            .apply()
//                    binding.prog.visibility = View.INVISIBLE
                        loading.isDismiss()
                        startActivity(i)
                        val snack = Snackbar.make(view, "Welcome 👋", Snackbar.LENGTH_LONG)
                            .setAction("Hi") {
                            }
                        snack.show()
                        finish()
                    }
                }
                    .addOnFailureListener {
                        loading.isDismiss()
                        Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                    }
            } else {
                val snack = Snackbar.make(view, "No Data", Snackbar.LENGTH_LONG)
                    .setAction("Try") {

                    }
                snack.show()
            }
            // كود التسجيل القديم
//            val g =
//                d.get_Data(binding.phoneNumber.text.toString(), binding.password2.text.toString())
//            var id = d.get_id(binding.phoneNumber.text.toString())
//            if (g) {
//                val i = Intent(this, Home_Activity::class.java)
//                startActivity(i)
//                b = true
//                val shared = getSharedPreferences("user_data", MODE_PRIVATE)
//                val editor = shared.edit()
//                editor.putBoolean("isSign", b)
//                editor.putInt("userid", id)
//                val com = editor.commit()
//                if (com) {
//                    Toast.makeText(this, "true", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                binding.phoneNumber.setError("Wrong phone number or password")
//                binding.password2.setError("Wrong phone number or password")
//            }

        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}