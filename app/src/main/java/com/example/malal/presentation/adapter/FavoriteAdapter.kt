package com.example.malal.presentation.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.malal.R
import com.example.malal.BR
import com.example.malal.model.ProductModel
import javax.inject.Inject

class FavoriteAdapter @Inject constructor() : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>()
{

    private val productList = mutableListOf<ProductModel>()
    private val productSaveList=mutableListOf<ProductModel>()
    private lateinit var favListener: FavoriteProductListener
    @SuppressLint("NotifyDataSetChanged")
    fun addProducts(list: List<ProductModel>, listener: FavoriteProductListener)
    {
        favListener=listener
        productList.clear()
        productSaveList.clear()
        productList.addAll(list)
        productSaveList.addAll(list)
        notifyDataSetChanged()
    }

    fun getAllFavoriteProducts() = productSaveList

    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): FavoriteViewHolder
    {
       return FavoriteViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
               R.layout.favorite_item_rv_layout,
               parent, false))
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size

    inner class FavoriteViewHolder(private val binding:ViewDataBinding) :RecyclerView.ViewHolder(binding.root)
    {

        fun bind(productModel: ProductModel) = with(binding) {
            setVariable(BR.productModel, productModel)
            setVariable(BR.favListener, favListener)
            val isLastItem = adapterPosition == productList.size - 1
            setVariable(BR.isLastItem, isLastItem)
        }
    }

    interface FavoriteProductListener
    {
        fun onFavProductClick(productModel: ProductModel, favProductImage:ImageView)
        fun removeProductFromFavorite(productModel: ProductModel)
    }

}