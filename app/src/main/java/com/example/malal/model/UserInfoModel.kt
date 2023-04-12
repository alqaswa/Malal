package com.example.malal.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.net.InetSocketAddress

@Parcelize
data class UserInfoModel(
        val userUid: String = "",
        val userName: String,
        val userImage: String,
        val userEmail: String,
        var userAddress:String="") : Parcelable
{

        fun toMapWithoutImage(): MutableMap<String, Any>
        {
                val map = mutableMapOf<String, Any>()
                map["userUid"] = userUid
                map["userName"] = userName
                map["userEmail"] = userEmail
                map["userAddress"]=userAddress
                return map
        }
}

fun UserInfoModel.toMap(): Map<String, Any>
{
        val map = mutableMapOf<String, Any>()
        map["userUid"] = userUid
        map["userName"] = userName
        map["userImage"] = userImage
        map["userEmail"] = userEmail
        map["userAddress"]=userAddress
        return map
}

fun convertMapToUserInfoModel(map: Map<String, Any>): UserInfoModel
{
        return UserInfoModel(
                map["userUid"].toString(),
                map["userName"].toString(),
                map["userImage"].toString(),
                map["userEmail"].toString(),
                map["userAddress"].toString()
                            )
}
