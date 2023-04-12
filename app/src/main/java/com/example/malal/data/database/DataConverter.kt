package com.example.malal.data.database

import androidx.room.TypeConverter
import com.example.malal.model.ProductColor
import com.example.malal.model.ProductModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverter
{

    @TypeConverter
    fun fromProductColorList(value: List<ProductColor>): String {
        val gson = Gson()
        val type = object : TypeToken<List<ProductColor>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toProductColorList(value: String): List<ProductColor> {
        val gson = Gson()
        val type = object : TypeToken<List<ProductColor>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromTagList(value:List<String>):String
    {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toTagList(value:String):List<String>
    {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromProductModel(productModel:ProductModel):String
    {
        val gson = Gson()
        val type = object : TypeToken<ProductModel>() {}.type
        return gson.toJson(productModel, type)
    }

    @TypeConverter
    fun toProductModel(value:String):ProductModel
    {
        val gson = Gson()
        val type = object : TypeToken<ProductModel>() {}.type
        return gson.fromJson(value, type)
    }
}