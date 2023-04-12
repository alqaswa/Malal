package com.example.malal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.malal.data.repository.AuthenticationRepository
import com.example.malal.model.AddressModel
import com.example.malal.model.UserInfoModel
import com.example.malal.util.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(private val authenticationRepository:AuthenticationRepository):ViewModel()
{
    private val _userInformation = MutableLiveData<Resource<UserInfoModel>>()
    val userInformationLiveData : LiveData<Resource<UserInfoModel>> = _userInformation

    private val _userAddress = MutableLiveData<Resource<AddressModel>>()
    val userAddressLiveData : LiveData<Resource<AddressModel>> = _userAddress

    init {
        getUserInformation()
        getUserAddress()
    }

    private fun getUserInformation(){
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.getUserInformation(_userInformation)
        }
    }

    private fun getUserAddress()
    {
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.getUserAddress(_userAddress)
        }
    }
}