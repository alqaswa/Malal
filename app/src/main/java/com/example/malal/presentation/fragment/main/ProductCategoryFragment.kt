package com.example.malal.presentation.fragment.main

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.malal.R
import com.example.malal.databinding.FragmentProductCategoryBinding
import com.example.malal.model.ProductModel
import com.example.malal.presentation.adapter.ItemDetailsAdapter
import com.example.malal.presentation.adapter.ProductCategoryAdapter
import com.example.malal.presentation.adapter.ProductItemsAdapter
import com.example.malal.util.LOADING_ANNOTATION
import com.example.malal.util.Resource
import com.example.malal.viewmodel.ShopViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ProductCategoryFragment:Fragment(),ProductItemsAdapter.ProductListener
{
    private lateinit var binding:FragmentProductCategoryBinding
    private lateinit var categoryAdapter:ProductCategoryAdapter

    private val args by navArgs<ProductCategoryFragmentArgs>()
    private val categoryName by lazy { args.categoryName }

    private val shopViewModel by viewModels<ShopViewModel>()

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog:Dialog

    override fun onCreate(savedInstanceState:Bundle?)
    {
        super.onCreate(savedInstanceState)
        var cName=categoryName
        if(cName=="Lip Liner")
        {
            cName="lip_liner"
        }
        else if(cName=="Nail Polish")
        {
            cName="nail_polish"
        }
        shopViewModel.getProductByCategory(cName)
    }

    override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?):View?
    {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_product_category,container,false)
        setRecyclerView()
        return binding.run {
            cName=categoryName
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
        shopViewModel.productByCategoryLiveData.observe(viewLifecycleOwner) {
            when(it)
            {
                is Resource.Loading ->
                {
                    loadingDialog.show()
                    return@observe
                }
                is Resource.Success ->
                {
                    loadingDialog.hide()
                    categoryAdapter.differ.submitList(it.data)
                    return@observe
                }
                is Resource.Error ->
                {
                    loadingDialog.hide()
                    return@observe
                }

                is Resource.Idle ->
                {
                    loadingDialog.hide()
                    return@observe
                }
            }
        }
    }

    private fun setRecyclerView()
    {
        categoryAdapter=ProductCategoryAdapter(this)
        binding.categoryRecView.adapter=categoryAdapter

    }

    override fun onProductClick(productModel:ProductModel)
    {
        val bundle=Bundle()
        bundle.putParcelable("productItem",productModel)
        findNavController().navigate(R.id.itemDetailsFragment,bundle)
    }
}