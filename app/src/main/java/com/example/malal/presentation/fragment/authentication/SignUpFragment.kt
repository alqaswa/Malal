package com.example.malal.presentation.fragment.authentication

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.malal.R
import com.example.malal.databinding.FragmentSignUpBinding
import com.example.malal.util.LOADING_ANNOTATION
import com.example.malal.util.Resource
import com.example.malal.util.extention.closeFragment
import com.example.malal.util.extention.showToast
import com.example.malal.viewmodel.AuthenticationViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class SignUpFragment:Fragment()
{

    private lateinit var binding:FragmentSignUpBinding
    private val authViewModel:AuthenticationViewModel by activityViewModels()

    private lateinit var nameEt:AppCompatEditText
    private lateinit var emailEt:AppCompatEditText
    private lateinit var passEt:AppCompatEditText
    private lateinit var cnfPassEt:AppCompatEditText


    @Inject
    lateinit var firebaseFirestore:FirebaseFirestore

    @Inject
    lateinit var firebaseAuth:FirebaseAuth

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog:Dialog


    override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?):View?
    {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        binding.apply {
            nameEt=nameEtSignUpPage
            emailEt=emailEtSignUpPage
            passEt=PassEtSignUpPage
            cnfPassEt=cPassEtSignUpPage
            fragment=this@SignUpFragment

        }
        observeLiveData()
        return binding.root
    }

    fun userSignUp()
    {
        checkInput()
    }
    fun backToLoginFragment()
    {
        closeFragment()
    }

    private fun checkInput()
    {
        val name=nameEt.text.toString().trim()
        val email=emailEt.text.toString().trim()
        val password=passEt.text.toString().trim()
        val cnfPassword=cnfPassEt.text.toString().trim()

        if (name.isEmpty())
        {
            showToast("Name can't empty!")
            return
        }
        if (email.isEmpty())
        {
            showToast("Email can't empty!")
            return
        }

        if (!email.matches(Patterns.EMAIL_ADDRESS.toRegex()))
        {
            showToast("Enter Valid Email")
            return
        }
        if(password.isEmpty())
        {
            showToast("Password can't empty!")
            return
        }
        if (password != cnfPassword)
        {
            showToast("Password not Match")
            return
        }

        authViewModel.userSignUp(email,password)
    }

    private fun observeLiveData()
    {

        authViewModel.signUpStatusLiveData.observe(viewLifecycleOwner)
        {
            when(it)
            {
                is Resource.Loading ->
                {
                    loadingDialog.show()
                }
                is Resource.Success ->
                {
                    val name=nameEt.text.toString().trim()
                    val email=emailEt.text.toString().trim()
                    val userUid=it.data!!.uid

                    loadingDialog.hide()
                    authViewModel.uploadUserInformation(userUid,name,null,email,"")

                }
                is Resource.Error ->
                {
                    loadingDialog.hide()
                    showToast(it.message.toString())
                }
                else ->
                {
                    loadingDialog.hide()
                }
            }
        }
        authViewModel.userInfoLiveData.observe(viewLifecycleOwner)
        {
            when(it)
            {
                is Resource.Loading ->
                {
                    loadingDialog.show()
                }
                is Resource.Success ->
                {
                    loadingDialog.hide()
                    val action=SignUpFragmentDirections.actionSignUpFragmentToHomeFragment()
                    findNavController().navigate(action)
                }
                is Resource.Error ->
                {
                    loadingDialog.hide()
                    showToast(it.message.toString())
                }

                is Resource.Idle ->
                {
                    loadingDialog.hide()
                }
            }
        }

    }
}