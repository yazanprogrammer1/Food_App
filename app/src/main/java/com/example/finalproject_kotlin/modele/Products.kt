package com.example.finalproject_kotlin.modele

import android.annotation.SuppressLint
import android.media.Image
import android.os.Parcel
import android.os.Parcelable

@SuppressLint("ParcelCreator")
class Products(
    var id: Int,
    var name: String,
    var price: Double,
    var description: String,
    var category: String,
    var isFavorite: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt()
    ) {
    }

    companion object c : Parcelable.Creator<Products> {
        const val TABLE_NAME = "products"
        const val COL_ID = "Id"
        const val COL_NAME = "name"
        const val COL_PRICE = "price"
        const val COL_DESCRIPTION = "description"
        const val COL_CATEGORY = "category"
        const val COL_FAVORITE = "favorite"

        //const val COL_IMAGE = "image"
        const val TABLE_CREAT =
            "create table $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " $COL_NAME TEXT NOT NULL , $COL_PRICE double not null , $COL_DESCRIPTION TEXT NOT NULL , $COL_CATEGORY TEXT NOT NULL , $COL_FAVORITE INTEGER not null)"

        override fun createFromParcel(parcel: Parcel): Products {
            return Products(parcel)
        }

        override fun newArray(size: Int): Array<Products?> {
            return arrayOfNulls(size)
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeDouble(price)
        parcel.writeString(description)
        parcel.writeString(category)
        parcel.writeInt(isFavorite)
    }

    override fun describeContents(): Int {
        return 0
    }
}