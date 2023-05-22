package com.example.malal.util

import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.malal.R
import com.example.malal.model.ProductModel
import com.example.malal.presentation.adapter.ProductItemsAdapter
import com.example.malal.util.extention.hide
import com.example.malal.util.extention.loadGif
import com.example.malal.util.extention.loadImage
import com.example.malal.util.extention.show
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("product","isCoverRecyclerView","listener")
fun setProducts(
        rv:RecyclerView,
        products:MutableList<ProductModel>?,
        isCoverRecyclerView:Boolean,
        listener:ProductItemsAdapter.ProductListener
               ) {
    if (products != null && products.isNotEmpty()) {
        val productAdapter =
            ProductItemsAdapter(products,isCoverRecyclerView,listener)
        rv.adapter = productAdapter
        rv.show()
    } else {
        rv.hide()
    }
}

@BindingAdapter("quantityEditText", "increasePrice")
fun changeProductQuantityValue(imageView: ImageView, quantityEditText: EditText, increasePrice: Boolean)
{
    imageView.setOnClickListener {
        var quantity = quantityEditText.text.toString().trim().toInt()
        if (increasePrice)
        {
            quantity++
        } else if (quantity > 1)
        {
            quantity--
        }
        quantityEditText.setText(quantity.toString())
    }
}

@BindingAdapter("removeWhiteSpaces")
fun setPrice(textView:TextView,name:String)
{
    textView.text=name.trim()
}

@BindingAdapter("Rating")
fun setRating(ratingBar:RatingBar,boolean:Boolean)
{
    val random=(1..5).random()
    ratingBar.rating=random.toFloat()
}

@BindingAdapter("discount")
fun setDiscount(textView:TextView,boolean:Boolean)
{
    val discount=(10..80).random()
    textView.text="-$discount%"
}

@BindingAdapter("tags")
fun setTags(textView:TextView,tagsList:List<String>)
{
    for(tag in tagsList)
    {
        textView.append("$tag")
        if(tag.length>1)
        {
            textView.append(",")
        }
    }
}

@BindingAdapter("formatDate")
fun formatMilliSecondsDate(textView: TextView, milliseconds: Long){
    val df = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
    df.format(Date(milliseconds)).let {
        textView.text = it
    }
}

@BindingAdapter("imageBeautyImages")
fun setImage(imageView:ImageView, link: String) {
    imageView.loadGif(R.drawable.spinner)
    if(link.isNotEmpty())
        imageView.loadImage("https:$link")
}

@BindingAdapter("imageUrl")
fun loadImage(imageView:ImageView, link: String) {
    //imageView.loadGif(R.drawable.spinner)

    if(link != "null")
    {
        imageView.loadImage(link)
    }
    else
    {
        imageView.setImageResource(R.drawable.ic_profile)
    }
}