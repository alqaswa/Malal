package com.example.malal.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.malal.model.ProductModel

@Dao
interface FavoriteDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProduct(productModel:ProductModel)

    @Query("SELECT * FROM productModel")
    fun getAllFavoriteProducts():LiveData<List<ProductModel>>

    @Query("SELECT * FROM productModel WHERE id =:id")
    suspend fun getSpecificFavoriteProduct(id: Int): ProductModel?

    @Query("SELECT * FROM productModel WHERE id =:id")
    fun getSpecificFavoriteProductLiveData(id: Int): LiveData<ProductModel?>

    @Delete
    suspend fun removeProductFromFavorites(quantity:ProductModel)

    @Query("DELETE FROM productModel")
    suspend fun deleteAllProducts()


}