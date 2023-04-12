package com.example.malal.data.repository

import androidx.lifecycle.LiveData
import com.example.malal.data.database.FavoriteDao
import com.example.malal.data.networking.MakeUpApi
import com.example.malal.model.*
import com.example.malal.util.CART_COLLECTION
import com.example.malal.util.INCOMPLETE_ORDERS
import com.example.malal.util.Resource
import com.example.malal.util.USERS_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShopRepository @Inject constructor(
    private val makeUpApi:MakeUpApi,
    private val fireStore:FirebaseFirestore,
    private val firebaseAuth:FirebaseAuth,
    private val favoriteDao:FavoriteDao)
{

    private val userUid by lazy { firebaseAuth.uid!! }
    private val cartCollection by lazy {fireStore.collection(USERS_COLLECTION).document(userUid).collection(CART_COLLECTION)}

    suspend fun getProducts():Resource<List<ProductModel>?>
    {
       return try
        {
            val result=makeUpApi.getProduct()
            Resource.Success(result.body())
        }
        catch(e:Exception)
        {
            Resource.Error(e.message.toString())
        }
    }

    suspend fun getProductByBrand(brand:String):Resource<List<ProductModel>?>
    {
        return try
        {
            val result=makeUpApi.getProductByBrand(brand)
            Resource.Success(result.body())
        }
        catch(e:Exception)
        {
            Resource.Error(e.message.toString())
        }
    }

    suspend fun getProductByCategory(category:String):Resource<List<ProductModel>?>
    {
        return try
        {
            val result=makeUpApi.getProductByCategory(category)
            Resource.Success(result.body())
        }
        catch(e:Exception)
        {
            Resource.Error(e.message.toString())
        }
    }


    //Database
    suspend fun saveOrRemoveProductFromFavorite(productModel:ProductModel) {
        val isSavedBefore = getProductFromFavorite(productModel.id)
        return if (isSavedBefore)
        {
            favoriteDao.removeProductFromFavorites(productModel)
        }
        else
        {
            favoriteDao.saveProduct(productModel)
        }
    }

    suspend fun removeProductFromFavorite(productModel:ProductModel)
    {
            favoriteDao.removeProductFromFavorites(productModel)
    }

    // check if product is saved into favorite database or not .
    private suspend fun getProductFromFavorite(id: Int): Boolean
    {
        val productModel = favoriteDao.getSpecificFavoriteProduct(id)
        return productModel != null
    }

    fun getProductFromFavoriteLiveData(id: Int): LiveData<ProductModel?> =
        favoriteDao.getSpecificFavoriteProductLiveData(id)

    fun getFavoriteProductsLiveData(): LiveData<List<ProductModel>> =
        favoriteDao.getAllFavoriteProducts()


    // save products to user cart to buy it anytime.
    suspend fun addProductsToCart(list: List<ProductModel>, deleteFavoriteProducts: Boolean,isAddToCart:Boolean): Resource<Any>
    {
        return try
        {
            val cartProductsList = getAllUserProducts().data
            list.forEach { productModel ->
                if(isAddToCart)
                {
                    if (cartProductsList != null && cartProductsList.any {it.id == productModel.id })
                    {
                        val selectedProduct = cartProductsList.last {
                            it.id == productModel.id }
                            productModel.quantity =selectedProduct.quantity?.let {productModel.quantity?.plus(it)}
                    }
                }
                cartCollection.document(productModel.id.toString()).set(productModel).await()
            }
            if (deleteFavoriteProducts)
                favoriteDao.deleteAllProducts()
            Resource.Success(Any())
        }
        catch (e: Exception)
        {
            Resource.Error(e.toString())
        }

    }

    // delete specific product from user cart.
    suspend fun deleteProductFromUserCart(productId: String) {
        cartCollection.document(productId).delete().await()
    }

    // get all products from user cart to show into cart fragment.
    suspend fun getAllUserProducts(): Resource<List<ProductModel>>
    {
        return try
        {

            val result = cartCollection.get().await()
            val products = convertDocumentToProductList(result.documents)
            Resource.Success(products)
        }
        catch (e: Exception)
        {
            Resource.Error(e.toString())
        }
    }

    /* after submit the order successfully here we'll add all cart products to incomplete orders to show to admin panel
       and delete it from user cart.*/
    suspend fun uploadProductsToOrders(cartProductsList: Array<ProductModel>, userLocation: String, totalCost: Float): Resource<OrderModel> {
        return try
        {
            val orderCollection = fireStore.collection(INCOMPLETE_ORDERS)
            val id = orderCollection.document().id
            val orderModel = OrderModel(id, userUid, System.currentTimeMillis(), userLocation, OrderEnums.PLACED, totalCost, cartProductsList.toList())
            orderCollection.document(id).set(orderModel.toMap()).await()
            removeUserCartProducts()
            Resource.Success(orderModel)
        }
        catch (e: Exception)
        {
            Resource.Error(e.toString())
        }
    }

    // delete all products from user cart .
     suspend fun removeUserCartProducts() {
        cartCollection.get().await().let {
            it.forEach { doc ->
                cartCollection.document(doc.id).delete().await()
            }
        }
    }

    suspend fun getUserOrders(): Resource<List<OrderModel>>
    {
        return try
        {
            val result = fireStore.collection(INCOMPLETE_ORDERS).whereEqualTo("userUid", userUid).get().await()
            val orders = convertDocumentsToOrderList(result.documents)
            Resource.Success(orders)
        }
        catch (e: Exception)
        {
            println(">>>>>>>>>>>>>> ${e.message}")
            Resource.Error(e.toString())
        }
    }
}