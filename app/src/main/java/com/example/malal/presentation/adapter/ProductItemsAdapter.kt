package com.example.malal.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.malal.BR
import com.example.malal.R
import com.example.malal.model.ProductModel
import com.example.malal.util.COVER_VIEW
import com.example.malal.util.SIMPLE_VIEW

class ProductItemsAdapter(
        private val productsList: List<ProductModel>,
        private val coverView:Boolean,
        private val listener: ProductListener):RecyclerView.Adapter<ProductItemsAdapter.MainProductViewHolder>()
{
    override fun getItemViewType(position:Int):Int
    {
        return if(coverView)
            COVER_VIEW
        else
            SIMPLE_VIEW
    }

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int):MainProductViewHolder
    {
        if(viewType== COVER_VIEW)
            return MainProductViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                    R.layout.cover_single,parent,false))
        return MainProductViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.single_product,parent,false))

    }

    override fun onBindViewHolder(holder:MainProductViewHolder, position:Int)
    {
        holder.bind(productsList[position])
       // holder.binding.executePendingBindings()
    }

    override fun getItemCount():Int
    {
        return productsList.size
    }

    inner class MainProductViewHolder(val binding:ViewDataBinding) :RecyclerView.ViewHolder(binding.root)
    {
        fun bind(productItem: ProductModel) = with(binding) {
            setVariable(BR.product,productItem)
            setVariable(BR.productListener,listener)
        }
    }

    interface ProductListener{
        fun onProductClick(productModel: ProductModel)
    }
}