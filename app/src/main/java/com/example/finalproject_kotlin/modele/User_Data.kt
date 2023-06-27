package com.example.finalproject_kotlin.modele

class User_Data(var id: Int, var user_name: String, var password: String, var phone: String) {

    companion object {
        const val TABLE_NAME = "Users"
        const val COL_ID = "Id"
        const val COL_NAME = "Name"
        const val COL_PASSWORD = "Password"
        const val COL_PHONE = "Phone"
        const val TABLE_CREAT =
            "create table ${TABLE_NAME} ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    "$COL_NAME  TEXT NOT NULL , $COL_PASSWORD TEXT NOT NULL , $COL_PHONE TEXT NOT NULL) "
    }

}