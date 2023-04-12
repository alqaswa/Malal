package com.example.malal.presentation.fragment.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.malal.R
import com.example.malal.databinding.FragmentHelpBinding

class HelpFragment:Fragment()
{
    private lateinit var binding:FragmentHelpBinding
    override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?):View?
    {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_help,container,false)
        binding.fragment=this
        return binding.root
    }

     fun openCallApp()
     {
        Intent(Intent.ACTION_DIAL, Uri.parse("tel:123456789")).also {startActivity(it) }
    }

     fun openEmailApp()
     {
        Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("arshadahmed10@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "")
            putExtra(Intent.EXTRA_TEXT, "")
        }.also { startActivity(Intent.createChooser(it,"Send email with...?")) }
    }
}