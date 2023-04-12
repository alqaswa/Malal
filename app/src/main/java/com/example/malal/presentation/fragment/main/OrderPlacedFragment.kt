package com.example.malal.presentation.fragment.main

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable
import com.example.malal.R
import com.example.malal.databinding.FragmentOrderPlacedBinding
import com.example.malal.model.OrderModel
import com.example.malal.presentation.adapter.OrderPlacedAdapter
import com.example.malal.util.LOADING_ANNOTATION
import com.example.malal.util.Resource
import com.example.malal.util.extention.closeFragment
import com.example.malal.util.extention.hide
import com.example.malal.util.extention.show
import com.example.malal.util.extention.showToast
import com.example.malal.viewmodel.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class OrderPlacedFragment:Fragment()
{
    private lateinit var binding: FragmentOrderPlacedBinding
    private val ordersViewModel by viewModels<OrdersViewModel>()
    private lateinit var orderPlacedAdapter: OrderPlacedAdapter

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog:Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
                             ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_placed, container, false)
        return binding.run {
            fragment = this@OrderPlacedFragment
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
    }

    private fun observeListener()
    {
        ordersViewModel.userOrdersLiveData.observe(viewLifecycleOwner)
        {
            when(it)
            {
                is Resource.Loading ->
                {
                    loadingDialog.show()
                }
                is Resource.Success ->
                {
                    if(it.data != null && it.data.isNotEmpty())
                    {
                        showRecyclerView(it.data)
                    }
                    else
                    {
                        hideRecyclerView()
                    }
                    loadingDialog.hide()
                }
                is Resource.Error ->
                {
                    showToast(it.message!!)
                    hideRecyclerView()
                    loadingDialog.hide()
                }
                is Resource.Idle ->
                {
                    loadingDialog.hide()
                }
            }
        }
    }

    private fun hideRecyclerView() {
        binding.let {
            it.ordersRecView.hide()
            it.emptyOrderMsgLayout.show()
            it.animationViewOrderPage.playAnimation()
            it.animationViewOrderPage.repeatCount=LottieDrawable.INFINITE

        }
    }

    private fun showRecyclerView(data: List<OrderModel>)
    {
        orderPlacedAdapter = OrderPlacedAdapter(data.sortedBy { it.orderSubmittedTime })
        binding.let {
            it.ordersRecView.adapter = orderPlacedAdapter
            it.emptyOrderMsgLayout.hide()
            it.ordersRecView.show()
        }
    }

}