package com.example.malal.presentation.fragment.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable
import com.example.malal.R
import com.example.malal.databinding.FragmentFavoriteBinding
import com.example.malal.model.ProductModel
import com.example.malal.presentation.adapter.FavoriteAdapter
import com.example.malal.util.LOADING_ANNOTATION
import com.example.malal.util.Resource
import com.example.malal.util.extention.hide
import com.example.malal.util.extention.show
import com.example.malal.util.extention.showToast
import com.example.malal.viewmodel.FavoriteViewModel
import com.example.malal.viewmodel.ShopViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class FavoriteFragment:Fragment(), FavoriteAdapter.FavoriteProductListener
{
    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog:Dialog

    private lateinit var binding:FragmentFavoriteBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel>()


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        favoriteViewModel.getFavoriteProducts()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)
        binding.adapter = favoriteAdapter
        binding.fragment = this
        observeListener()
        return binding.root
    }

    private fun observeListener()
    {
        favoriteViewModel.favoriteProductsLiveData.observe(viewLifecycleOwner) {
            if(it.isEmpty())
            {
                showAnimation()
            }
            else
            {
                pauseAnimation()
                favoriteAdapter.addProducts(it, this)
            }
        }

        favoriteViewModel.cartProductsLiveData.observe(viewLifecycleOwner) {
            when(it)
            {
                is Resource.Success ->
                {
                    showToast(getString(R.string.savedToCart))
                    loadingDialog.hide()
                }
                is Resource.Error ->
                {
                    showToast(it.message.toString())
                    loadingDialog.hide()
                }
                is Resource.Loading ->
                {
                    loadingDialog.show()
                }

                is Resource.Idle ->
                {
                    loadingDialog.show()
                }
            }
        }
    }


    fun addAllToCart()
    {
        val favList = favoriteAdapter.getAllFavoriteProducts()
        favList.forEach{
            it.quantity=1
        }

        favoriteViewModel.addProductsToCart(favList)

    }

    override fun removeProductFromFavorite(productModel:ProductModel)
    {
        favoriteViewModel.removeProductFromFavourite(productModel)
        showToast("Removed from favorite")
    }

    override fun onFavProductClick(productModel: ProductModel, favProductImage: ImageView)
    {
        navigateToItemDetailsFragment(productModel, favProductImage)
    }

    private fun navigateToItemDetailsFragment(productModel: ProductModel, transitionImageView: ImageView)
    {
        val bundle=Bundle()
        bundle.putParcelable("productItem",productModel)

        val extras = FragmentNavigatorExtras(transitionImageView to productModel.api_featured_image)
        findNavController().navigate(R.id.itemDetailsFragment,bundle,null, navigatorExtras =extras)
    }

    private fun showAnimation()
    {
        binding.apply {
            animationViewFavPage.playAnimation()
            animationViewFavPage.repeatCount=LottieDrawable.INFINITE
            MyFavoriteText.hide()
            bottomCartLayout.hide()
            cartRecView.hide()
            emptyBagMsgLayout.show()
        }
    }

    private fun pauseAnimation()
    {
        binding.apply {
            animationViewFavPage.pauseAnimation()
            MyFavoriteText.show()
            bottomCartLayout.show()
            cartRecView.show()
            emptyBagMsgLayout.hide()
        }
    }

}


