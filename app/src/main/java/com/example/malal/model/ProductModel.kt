package com.example.malal.model

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize
import javax.annotation.Nullable

@Entity(tableName = "productModel")
@Parcelize
data class ProductModel(
        val api_featured_image: String,
        val brand: String,

        val category: String?,
        val created_at: String,
        val currency: String,
        val description: String,
        @PrimaryKey
        val id: Int,
        val image_link: String,
        val name: String,
        val price: String,
        val price_sign: String,
        val product_api_url: String,
        val product_colors: List<ProductColor>,
        val product_link: String,
        val product_type: String,
        val rating: Double,
        val tag_list: List<String>,
        val updated_at: String,
        val website_link: String,
        var quantity:Int?=0):Parcelable
{
        override fun hashCode(): Int
        {
                var result = id.hashCode()
                if(price_sign.isEmpty())
                {
                        result = 31 * result + price_sign.hashCode()
                }
                return result
        }
}

fun convertArrayMapToProductModelList(map: ArrayList<Map<String, Any>>): List<ProductModel>
{
        val list = mutableListOf<ProductModel>()
        map.forEach {
                list.add(convertMapToProductModel(it))
        }
        return list
}

fun convertMapToProductModel(map: Map<String, Any>): ProductModel
{
        return ProductModel(
                map["api_featured_image"].toString(),
                map["brand"].toString(),
                map["category"].toString(),
                map["created_at"].toString(),
                map["currency"].toString(),
                map["description"].toString(),
                map["id"].toString().toInt(),
                map["image_link"].toString(),
                map["name"].toString(),
                map["price"].toString(),
                map["price_sign"].toString(),
                map["product_api_url"].toString(),
                convertArrayMapToProductColorList(map["product_colors"] as ArrayList<Map<String, Any>>),
                map["product_link"].toString(),
                map["product_type"].toString(),
                map["rating"].toString().toDouble(),
                map["tag_list"] as List<String>,
                map["updated_at"].toString(),
                map["website_link"].toString(),
                map["quantity"].toString().toInt()
                           )
}

fun convertArrayMapToProductColorList(map: ArrayList<Map<String, Any>>): List<ProductColor>
{
        val list = mutableListOf<ProductColor>()
        map.forEach {
                list.add(convertMapToProductColor(it))
        }
        return list
}

fun convertDocumentToProductList(document: List<DocumentSnapshot>): MutableList<ProductModel> {
        val list = mutableListOf<ProductModel>()
        document.forEach { map->
                list.add(ProductModel(
                        map["api_featured_image"].toString(),
                        map["brand"].toString(),
                        map["category"].toString(),
                        map["created_at"].toString(),
                        map["currency"].toString(),
                        map["description"].toString(),
                        map["id"].toString().toInt(),
                        map["image_link"].toString(),
                        map["name"].toString(),
                        map["price"].toString(),
                        map["price_sign"].toString(),
                        map["product_api_url"].toString(),
                        convertArrayMapToProductColorList(map["product_colors"] as ArrayList<Map<String, Any>>),
                        map["product_link"].toString(),
                        map["product_type"].toString(),
                        map["rating"].toString().toDouble(),
                        map["tag_list"] as List<String>,
                        map["updated_at"].toString(),
                        map["website_link"].toString(),
                        map["quantity"].toString().toInt()
                                     ))
        }
        return list
}
