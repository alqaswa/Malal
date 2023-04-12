package com.example.malal.data.networking

import com.example.malal.model.ProductModel
import com.example.malal.util.END_POINT
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MakeUpApi
{
    @GET(END_POINT)
    suspend fun getProduct():Response<List<ProductModel>>

    @GET(END_POINT)
    suspend fun getProductByBrand(@Query("brand") brand:String):Response<List<ProductModel>>

    @GET(END_POINT)
    suspend fun getProductByCategory(@Query("product_type") category:String):Response<List<ProductModel>>
}