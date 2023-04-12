package com.example.malal.presentation.fragment.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.malal.R
import com.example.malal.databinding.FragmentExploreBinding
import com.example.malal.model.Categories
import com.example.malal.presentation.adapter.ExploreAdapter
import com.example.malal.util.MyTag
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment:Fragment(),ExploreAdapter.ExploreListener
{
    private lateinit var binding:FragmentExploreBinding
    private lateinit var cateList:ArrayList<Categories>
    private lateinit var exploreAdapter:ExploreAdapter

    override fun onCreate(savedInstanceState:Bundle?)
    {
        super.onCreate(savedInstanceState)
        cateList=arrayListOf()
    }

    override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?):View?
    {
        setCategoryData()
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_explore,container,false)
        return binding.root
    }

    override fun onViewCreated(view:View, savedInstanceState:Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
    }

    private fun setRecyclerView()
    {
        exploreAdapter=ExploreAdapter(cateList,this)
        binding.categoriesRecView.adapter=exploreAdapter
    }
    private fun setCategoryData()
    {
        cateList.add(Categories("Blush","https://s3.amazonaws.com/donovanbailey/products/api_featured_images/000/001/035/original/open-uri20180630-4-n6wb0y?1530390383"))
        cateList.add(Categories("Bronzer","https://s3.amazonaws.com/donovanbailey/products/api_featured_images/000/001/032/original/open-uri20180630-4-1bl3btv?1530390381"))
        cateList.add(Categories("Eyebrow","https://s3.amazonaws.com/donovanbailey/products/api_featured_images/000/000/977/original/open-uri20171224-4-ylht42?1514082762"))
        cateList.add(Categories("Eyeliner","https://s3.amazonaws.com/donovanbailey/products/api_featured_images/000/000/956/original/open-uri20171224-4-1sgu8lk?1514082713"))
        cateList.add(Categories("Eyeshadow","https://s3.amazonaws.com/donovanbailey/products/api_featured_images/000/001/014/original/open-uri20180630-4-1y604g6?1530390368"))
        cateList.add(Categories("Foundation","https://s3.amazonaws.com/donovanbailey/products/api_featured_images/000/001/045/original/open-uri20180708-4-4bvqii?1531074237"))
        cateList.add(Categories("Lip Liner","https://s3.amazonaws.com/donovanbailey/products/api_featured_images/000/001/048/original/open-uri20180708-4-13okqci?1531093614"))
        cateList.add(Categories("Lipstick","https://cdn.shopify.com/s/files/1/1338/0845/products/brain-freeze_a_800x1200.jpg?v=1502255076"))
        cateList.add(Categories("Mascara","https://s3.amazonaws.com/donovanbailey/products/api_featured_images/000/001/025/original/open-uri20180630-4-yxmmga?1530390376"))
        cateList.add(Categories("Nail Polish","https://s3.amazonaws.com/donovanbailey/products/api_featured_images/000/000/730/original/data?1514062740"))
    }

    override fun onProductClick(categoryName:String)
    {
        val action=ExploreFragmentDirections.actionExploreFragmentToProductCategoryFragment(categoryName)
        findNavController().navigate(action)
    }
}