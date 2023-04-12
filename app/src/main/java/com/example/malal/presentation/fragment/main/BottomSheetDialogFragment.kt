package com.example.malal.presentation.fragment.main

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.malal.R
import com.example.malal.databinding.FragmentBottomSheetDialogBinding
import com.example.malal.model.ProductModel
import com.example.malal.util.extention.closeFragment
import com.example.malal.util.extention.showToast
import com.example.malal.viewmodel.ShopViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetDialogFragment:BottomSheetDialogFragment()
{
    private lateinit var binding:FragmentBottomSheetDialogBinding
    private val args by navArgs<BottomSheetDialogFragmentArgs>()
    private val productItems by lazy {args.productItem}
    private val shopViewModel by activityViewModels<ShopViewModel> ()

    private var productQuantity = 1

    override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?):View?
    {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_bottom_sheet_dialog,container,false)
        binding.fragment=this
        return binding.root
    }

    //Increase or decrease quantity value .
    fun changeProductQuantity(increasePrice: Boolean)
    {
        var quantity = binding.productQuantityEt.text.toString().trim().toInt()
        if (increasePrice) {
            quantity++
        } else if (quantity > 1) {
            quantity--
        }
        binding.productQuantityEt.setText(quantity.toString())
    }

    //Change product quantity with what user last saved.
    fun addProductToCart()
    {
        val productTemp = createTempProductWithNewQuantity()
        if (productTemp != null)
        {
            shopViewModel.addProductToCart(productTemp,true)
        }
        showToast("Added to cart")
        closeFragment()

    }

    private fun createTempProductWithNewQuantity(): ProductModel?
    {
        val quantity = binding.productQuantityEt.text.toString().trim().toInt()
        return if (TextUtils.isDigitsOnly(quantity.toString()))
        {
            productItems.copy().let { temp ->
                productQuantity=quantity
                temp.quantity = productQuantity
                temp
            }
        }
        else
        {
            binding.productQuantityEt.setError(getString(R.string.productQuantityError), null)
            null
        }
    }


}