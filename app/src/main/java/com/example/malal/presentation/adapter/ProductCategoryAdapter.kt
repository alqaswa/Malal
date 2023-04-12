package com.example.malal.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.malal.BR
import com.example.malal.databinding.SingleProductBinding
import com.example.malal.model.Categories
import com.example.malal.model.ProductModel

class ProductCategoryAdapter(
    private val listener:ProductItemsAdapter.ProductListener):RecyclerView.Adapter<ProductCategoryAdapter.ProductCategoryViewHolder>()
{

    private val diffCallback=object : DiffUtil.ItemCallback<ProductModel>()
    {
        override fun areItemsTheSame(oldItem:ProductModel, newItem:ProductModel):Boolean
        {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem:ProductModel, newItem:ProductModel):Boolean
        {
            return oldItem==newItem
        }
    }

    val differ=AsyncListDiffer(this,diffCallback)


    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int):ProductCategoryViewHolder
    {
        return ProductCategoryViewHolder(SingleProductBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount():Int
    {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder:ProductCategoryViewHolder, position:Int)
    {
        holder.bind(differ.currentList[position])
    }

    inner class ProductCategoryViewHolder(val binding:SingleProductBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(productModel:ProductModel)
        {
            binding.apply {
                setVariable(BR.product,productModel)
                setVariable(BR.productListener,listener)
            }
        }
    }
}