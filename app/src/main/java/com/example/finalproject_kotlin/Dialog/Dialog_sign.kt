package com.example.finalproject_kotlin.Dialog

import android.app.Activity
import android.app.AlertDialog
import com.example.finalproject_kotlin.R

class Dialog_sign(var Activity: Activity) {

    private lateinit var isdialog: AlertDialog

    fun start_Loding() {
        // set View
        val inflater = Activity.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_dialog_sign, null)
        // set dialog
        val bulider = AlertDialog.Builder(Activity)
        bulider.setView(dialogView)
        bulider.setCancelable(false)
        isdialog = bulider.create()
        isdialog.show()
    }

    fun isDismiss() {
        isdialog.dismiss()
    }
}