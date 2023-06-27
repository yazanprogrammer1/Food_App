package com.example.finalproject_kotlin.Servic

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.JobIntentService
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject_kotlin.Adapter.RestrantAdapter
import com.example.finalproject_kotlin.databinding.ActivityAdminBinding
import com.example.finalproject_kotlin.modele.Restrant
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class MyJobIntentService : JobIntentService() {

    companion object {
        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, MyJobIntentService::class.java, 123, intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("hzm", "onCreate")
        Toast.makeText(this, "Create", Toast.LENGTH_SHORT).show()
    }

    override fun onHandleWork(intent: Intent) {

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("hzm", "onDestroy")
    }

    override fun onStopCurrentWork(): Boolean {
        return super.onStopCurrentWork()
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }
}