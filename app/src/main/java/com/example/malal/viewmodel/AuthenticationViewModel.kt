package com.example.malal.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.malal.data.repository.AuthenticationRepository
import com.example.malal.util.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(private val authenticationRepository:AuthenticationRepository) : ViewModel()
{

    private val _signInStatusLiveData=MutableLiveData<Resource<Unit?>>()
    val signInStatusLiveData:LiveData<Resource<Unit?>>
        get()=_signInStatusLiveData


    private val _signUpStatusLiveData=MutableLiveData<Resource<FirebaseUser?>>()
    val signUpStatusLiveData:LiveData<Resource<FirebaseUser?>>
        get()=_signUpStatusLiveData


    private val _userInfoLiveData = MutableLiveData<Resource<String>>()
    val userInfoLiveData: LiveData<Resource<String>>
    get() = _userInfoLiveData


    private val _userAddressLiveData = MutableLiveData<Resource<String>>()
    val userAddressLiveData: LiveData<Resource<String>>
        get() = _userAddressLiveData


    fun setSignInStatusForIdle()
    {
        _signUpStatusLiveData.postValue(Resource.Idle())
    }

    fun setUserInformationValue()
    {
        _userInfoLiveData.value = Resource.Idle()
    }


    fun isFirstTimeOpened(): Boolean = authenticationRepository.isFirstTimeOpened()

    fun isUserLoggedIn(): Boolean = authenticationRepository.isUserLoggedIn()

    fun userLogin(email:String, password:String)
    {
        _signInStatusLiveData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO){
           _signInStatusLiveData.postValue(authenticationRepository.userLogin(email,password))
        }
    }

    fun userSignUp(email:String, password:String)
    {
        _signUpStatusLiveData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO){
            _signUpStatusLiveData.postValue(authenticationRepository.userSignUp(email,password))
        }
    }

    fun uploadUserInformation(userUid: String,userName: String, imageUri: Uri?, userEmail: String,userAddress:String)
    {
        _userInfoLiveData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _userInfoLiveData.postValue(authenticationRepository.uploadUserInformation(userUid,userName,imageUri,userEmail,userAddress))
        }

    }

    fun uploadUserAddress(addressTitle:String,phone: String, city: String, state: String)
    {
        _userAddressLiveData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _userAddressLiveData.postValue(authenticationRepository.uploadUserAddress(addressTitle,phone,city,state))
        }
    }

}