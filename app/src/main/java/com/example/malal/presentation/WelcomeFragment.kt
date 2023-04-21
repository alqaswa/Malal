package com.example.malal.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.malal.R
import com.example.malal.databinding.FragmentWelcomeBinding
import com.example.malal.viewmodel.AuthenticationViewModel
import com.example.malal.viewmodel.ShopViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : Fragment()
{

    private lateinit var binding:FragmentWelcomeBinding
    private val authViewModel: AuthenticationViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val isFirstTimeLogIn = authViewModel.isFirstTimeOpened()
        if(!isFirstTimeLogIn)
        {
            checkIfUserLoggedIn()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_welcome, container, false)
        binding.fragment = this
        return binding.root
    }

    private fun checkIfUserLoggedIn() {
        val isLoggedIn = authViewModel.isUserLoggedIn()
        if(isLoggedIn)
        {
            navigateToMainFragment()
        }else
        {
            navigateToLoginFragment()
        }
    }

    private fun navigateToLoginFragment()
    {
        val action=WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    private fun navigateToMainFragment()
    {
        val action=WelcomeFragmentDirections.actionWelcomeFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    fun openSignupFragment()
    {
        navigateToLoginFragment()
    }
}