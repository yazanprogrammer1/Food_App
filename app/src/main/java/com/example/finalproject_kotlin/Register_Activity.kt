package com.example.finalproject_kotlin

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject_kotlin.Dialog.Dialog_sign
import com.example.finalproject_kotlin.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class Register_Activity : AppCompatActivity() {

    companion object {
        const val name = ""
    }

    var outh: FirebaseAuth? = null
    var store: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // code sign up app
//        val users = Data_Base_Holder(this)

        outh = FirebaseAuth.getInstance()
        store = FirebaseFirestore.getInstance()

        binding.birthday.setOnClickListener {
            val date = Calendar.getInstance()
            val year = date.get(Calendar.YEAR)
            val month = date.get(Calendar.MONTH)
            val day = date.get(Calendar.DAY_OF_MONTH)

            val date_picker = DatePickerDialog(
                this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    binding.birthday.setText("$year / ${month + 1} / $dayOfMonth")
                }, year, month, day
            )
            date_picker.show()
        }

        binding.back.setOnClickListener {
            val i = Intent(this, First_Screen::class.java)
            startActivity(i)
        }

        //.. كود قديم للتسجيل عبر داتا بيز لوكل
//        binding.register2.setOnClickListener {
////            val shared = getSharedPreferences("user_data", MODE_PRIVATE)
////            val editor = shared.edit()
//
//            if (binding.password2.text.toString().length >= 8 && binding.confirmPassword.text.toString()
//                    .equals(binding.password2.text.toString()) && binding.phone.text.toString().length == 13
//                && binding.phone.text.toString().substring(0, 4)
//                    .equals("+970") && binding.userName2.text.toString().isNotEmpty()
//            ) {
////                editor.putString("user_name", binding.userName2.text.toString())
////                editor.putString("password", binding.password2.text.toString())
////                editor.putString("confirm_password", binding.confirmPassword.text.toString())
////                editor.putString("phone", binding.phone.text.toString())
//                val c2 = users.get_phone(binding.phone.text.toString())
//                var c1 = users.insert_User(
//                    binding.userName2.text.toString(),
//                    binding.password2.text.toString(),
//                    binding.phone.text.toString()
//                )
////                val c = editor.commit()
//                if (c1 && !c2) {
//                    val i = Intent(this, Sgin_In_Activity::class.java)
//                    i.putExtra("name", binding.userName2.text.toString())
//                    i.putExtra("password", binding.password2.text.toString())
//                    i.putExtra("phone", binding.phone.text.toString())
//                    Toast.makeText(this, "Welcome 😉", Toast.LENGTH_SHORT).show()
//                    startActivity(i)
//                } else {
//                    Toast.makeText(this, "Math Phone 📞", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                binding.userName2.setError("Verify the username")
//                binding.password2.setError("Verify the password")
//                binding.confirmPassword.setError("Verify the password")
//                binding.phone.setError("Make sure the phone is typed correctly!!")
//            }
//        }


        binding.register2.setOnClickListener {
            val name = binding.userName2.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password2.text.toString()
            val birthday = binding.birthday.text.toString()
            val location = binding.location.text.toString()

            if (binding.userName2.text.toString().isNotEmpty() && binding.password2.text.toString()
                    .isNotEmpty() && binding.email.text.toString()
                    .isNotEmpty() && binding.birthday.text.toString().isNotEmpty()
                && binding.location.text.toString().isNotEmpty()
            ) {
//                binding.prog.visibility = View.VISIBLE
                val loading = Dialog_sign(this)
                loading.start_Loding()
                outh!!.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { tasks ->
                        if (tasks.isSuccessful) {
                            var id_user = outh!!.currentUser!!.uid
                            var hashMap = HashMap<String, Any>()
                            hashMap.put("userName", name)
                            hashMap.put("email", email)
                            hashMap.put("password", password)
                            hashMap.put("birthday", birthday)
                            hashMap.put("location", location)
                            store!!.collection("Users").document(id_user)
                                .set(hashMap).addOnSuccessListener {
                                    loading.isDismiss()
                                }
                                .addOnFailureListener {
                                    Log.e("yazan", it.message.toString())
                                }
                            finish()
                        } else {
                            Toast.makeText(applicationContext, "not complete", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            } else {
                Snackbar.make(it, "Enter Data 😑", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Try") {
                        // Responds to click on the action
                    }
                    .setBackgroundTint(resources.getColor(R.color.yellow))
                    .setActionTextColor(resources.getColor(R.color.black))
                    .show()
            }

        }
    }//end onCreate

}
