package com.example.malal.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductColor(
    val colour_name: String,
    val hex_value: String):Parcelable

fun convertMapToProductColor(map: Map<String, Any>): ProductColor
{
    return ProductColor(
            map["colour_name"].toString(),
            map["hex_value"].toString())
}