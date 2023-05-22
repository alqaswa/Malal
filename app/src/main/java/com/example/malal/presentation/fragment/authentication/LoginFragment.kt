package com.example.malal.presentation.fragment.authentication

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.malal.R
import com.example.malal.databinding.FragmentLoginBinding
import com.example.malal.util.LOADING_ANNOTATION
import com.example.malal.util.Resource
import com.example.malal.util.extention.hide
import com.example.malal.util.extention.show
import com.example.malal.util.extention.showToast
import com.example.malal.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class LoginFragment : Fragment()
{
    private lateinit var binding:FragmentLoginBinding
    private val authViewModel by viewModels<AuthenticationViewModel>()

    @Inject
    @Named(LOADING_ANNOTATION)
    lateinit var loadingDialog:Dialog

    override fun onCreate(savedInstanceState:Bundle?)
    {
        super.onCreate(savedInstanceState)
        loadingDialog.hide()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        observeLiveData()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.fragment = this

        return binding.root
    }

    private fun observeLiveData()
    {
        authViewModel.signInStatusLiveData.observe(viewLifecycleOwner) {
            when(it)
            {
                is Resource.Loading ->
                {
                    loadingDialog.show()
                }
                is Resource.Success ->
                {
                    loadingDialog.hide()
                    showToast("Login Successfully")
                    authViewModel.setSignInStatusForIdle()
                    val action=LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                    findNavController().navigate(action)
                }
                is Resource.Error ->
                {
                    loadingDialog.hide()
                    showToast(it.message.toString())
                    authViewModel.setSignInStatusForIdle()
                }
                else ->
                {
                    loadingDialog.hide()
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun checkInput()
    {
        val email=binding.loginEmailET.text.toString().trim()
        val pass=binding.LoginPassEt.text.toString().trim()

        if (email.isEmpty())
        {
            binding.emailError.show()
            binding.emailError.text = "Email Can't be Empty"
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            binding.emailError.show()
            binding.emailError.text = "Enter Valid Email"
            return
        }
        if(pass.isEmpty())
        {
            binding.passwordError.show()
            binding.passwordError.text= "Password Can't be Empty"
            return
        }

        if (pass.isNotEmpty() and email.isNotEmpty())
        {
            binding.emailError.hide()
            binding.passwordError.hide()

            val emailId=binding.loginEmailET.text.toString().trim()
            val password=binding.LoginPassEt.text.toString().trim()
            authViewModel.userLogin(emailId,password)
        }
    }

    fun signInUser()
    {
        checkInput()
    }

    fun signUpUser()
    {
        val action=LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
        findNavController().navigate(action)
    }

}