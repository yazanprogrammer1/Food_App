package com.example.finalproject_kotlin.Data_Base

import android.app.Activity
import android.content.ClipDescription
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.finalproject_kotlin.modele.User_Data
import com.example.finalproject_kotlin.modele.Products
import kotlin.collections.ArrayList

class Data_Base_Holder(var activity: Activity) : SQLiteOpenHelper(activity, "Food_App", null, 1) {

    private var dbd = writableDatabase


    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(User_Data.TABLE_CREAT)
        db!!.execSQL(Products.TABLE_CREAT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE ${User_Data.TABLE_NAME}")
        db.execSQL("DROP TABLE ${Products.TABLE_NAME}")
        onCreate(db)
    }

    //............................... Users Table

    fun insert_User(name: String, password: String, phone: String): Boolean {
        val cv = ContentValues()
        cv.put(User_Data.COL_NAME, name)
        cv.put(User_Data.COL_PASSWORD, password)
        cv.put(User_Data.COL_PHONE, phone)
        return dbd.insert(User_Data.TABLE_NAME, null, cv) > 0
    }

    fun get_Data(phone: String, password: String): Boolean {
//        var array_user = ArrayList<User_Data>()
        val c = dbd.rawQuery(
            "SELECT * FROM ${User_Data.TABLE_NAME} WHERE ${User_Data.COL_PHONE} = '$phone' and ${User_Data.COL_PASSWORD} = '$password'",
            null
        )
        // الكيرسر برجع اذا كانت المعلوات متشابهة بزيد الكاونت بواحد يعني موجودة
        return c.count > 0
    }

    fun get_phone(phone: String): Boolean {
        val c = dbd.rawQuery(
            "SELECT * FROM ${User_Data.TABLE_NAME} WHERE ${User_Data.COL_PHONE} = '$phone'",
            null
        )
        return c.count > 0
    }

    fun edit_Data(id: Int, name: String, password: String): Boolean {
        val c = dbd.rawQuery(
            "UPDATE ${User_Data.TABLE_NAME} SET ${User_Data.COL_NAME} = '$name' , ${User_Data.COL_PASSWORD} = '$password'" +
                    "WHERE ${User_Data.COL_ID} = $id", null
        )
        return c.count > 0
    }

    fun get_id(phone: String): Int {
        val c = dbd.rawQuery(
            "SELECT ${User_Data.COL_ID} FROM ${User_Data.TABLE_NAME} WHERE ${User_Data.COL_PHONE} = '$phone' ",
            null
        )
        val bol = c.moveToFirst()
        if (bol) return c.getInt(0)
        else return -1
    }

    fun get_data_profile(id: Int): ArrayList<User_Data> {
        var data = ArrayList<User_Data>()
        val c = dbd.rawQuery(
            "SELECT * FROM ${User_Data.TABLE_NAME} WHERE ${User_Data.COL_ID} = $id",
            null
        )
        c.moveToFirst()
        while (!c.isAfterLast) {
            val user_data = User_Data(c.getInt(0), c.getString(1), c.getString(2), c.getString(3))
            data.add(user_data)
            c.moveToNext()
        }
        c.close()
        return data
    }


    //................................... Products Table

    fun insert_Product(name: String, price: Double, description: String ,category: String, favorite: Int): Boolean {
        val cv = ContentValues()
        cv.put(Products.COL_NAME, name)
        cv.put(Products.COL_PRICE, price)
        cv.put(Products.COL_DESCRIPTION,description)
        cv.put(Products.COL_CATEGORY, category)
        cv.put(Products.COL_FAVORITE, favorite)
        return dbd.insert(Products.TABLE_NAME, null, cv) > 0
    }

    fun get_products_purgar(): ArrayList<Products> {
        val products = ArrayList<Products>()
        val c = dbd.rawQuery(
            "SELECT * FROM ${Products.TABLE_NAME} WHERE ${Products.COL_CATEGORY} = 'Burger'",
            null
        )
        c.moveToFirst()
        while (!c.isAfterLast) {
            val p1 =
                Products(c.getInt(0), c.getString(1), c.getDouble(2), c.getString(3) ,c.getString(4), c.getInt(5))
            products.add(p1)
            c.moveToNext()
        }
        c.close()
        return products
    }

    fun get_products_pizza(): ArrayList<Products> {
        val products = ArrayList<Products>()
        val c = dbd.rawQuery(
            "SELECT * FROM ${Products.TABLE_NAME} WHERE ${Products.COL_CATEGORY} = 'Pizza'",
            null
        )
        c.moveToFirst()
        while (!c.isAfterLast) {
            val p =
                Products(c.getInt(0), c.getString(1), c.getDouble(2), c.getString(3) ,c.getString(4), c.getInt(5))
            products.add(p)
            c.moveToNext()
        }
        c.close()
        return products
    }

    fun get_products_Sweet(): ArrayList<Products> {
        val products = ArrayList<Products>()
        val c = dbd.rawQuery(
            "SELECT * FROM ${Products.TABLE_NAME} WHERE ${Products.COL_CATEGORY} = 'sweet'",
            null
        )
        c.moveToFirst()
        while (!c.isAfterLast) {
            val p =
                Products(c.getInt(0), c.getString(1), c.getDouble(2), c.getString(3),c.getString(4), c.getInt(5))
            products.add(p)
            c.moveToNext()
        }
        c.close()
        return products
    }

    fun get_product_id(name: String): Int {
        val c = dbd.rawQuery(
            "SELECT ${Products.COL_ID} FROM ${Products.TABLE_NAME} WHERE ${Products.COL_NAME} = '$name' ",
            null
        )
        val bol = c.moveToFirst()
        if (bol) return c.getInt(0)
        else return -1
    }

    fun delete_product(id: Int): Boolean {
        val c =
            dbd.rawQuery("DELETE FROM ${Products.TABLE_NAME} WHERE ${Products.COL_ID} = $id", null)
        return c.count > 0
    }


    fun add_Favorite(id: Int): Boolean {
        val c = dbd.rawQuery(
            "UPDATE ${Products.TABLE_NAME} SET ${Products.COL_FAVORITE} = 1  WHERE ${Products.COL_ID} = $id",
            null
        )
        return c.count > 0
    }

    fun delete_Favorite(id: Int): Boolean {
        val c = dbd.rawQuery(
            "UPDATE ${Products.TABLE_NAME} SET ${Products.COL_FAVORITE} = 0  WHERE ${Products.COL_ID} = $id",
            null
        )
        return c.count > 0
    }

    fun get_Favorite(): ArrayList<Products> {
        var products = ArrayList<Products>()
        val c = dbd.rawQuery(
            "SELECT * FROM ${Products.TABLE_NAME} WHERE ${Products.COL_FAVORITE} = 1",
            null
        )
        c.moveToFirst()
        while (!c.isAfterLast) {
            val favorite_data =
                Products(c.getInt(0), c.getString(1), c.getDouble(2), c.getString(3) , c.getString(4), c.getInt(5))
            products.add(favorite_data)
            c.moveToNext()
        }
        c.close()
        return products
    }

}