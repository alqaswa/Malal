package com.example.malal.presentation.fragment.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable
import com.example.malal.util.Resource
import com.example.malal.R
import com.example.malal.databinding.FragmentCartBinding
import com.example.malal.model.ProductModel
import com.example.malal.presentation.adapter.CartAdapter
import com.example.malal.util.LOADING_ANNOTATION
import com.example.malal.util.extention.hide
import com.example.malal.util.extention.show
import com.example.malal.util.extention.showToast
import com.example.malal.viewmodel.CartViewModel
import com.example.malal.viewmodel.ShopViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class CartFragment:Fragment(),CartAdapter.CartItemClickListener
{
    private lateinit var binding:FragmentCartBinding
    private val cartViewModel by viewModels<CartViewModel>()
    private val shopViewModel by viewModels<ShopViewModel>()

    private val cartProductsList = mutableListOf<ProductModel>()

    @Inject
    lateinit var cartAdapter:CartAdapter

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog:Dialog

    override fun onCreate(savedInstanceState:Bundle?)
    {
        super.onCreate(savedInstanceState)
        cartViewModel.getAllCartProducts()
    }

    override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?):View?
    {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_cart,container,false)
        showAnimation()
        return binding.run {
            fragment=this@CartFragment
            adapter=cartAdapter
            root
        }
    }

    override fun onViewCreated(view:View, savedInstanceState:Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()

    }

    private fun observeLiveData()
    {
        cartViewModel.cartProductsLiveData.observe(viewLifecycleOwner){List->
            when(List)
            {
                is Resource.Success ->
                {
                    loadingDialog.hide()
                    if((List.data == null) || List.data.isEmpty())
                    {
                        showAnimation()
                        binding.cartRecView.hide()
                    }
                    else
                    {
                        hideAnimation()
                        cartAdapter.addProducts(List.data,this)
                        binding.totalPriceBagFrag.text=getTotalPrice()
                    }

                }

                is Resource.Error ->
                {
                    loadingDialog.hide()
                }

                is Resource.Loading ->
                {
                    loadingDialog.show()
                }

                is Resource.Idle ->
                {
                    loadingDialog.hide()
                }
            }
        }
    }

    private fun showAnimation()
    {
        binding.apply {
            animationViewCartPage.playAnimation()
            animationViewCartPage.repeatCount=LottieDrawable.INFINITE
            bottomCartLayout.hide()
            MybagText.hide()
            emptyBagMsgLayout.show()
        }
    }

    private fun hideAnimation()
    {
        binding.apply {
            animationViewCartPage.pauseAnimation()
            emptyBagMsgLayout.hide()
            MybagText.show()
            bottomCartLayout.show()

        }
    }

    fun checkOutProduct()
    {
        openCheckOutDialog(getTotalPriceInFloat())
    }

    private fun openCheckOutDialog(totalPrice: Float)
    {
        cartProductsList.clear()
        cartProductsList.addAll(cartAdapter.getPurchasedProducts())
        if (cartProductsList.isEmpty())
        {
            showToast(getString(R.string.noProductsCart))
            return
        }
        val action = CartFragmentDirections.actionCartFragmentToCheckoutFragment(totalPrice, cartProductsList.toTypedArray())
        findNavController().navigate(action)
    }


    private fun getTotalPrice(): String
    {
        var totalPrice = 0.0
        cartAdapter.getPurchasedProducts().forEach {
            totalPrice+=(it.quantity?.times(it.price.toDouble())!!)
        }

        val price=String.format("%.2f", totalPrice)
        return "$$price"
    }

    private fun getTotalPriceInFloat():Float
    {
        var totalPrice = 0.0
        cartAdapter.getPurchasedProducts().forEach {
            totalPrice+=(it.quantity?.times(it.price.toDouble())!!)
        }
        return totalPrice.toFloat()
    }

    @SuppressLint("SetTextI18n")
    override fun changeProductQuantity(increasePrice: Boolean,quantityTextView:TextView,priceTextView:TextView,productModel:ProductModel)
    {

        var quantity = quantityTextView.text.toString().trim().toInt()

        if (increasePrice)
        {
            quantity++
        }
        else if (quantity > 1)
        {
            quantity--
        }

        val itemPrice=(productModel.quantity?.times(productModel.price.toDouble())!!)
        val price=String.format("%.2f", itemPrice)

        priceTextView.text="$$price"
        quantityTextView.text=quantity.toString()

        productModel.quantity=quantity
        shopViewModel.addProductToCart(productModel,false)

        binding.totalPriceBagFrag.text=getTotalPrice()
    }



    override fun onItemDeleteClick(productModel:ProductModel)
    {
        cartViewModel.deleteProductFromCart(productModel)
        showToast("Removed from cart")
    }

    fun removeAllProductFromCart()
    {
        cartViewModel.removeAllProductFromCart()
    }


}