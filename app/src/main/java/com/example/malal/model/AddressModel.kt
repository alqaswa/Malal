package com.example.malal.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressModel(
    val userUid: String = "",
    val addressTitle:String,
    val phone:String,
    val city:String,
    val state:String) :Parcelable

fun AddressModel.toMap(): Map<String, Any>
{
    val map = mutableMapOf<String, Any>()
    map["userUid"] = userUid
    map["addressTitle"] = addressTitle
    map["phone"] = phone
    map["city"] = city
    map["state"] = state
    return map
}

fun convertMapToAddressModel(map: Map<String, Any>): AddressModel
{
    return AddressModel(
            map["userUid"].toString(),
            map["addressTitle"].toString(),
            map["phone"].toString(),
            map["city"] .toString(),
            map["state"] .toString())
}