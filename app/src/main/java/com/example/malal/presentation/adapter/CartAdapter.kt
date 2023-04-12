package com.example.malal.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.malal.BR
import com.example.malal.databinding.CartItemBinding
import com.example.malal.model.ProductModel
import javax.inject.Inject

class CartAdapter @Inject constructor():RecyclerView.Adapter<CartAdapter.CartViewHolder>()
{
    private val productModel = mutableListOf<ProductModel>()
    private val purchasedProductsList = mutableListOf<ProductModel>()
    private lateinit var cartListener:CartItemClickListener

    @SuppressLint("NotifyDataSetChanged")
    fun addProducts(list: List<ProductModel>,listener:CartItemClickListener)
    {
        cartListener=listener
        productModel.clear()
        purchasedProductsList.clear()
        productModel.addAll(list)
        purchasedProductsList.addAll(list)
        notifyDataSetChanged()
    }

    fun getPurchasedProducts() = purchasedProductsList

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int):CartViewHolder
    {
        return CartViewHolder(CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount():Int
    {
        return productModel.size
    }

    override fun onBindViewHolder(holder:CartViewHolder, position:Int)
    {
        holder.bind(productModel[position])
    }


    inner class CartViewHolder(private val binding:CartItemBinding) :RecyclerView.ViewHolder(binding.root)
    {

        fun bind(productModel:ProductModel) = with(binding) {
            setVariable(BR.productModel,productModel)
            setVariable(BR.cartItemListener,cartListener)
            setVariable(BR.adapter,this@CartAdapter)
        }
    }

    interface CartItemClickListener
    {
        fun onItemDeleteClick(productModel:ProductModel)
        fun changeProductQuantity(increasePrice: Boolean,quantityTextView:TextView,priceTextView:TextView,productModel:ProductModel)
    }
}