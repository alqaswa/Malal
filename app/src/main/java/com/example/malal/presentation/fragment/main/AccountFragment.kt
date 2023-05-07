package com.example.malal.presentation.fragment.main

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.malal.R
import com.example.malal.databinding.FragmentAccountBinding
import com.example.malal.model.UserInfoModel
import com.example.malal.presentation.fragment.main.AccountFragmentDirections
import com.example.malal.util.LOADING_ANNOTATION
import com.example.malal.util.Resource
import com.example.malal.util.extention.showToast
import com.example.malal.viewmodel.AuthenticationViewModel
import com.example.malal.viewmodel.UserInfoViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class AccountFragment:Fragment()
{
    private lateinit var binding:FragmentAccountBinding
    private val userInfoViewModel by viewModels<UserInfoViewModel>()
    private var userInfoModel:UserInfoModel?=null


    @Inject
    lateinit var firebaseAuth:FirebaseAuth

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog:Dialog

    override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?):View?
    {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        binding.fragment=this
        return binding.root
    }

    override fun onViewCreated(view:View, savedInstanceState:Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
    }

    private fun observeListener()
    {
        userInfoViewModel.userInformationLiveData.observe(viewLifecycleOwner) {
            when(it)
            {
                is Resource.Success ->
                {
                    loadingDialog.hide()
                    userInfoModel=it.data
                    binding.userInfo=userInfoModel
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

    fun openAddressFragment()
    {
        val action=AccountFragmentDirections.actionAccountFragmentToAddressFragment()
        findNavController().navigate(action)
    }

    fun openHelpFragment()
    {
        val action=AccountFragmentDirections.actionAccountFragmentToHelpFragment()
        findNavController().navigate(action)
    }

    fun openOrderPlacedFragment()
    {
        val action=AccountFragmentDirections.actionAccountFragmentToOrderPlacedFragment()
        findNavController().navigate(action)
    }

    fun openSettingFragment()
    {
        val action=AccountFragmentDirections.actionAccountFragmentToSettingFragment()
        findNavController().navigate(action)
    }

    fun logoutFromDevice()
    {
        AlertDialog.Builder(requireContext()).setTitle("Confirm Sign Out")
            .setMessage("${userInfoModel?.userName},you are signing out of your Malal apps on this device")
            .setPositiveButton("CANCEL") {_, _ ->}.setNegativeButton("SIGNOUT") {_, _ ->
                logout()
            }.show()
    }

    private fun logout() {
        firebaseAuth.signOut()
        navigateToAuthFragment()
        showToast(getString(R.string.logOutMessage))
    }

    private fun navigateToAuthFragment() {
        val action = AccountFragmentDirections.actionAccountFragmentToLoginFragment()
        findNavController().navigate(action)
    }
}
