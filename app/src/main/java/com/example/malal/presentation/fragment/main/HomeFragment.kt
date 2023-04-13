package com.example.malal.presentation.fragment.main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.malal.R
import com.example.malal.databinding.FragmentHomeBinding
import com.example.malal.model.ProductModel
import com.example.malal.presentation.adapter.ProductItemsAdapter
import com.example.malal.util.LOADING_ANNOTATION
import com.example.malal.util.PRODUCT_JSON
import com.example.malal.viewmodel.ShopViewModel
import com.example.malal.util.Resource
import com.example.malal.util.extention.hide
import com.example.malal.util.extention.show
import com.example.malal.util.extention.showToast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home),ProductItemsAdapter.ProductListener
{

    private lateinit var binding: FragmentHomeBinding
    private val productModel:ShopViewModel by activityViewModels()

    private lateinit var offlineProductsList:ArrayList<ProductModel>

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog:Dialog

    override fun onCreate(savedInstanceState:Bundle?)
    {
        super.onCreate(savedInstanceState)
        offlineProductsList=arrayListOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        setCoverData()
        return binding.root
    }

    override fun onViewCreated(view:View, savedInstanceState:Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    private fun observeLiveData()
    {
        binding.productListener=this
        productModel.productLiveData.observe(viewLifecycleOwner) {
            when(it)
            {
                is Resource.Loading ->
                {
                    binding.productList=offlineProductsList
                }
                is Resource.Success ->
                {
                    showView()
                    binding.productList=it.data
                    loadingDialog.hide()
                }
                is Resource.Error ->
                {
                    loadingDialog.hide()
                    hideView()
                    showToast(it.message.toString())
                }
                else ->
                {
                    loadingDialog.hide()
                }
            }
        }

    }
    private fun hideView()
    {
        binding.MainScrollViewHomeFrag.hide()
    }
    private fun showView()
    {
        binding.MainScrollViewHomeFrag.show()
    }
    override fun onProductClick(productModel:ProductModel)
    {
        val bundle=Bundle()
        bundle.putParcelable("productItem",productModel)

        val navOption=NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .build()
        findNavController().navigate(R.id.itemDetailsFragment,bundle,navOption)
    }

    private fun getJsonData(context:Context, fileName: String): String? {

        val jsonString: String
        try
        {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        }
        catch(ioException:IOException)
        {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    private fun setCoverData()
    {

        val jsonFileString = context?.let {

            getJsonData(it, PRODUCT_JSON)
        }

        val gson = Gson()
        val offlineProducts = object : TypeToken<List<ProductModel>>() {}.type

        val coverD: List<ProductModel> = gson.fromJson(jsonFileString, offlineProducts)

        coverD.forEachIndexed { idx, products ->

            offlineProductsList.add(products)
            offlineProductsList.add(products)

        }

    }
}