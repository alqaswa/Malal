package com.example.malal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.malal.data.repository.ShopRepository
import com.example.malal.model.ProductModel
import com.example.malal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(private val shopRepository:ShopRepository):ViewModel()
{
    private val _productLiveData=MutableLiveData<Resource<List<ProductModel>?>>(Resource.Loading())
    val productLiveData:LiveData<Resource<List<ProductModel>?>>
        get()=_productLiveData


    private val _productByBrandLiveData=MutableLiveData<Resource<List<ProductModel>?>>(Resource.Loading())
    val productByBrandLiveData:LiveData<Resource<List<ProductModel>?>>
        get()=_productByBrandLiveData

    private val _productByCategoryLiveData=MutableLiveData<Resource<List<ProductModel>?>>(Resource.Loading())
    val productByCategoryLiveData:LiveData<Resource<List<ProductModel>?>>
        get()=_productByCategoryLiveData


    private val _cartProductsLiveData = MutableLiveData<Resource<Any>>()
    val cartProductsLiveData: LiveData<Resource<Any>> = _cartProductsLiveData

    fun setCartProductValue()
    {
        _cartProductsLiveData.value = Resource.Idle()
    }

    init
    {
        viewModelScope.launch (Dispatchers.IO){
          _productLiveData.postValue(shopRepository.getProducts())
        }
    }

    fun setByBrandLiveDataIdle()
    {
        _productByBrandLiveData.postValue(Resource.Idle())
    }

    fun favoriteLiveData(id: Int) :LiveData<ProductModel?>
    {
        return shopRepository.getProductFromFavoriteLiveData(id)
    }

    fun getProductByBrand(brand:String)
    {
        viewModelScope.launch (Dispatchers.IO){
            _productByBrandLiveData.postValue(shopRepository.getProductByBrand(brand))
        }
    }

    fun getProductByCategory(category:String)
    {
        viewModelScope.launch (Dispatchers.IO){
            _productByCategoryLiveData.postValue(shopRepository.getProductByCategory(category))
        }
    }

    fun saveProductInFavorites(productModel:ProductModel)
    {
        viewModelScope.launch(Dispatchers.IO) {
            shopRepository.saveOrRemoveProductFromFavorite(productModel)
        }
    }

    fun addProductToCart(productModel:ProductModel,isAddToCart:Boolean)
    {
        viewModelScope.launch(Dispatchers.IO) {
            _cartProductsLiveData.postValue(shopRepository.addProductsToCart(listOf(productModel), false,isAddToCart))
        }
    }
}