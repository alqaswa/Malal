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
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val shopRepository:ShopRepository):ViewModel()
{
    private val _cartProductsLiveData = MutableLiveData<Resource<List<ProductModel>>>()
    val cartProductsLiveData: LiveData<Resource<List<ProductModel>>> = _cartProductsLiveData

    fun deleteProductFromCart(productModel:ProductModel){
        viewModelScope.launch(Dispatchers.IO) {
            shopRepository.deleteProductFromUserCart(productModel.id.toString())
            withContext(Dispatchers.Main){
                getAllCartProducts()
            }
        }
    }

    fun removeAllProductFromCart()
    {
        viewModelScope.launch(Dispatchers.IO) {
            shopRepository.removeUserCartProducts()
        }
    }

    fun getAllCartProducts() {
        _cartProductsLiveData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _cartProductsLiveData.postValue(
                    shopRepository.getAllUserProducts())
        }
    }


}