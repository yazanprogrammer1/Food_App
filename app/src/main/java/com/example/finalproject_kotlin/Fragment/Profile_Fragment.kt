package com.example.finalproject_kotlin.Fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.finalproject_kotlin.Dialog.Dialog_sign
import com.example.finalproject_kotlin.R
import com.example.finalproject_kotlin.Sgin_In_Activity
import com.example.finalproject_kotlin.databinding.FragmentProfileBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import de.hdodenhof.circleimageview.CircleImageView

class Profile_Fragment : Fragment() {
    // variable
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseFirestore
    lateinit var binding: FragmentProfileBinding
    lateinit var storage: FirebaseStorage
    private val progressBar: ProgressBar? = null
    private var storageReference: StorageReference? = null
    private var Uid: String? = null
    private var mImageUri: Uri? = null
    private val isPhotoSelected = false
    lateinit var imageView: CircleImageView
    lateinit var name: String
    lateinit var loading: Dialog_sign


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        Uid = auth.currentUser!!.uid
        imageView = binding.circleImageView
        loading = Dialog_sign(requireActivity())


        database.collection("Users").document(Uid!!).get()
            .addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                if (task.isSuccessful) {
                    if (task.result.exists()) {
                        name = task.result.getString("name").toString()
                        mImageUri = Uri.parse(task.result.getString("image").toString())
                        Glide.with(requireActivity()).load(mImageUri)
                            .into(binding.circleImageView)
                        Toast.makeText(requireActivity(), "Profile", Toast.LENGTH_SHORT).show()
                    }
                }
            })

        getData()

        binding.btnupload.setOnClickListener(View.OnClickListener { view ->
            if (mImageUri != null) {
                binding.prog.setVisibility(View.VISIBLE)
                val imageRef: StorageReference =
                    storageReference!!.child("Profile_pics").child(Uid + ".jpg")
                if (isPhotoSelected) {
                    if (mImageUri != null) {
                        imageRef.putFile(mImageUri!!).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                imageRef.downloadUrl.addOnSuccessListener { uri ->
                                    saveToFireStore(
                                        task,
                                        uri
                                    )
                                }
                            } else {
                                binding.prog.setVisibility(View.INVISIBLE)
                                Toast.makeText(
                                    requireActivity(),
                                    task.exception!!.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        binding.prog.setVisibility(View.INVISIBLE)
                        val snack = Snackbar.make(
                            view,
                            "Please Select picture and write your name",
                            Snackbar.LENGTH_LONG
                        )
                            .setAction("Ok") {

                            }
                        snack.show()
                    }
                } else {
                    saveToFireStore(null, mImageUri!!)
                }
            } else {
                val snack = Snackbar.make(
                    view,
                    "Select New Image",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Ok") {

                    }
                snack.show()
            }
        })

        binding.circleImageView.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        1
                    )
                } else {
                    val bottomSheetDialog =
                        BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme)
                    val bottomSheetView = LayoutInflater.from(requireActivity()).inflate(
                        R.layout.buttom_sheet_layout,
                        requireActivity().findViewById<LinearLayout>(R.id.layout_dialog)
                    )
                    bottomSheetView.findViewById<Button>(R.id.btn_store).setOnClickListener {
                        val gallery =
                            Intent(Intent.ACTION_PICK)
                        gallery.setType("image/*")
                        startActivityForResult(
                            gallery, 111
                        )
                        // استخدمنا هنا لارجاع بيانات بدل ستارت اكتيفتي العادية نستخدم هذه
                        // هنا هذا الرقم 100 يوحي ب الريكويستت كود للمقارنة عليه عندما تستلم الصورة من المستخدم
                    }
                    bottomSheetDialog.setContentView(bottomSheetView)
                    bottomSheetDialog.show()
                }
            }
        }
        binding.btnLogout.setOnClickListener { view ->
            val snack = Snackbar.make(
                view,
                "Logout !?",
                Snackbar.LENGTH_LONG
            )
                .setAction("Yes") {
                    auth.signOut()
                    val shared =
                        requireActivity().getSharedPreferences(
                            "user_data",
                            AppCompatActivity.MODE_PRIVATE
                        )
                    val editor = shared.edit()
                    editor.remove("isSign").apply()
                    val i = Intent(requireActivity(), Sgin_In_Activity::class.java)
                    startActivity(i)
                    requireActivity().finish()
                }
            snack.show()
        }
        binding.btnEdtUser.setOnClickListener {
            binding.edtUsername.isEnabled = true
        }
        binding.btnEdtPassword.setOnClickListener {
            binding!!.edtPassword.isEnabled = true
        }
        binding.btnEdtPhone.setOnClickListener {
            binding.edtPhone.isEnabled = true
        }

        // Inflate the layout for this fragment
        return binding.root

    }

    private fun saveToFireStore(
        task: Task<UploadTask.TaskSnapshot>?,
        downloadUri: Uri
    ) {
        val name = binding.edtUsername.text.toString()
        val birthday = binding.edtPassword.text.toString()
        val location = binding.edtPhone.text.toString()
        var hashMap = HashMap<String, Any>()
        hashMap.put("userName", name)
        hashMap.put("location", location)
        hashMap.put("birthday", birthday)
        hashMap.put("image", downloadUri.toString())

        database.collection("Users").document(Uid!!).update(hashMap).addOnCompleteListener(
            OnCompleteListener<Void?> { task ->
                if (task.isSuccessful) {
                    binding.prog.visibility = View.INVISIBLE
                    binding.edtPhone.isEnabled = false
                    binding.edtPassword.isEnabled = false
                    binding.edtUsername.isEnabled = false
                    Toast.makeText(
                        requireActivity(),
                        "Profile Settings Saved",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    binding.prog.visibility = View.INVISIBLE
                    Toast.makeText(
                        requireActivity(),
                        task.exception.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    // هذا الريكويست كود هو ال 100 الي حطيناها قبل
    // هنا هذا الانتنت الموجود خو الذي يرجع بيانات الصورة
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        // نتاكد بان العملية قد نجحت منخلال التالي
        if (resultCode == Activity.RESULT_OK && requestCode == 111) {
            mImageUri = data!!.data
            binding.circleImageView.setImageURI(mImageUri)
        } else if (resultCode == Activity.RESULT_OK && requestCode == 112) {
            val bitmap = data!!.extras!!.get("data")
            binding!!.circleImageView.setImageBitmap(bitmap as Bitmap)
        }
    }

    fun getData() {
        var idUser = auth.currentUser!!.uid
        loading.start_Loding()
        val docRef = database.collection("Users").document(idUser.toString())
        docRef.get().addOnSuccessListener {
            if (it != null) {
                val name = it.data!!["userName"].toString()
                val email = it.data!!["email"].toString()
                val birthday = it.data!!["birthday"].toString()
                val location = it.data!!["location"].toString()
//                Picasso.get().load(it.get("image").toString().toUri()).into(binding.circleImageView)
                binding.nameUser.text = name
                binding.phoneUser.text = email
                binding.edtUsername.setText(name)
                binding.edtPassword.setText(birthday)
                binding.edtPhone.setText(location)
                loading.isDismiss()
            } else {

            }
        }.addOnFailureListener {
            Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show()
        }

    }
}