package com.example.malal.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.malal.R
import com.example.malal.BR
import com.example.malal.model.OrderModel

class OrderPlacedAdapter(private val orderList: List<OrderModel>): RecyclerView.Adapter<OrderPlacedAdapter.OrderPlacedViewHolder>()
{


    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): OrderPlacedViewHolder
    {
        return OrderPlacedViewHolder(DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.order_item_rv_layout,
                        parent, false))
    }


    override fun onBindViewHolder(holder: OrderPlacedViewHolder, position: Int)
    {
        holder.bind(orderList[position])
    }

    override fun getItemCount(): Int = orderList.size

    inner class OrderPlacedViewHolder(private val binding:ViewDataBinding) :RecyclerView.ViewHolder(binding.root)
    {

        fun bind(orderModel: OrderModel)
        {
            binding.setVariable(BR.order, orderModel)
        }
    }

}