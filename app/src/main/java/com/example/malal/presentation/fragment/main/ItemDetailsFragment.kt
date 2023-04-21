package com.example.malal.presentation.fragment.main

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.malal.util.Resource
import com.example.malal.R
import com.example.malal.databinding.FragmentItemDetailsBinding
import com.example.malal.model.ProductModel
import com.example.malal.presentation.adapter.ItemDetailsAdapter
import com.example.malal.util.LOADING_ANNOTATION
import com.example.malal.util.PRODUCT_MODEL
import com.example.malal.util.extention.loadTimerGif
import com.example.malal.util.extention.showToast
import com.example.malal.viewmodel.ShopViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ItemDetailsFragment:Fragment(),ItemDetailsAdapter.ItemDetailListener
{
    private lateinit var binding:FragmentItemDetailsBinding
    private lateinit var itemDetailsAdapter:ItemDetailsAdapter
    private val shopViewModel by activityViewModels<ShopViewModel>()
    private lateinit var productModel:ProductModel



    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog:Dialog

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState:Bundle?)
    {
        super.onCreate(savedInstanceState)

        productModel=requireArguments().getParcelable(PRODUCT_MODEL)!!
        shopViewModel.getProductByBrand(productModel.brand)

        sharedElementEnterTransition =TransitionInflater.from(context).inflateTransition(android.R.transition.move)

    }

    override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?):View?
    {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_item_details,container,false)
        binding.productItem=productModel
        binding.fragment=this

        return binding.root
    }

    override fun onViewCreated(view:View, savedInstanceState:Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.favoriteProductImageView.transitionName=productModel.api_featured_image
        setRecyclerView()
        observeLiveData()

    }

    private fun observeLiveData()
    {
        shopViewModel.productByBrandLiveData.observe(viewLifecycleOwner) {

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
                    itemDetailsAdapter.differ.submitList(it.data)
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

        shopViewModel.favoriteLiveData(productModel.id).observe(viewLifecycleOwner) {product ->
            if(product != null)
            {
                binding.favoriteProductImageView.setImageResource(R.drawable.ic_fav_added)
            }
            else
            {
                binding.favoriteProductImageView.setImageResource(R.drawable.ic_fav)
            }
        }


    }

    fun saveProductInFavorite()
    {
        shopViewModel.saveProductInFavorites(productModel)
    }

    private fun setRecyclerView()
    {
        itemDetailsAdapter=ItemDetailsAdapter(this)
        binding.itemDetailsRv.adapter=itemDetailsAdapter

    }

    override fun onProductClick(productModel:ProductModel)
    {
        val bundle=Bundle()
        bundle.putParcelable(PRODUCT_MODEL,productModel)

        val navOption=NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .build()
        findNavController().navigate(R.id.itemDetailsFragment,bundle,navOption)
    }

    fun openBottomDialog(productModel:ProductModel)
    {
        val action=ItemDetailsFragmentDirections.actionItemDetailsFragmentToBottomSheetDialogFragment(productModel)
        findNavController().navigate(action)
    }



}