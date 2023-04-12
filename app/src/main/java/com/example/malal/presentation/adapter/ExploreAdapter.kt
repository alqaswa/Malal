package com.example.malal.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.malal.BR
import androidx.recyclerview.widget.RecyclerView
import com.example.malal.databinding.ExploreRv1Binding
import com.example.malal.model.Categories
import com.example.malal.model.ProductModel

class ExploreAdapter(
    private val categoryList: ArrayList<Categories>,
    private val exploreListener:ExploreListener):RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder>()
{

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int):ExploreViewHolder
    {
        return ExploreViewHolder(ExploreRv1Binding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount():Int
    {
        return categoryList.size
    }

    override fun onBindViewHolder(holder:ExploreViewHolder, position:Int)
    {
        holder.bind(categoryList[position])
    }

    inner class ExploreViewHolder(val binding:ExploreRv1Binding) :RecyclerView.ViewHolder(binding.root)
    {
        fun bind(categories:Categories)
        {
            binding.apply {
                setVariable(BR.category,categories)
                setVariable(BR.listener,exploreListener)
            }
        }
    }

    interface ExploreListener
    {
        fun onProductClick(categoryName:String)
    }
}