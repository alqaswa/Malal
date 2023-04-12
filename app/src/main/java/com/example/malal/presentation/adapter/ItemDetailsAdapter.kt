package com.example.malal.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.malal.BR
import com.example.malal.R
import com.example.malal.databinding.SimiliarProductBinding
import com.example.malal.model.ProductModel

class ItemDetailsAdapter(private val listener:ItemDetailListener):RecyclerView.Adapter<ItemDetailsAdapter.MainItemViewHolder>()
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


    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int):MainItemViewHolder
    {
        return MainItemViewHolder(SimiliarProductBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount():Int
    {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder:MainItemViewHolder, position:Int)
    {

        holder.binding.product=differ.currentList[position]
        holder.binding.itemDetailListener=listener
    }

    inner class MainItemViewHolder(val binding:SimiliarProductBinding) :RecyclerView.ViewHolder(binding.root)

    interface ItemDetailListener{
        fun onProductClick(productModel: ProductModel)
    }
}