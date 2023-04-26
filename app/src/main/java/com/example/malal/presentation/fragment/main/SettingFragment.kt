package com.example.malal.presentation.fragment.main

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.malal.R
import com.example.malal.databinding.FragmentSettingBinding
import com.example.malal.model.AddressModel
import com.example.malal.model.UserInfoModel
import com.example.malal.util.CACHE_DIR
import com.example.malal.util.LOADING_ANNOTATION
import com.example.malal.util.Resource
import com.example.malal.util.extention.closeFragment
import com.example.malal.util.extention.showToast
import com.example.malal.viewmodel.AuthenticationViewModel
import com.example.malal.viewmodel.UserInfoViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class SettingFragment:Fragment()
{
    private lateinit var binding:FragmentSettingBinding

    private val userInfoViewModel by activityViewModels<UserInfoViewModel>()
    private val authViewModel by activityViewModels<AuthenticationViewModel>()
    private var mImageUri: Uri? = null

    private var userInfoModel: UserInfoModel? = null
    private var userAddressModel:AddressModel?=null

    @Inject
    lateinit var firebaseAuth:FirebaseAuth

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog:Dialog


    override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?):View?
    {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_setting,container,false)
        binding.fragment=this
        return binding.root
    }

    override fun onViewCreated(view:View, savedInstanceState:Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        observeListener()
    }

    override fun onResume()
    {
        super.onResume()
        showUserImage()
    }
    private fun observeListener()
    {
        authViewModel.userInfoLiveData.observe(viewLifecycleOwner) {info ->
            when(info)
            {
                is Resource.Loading ->
                {
                    loadingDialog.show()
                }

                is Resource.Success ->
                {
                    loadingDialog.hide()
                    showToast(info.data!!)
                    authViewModel.setUserInformationValue()
                    closeFragment()
                }
                is Resource.Error ->
                {
                    loadingDialog.hide()
                    showToast(info.message.toString())
                }

                is Resource.Idle ->
                {
                    loadingDialog.hide()
                }
            }
        }

        userInfoViewModel.userAddressLiveData.observe(viewLifecycleOwner){
            when(it)
            {
                is Resource.Loading ->
                {
                    loadingDialog.show()
                }

                is Resource.Success ->
                {
                    loadingDialog.hide()
                    userAddressModel=it.data

                }
                is Resource.Error ->
                {
                    loadingDialog.hide()
                }

                is Resource.Idle ->
                {
                    loadingDialog.hide()
                }
            }
        }

        userInfoViewModel.userInformationLiveData.observe(viewLifecycleOwner){
            when(it)
            {
                is Resource.Loading ->
                {
                    loadingDialog.show()
                }

                is Resource.Success ->
                {
                    loadingDialog.hide()
                    userInfoModel=it.data
                    binding.userInfo=userInfoModel

                }
                is Resource.Error ->
                {
                    loadingDialog.hide()
                }

                is Resource.Idle ->
                {
                    loadingDialog.hide()
                }
            }
        }

    }


    fun submitUserInfo()
    {
        if(mImageUri.toString()=="null")
        {
            val imageBitmap=binding.userProfileImage.drawable.toBitmap()
            bitmapToImageUri(imageBitmap)
        }
        with(binding) {
            val userName=userNameEditText.text.toString().trim()
            if(userName.isEmpty())
            {
                showToast(getString(R.string.addUserName))
                return@with
            }
            if(mImageUri == null && userInfoModel == null)
            {
                showToast(getString(R.string.addUserImage))
                return@with
            }
            firebaseAuth.uid?.let {
                userAddressModel?.let {it1 ->
                    userInfoModel?.let {it2 ->
                        authViewModel.uploadUserInformation(
                                it,
                                userName,
                                mImageUri,
                                it2.userEmail,
                                it1.addressTitle
                                                           )
                    }
                }
            }

        }
    }

    fun setProfilePhoto()
    {
        val popupMenu= PopupMenu(context,binding.userProfileImage)

        popupMenu.menuInflater.inflate(R.menu.profile_photo_menu,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {item ->
            when(item.itemId)
            {
                R.id.galleryMenu -> selectImageFromGallery()
                R.id.cameraMenu -> setImageUsingCamera()

            }
            true
        }
        popupMenu.show()
    }

    private fun selectImageFromGallery()
    {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(Intent.createChooser(intent, "Select Picture"))
    }

    private val resultLauncher =registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        result ->
        mImageUri = result.data?.data
    }


    private fun setImageUsingCamera()
    {
        val intent=Intent()
        intent.action=MediaStore.ACTION_IMAGE_CAPTURE
        cameraResultLauncher.launch(intent)

    }

    private val cameraResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        result ->
        if(result.resultCode==Activity.RESULT_OK)
        {
            val bitmap=result.data?.extras?.get("data") as Bitmap
            bitmapToImageUri(bitmap)
        }

    }

    private fun bitmapToImageUri(bitmap:Bitmap)
    {

        binding.userProfileImage.setImageBitmap(bitmap)
        val file = File(requireContext().cacheDir, CACHE_DIR)
        file.delete()
        file.createNewFile()
        val fileOutputStream = file.outputStream()
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
        val bytearray = byteArrayOutputStream.toByteArray()
        fileOutputStream.write(bytearray)
        fileOutputStream.flush()
        fileOutputStream.close()
        byteArrayOutputStream.close()

        mImageUri = file.toUri()
    }


    private fun showUserImage()
    {
        if (mImageUri != null)
            binding.userProfileImage.setImageURI(mImageUri)
    }

}