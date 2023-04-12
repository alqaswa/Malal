package com.example.malal.presentation.fragment.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.malal.R
import com.example.malal.databinding.FragmentAddressBinding
import com.example.malal.model.AddressModel
import com.example.malal.model.UserInfoModel
import com.example.malal.util.LOADING_ANNOTATION
import com.example.malal.util.Resource
import com.example.malal.util.extention.closeFragment
import com.example.malal.util.extention.hide
import com.example.malal.util.extention.show
import com.example.malal.util.extention.showToast
import com.example.malal.viewmodel.AuthenticationViewModel
import com.example.malal.viewmodel.UserInfoViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class AddressFragment:Fragment()
{

    private val userInfoViewModel by activityViewModels<UserInfoViewModel>()
    private val authViewModel by activityViewModels<AuthenticationViewModel>()
    private lateinit var binding:FragmentAddressBinding

    private var addressModel: AddressModel? = null


    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog:Dialog

    override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?):View?
    {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_address,container,false)
        binding.fragment=this
        return binding.root
    }

    override fun onViewCreated(view:View, savedInstanceState:Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
    }

    @SuppressLint("SetTextI18n")
    private fun observeListener()
    {
        userInfoViewModel.userAddressLiveData.observe(viewLifecycleOwner) {
            when(it)
            {
                is Resource.Success ->
                {
                    loadingDialog.hide()
                    addressModel=it.data
                    if(addressModel!=null)
                    {
                        binding.currentAddressCv.show()
                        binding.currentAddress.text="${addressModel?.addressTitle} \n${addressModel?.city} \n${addressModel?.state} \n${addressModel?.phone}"
                    }


                }
                is Resource.Error ->
                {
                    loadingDialog.hide()
                    showToast(it.message.toString())
                }
                is Resource.Loading ->
                {
                    loadingDialog.show()
                }

                is Resource.Idle ->
                {
                    loadingDialog.hide()
                }
            }
        }
    }

    fun showAddressInputs()
    {
        binding.apply {
            address.show()
            llContainer.show()
            cancel.show()
            fab.hide()
            currentAddressCv.hide()
        }
    }

    fun submitAddress()
    {
        val addressTitle=binding.addressEt.text.toString().trim()
        val phone=binding.phoneEt.text.toString().trim()
        val city=binding.cityEt.text.toString().trim()
        val state=binding.stateEt.text.toString().trim()

        authViewModel.uploadUserAddress(addressTitle,phone,city,state)
        cancel()
    }

    fun cancel()
    {
        binding.apply {
            address.hide()
            llContainer.hide()
            cancel.hide()
            fab.show()
            currentAddressCv.show()
        }
    }


}