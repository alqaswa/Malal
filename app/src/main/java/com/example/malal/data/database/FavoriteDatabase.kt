package com.example.malal.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.malal.model.ProductModel


@Database(entities = [ProductModel::class], version = 1)
@TypeConverters(DataConverter::class)

abstract class FavoriteDatabase:RoomDatabase()
{
    abstract fun getDao():FavoriteDao
}