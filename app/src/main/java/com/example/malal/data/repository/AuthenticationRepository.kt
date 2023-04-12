package com.example.malal.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.malal.R
import com.example.malal.model.*
import com.example.malal.util.ADDRESS_COLLECTION
import com.example.malal.util.CART_COLLECTION
import com.example.malal.util.Resource
import com.example.malal.util.USERS_COLLECTION
import com.example.malal.util.helper.SharedPreferenceHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ViewModelScoped
class AuthenticationRepository @Inject constructor(private val sharedPreferenceHelper:SharedPreferenceHelper,
                                                   private val firebaseAuth:FirebaseAuth,
                                                   private val firebaseStorage:FirebaseStorage,
                                                   private val firebaseFirestore: FirebaseFirestore,
                                                   @ApplicationContext private val context:Context)
{

    private val userUid by lazy { firebaseAuth.uid!! }
    private val firebaseUserCollection by lazy { firebaseFirestore.collection(USERS_COLLECTION) }
    private val userAddressCollection by lazy {firebaseFirestore.collection(ADDRESS_COLLECTION)}

    fun isFirstTimeOpened(): Boolean
    {
        return sharedPreferenceHelper.isFirstTimeOpened()
    }

    fun isUserLoggedIn(): Boolean
    {
        val user = firebaseAuth.currentUser
        return user != null

    }

    suspend fun userLogin(email:String, password:String):Resource<Unit?>
    {
       return try
        {
           firebaseAuth.signInWithEmailAndPassword(email,password).await()
            Resource.Success(null)
        }
        catch(e:Exception)
        {
            Resource.Error(e.message.toString())
        }
    }
    suspend fun userSignUp(email:String,password:String):Resource<FirebaseUser?>
    {
        return try
        {
            firebaseAuth.createUserWithEmailAndPassword(email,password).await()
            Resource.Success(firebaseAuth.currentUser)
        }
        catch(e:Exception)
        {
            Resource.Error(e.message.toString())
        }
    }

    suspend fun uploadUserInformation(userUid:String,userName: String, imageUri: Uri?, userEmail: String,userAddress:String): Resource<String>
    {
        return try
        {
            var accountStatusMessage = context.getString(R.string.accountUpdatedSuccessfully)
            if (imageUri != null)
            {
                val uploadedImagePath = uploadUserImage(imageUri)
                val userInfoModel =UserInfoModel(userUid, userName, uploadedImagePath, userEmail,userAddress)
                firebaseUserCollection.document(userUid).update(userInfoModel.toMap()).await()
            }
            else
            {
                val userInfoModel =UserInfoModel(userUid, userName, "", userEmail,userAddress)
                firebaseUserCollection.document(userUid).set(userInfoModel.toMapWithoutImage()).await()
                accountStatusMessage = context.getString(R.string.accountUpdatedSuccessfully)
            }
            Resource.Success(accountStatusMessage)
        }
        catch (e: Exception)
        {
            Resource.Error(e.toString())
        }
    }

    private suspend fun uploadUserImage(imageUri:Uri): String
    {
        val uploadingResult =firebaseStorage.reference
            .child("${USERS_COLLECTION}/${System.currentTimeMillis()}.jpg")
            .putFile(imageUri).await()
        return uploadingResult.metadata?.reference?.downloadUrl?.await().toString()
    }

    suspend fun uploadUserAddress(addressTitle:String,phone: String, city: String, state: String):Resource<String>
    {
        return try
        {
            val userAddressModel =AddressModel(userUid, addressTitle, phone, city,state)
            userAddressCollection.document(userUid).set(userAddressModel.toMap()).await()

            Resource.Success("Address Added Successfully")
        }
        catch (e: Exception)
        {
            Resource.Error(e.toString())
        }
    }

    fun getUserAddress(userAddressLiveData:MutableLiveData<Resource<AddressModel>>)
    {
        firebaseFirestore
            .collection(ADDRESS_COLLECTION)
            .document(userUid)
            .addSnapshotListener { value, _ ->
                if (value == null)
                {
                    userAddressLiveData.postValue(Resource.Error(context.getString(R.string.errorMessage)))
                }
                else
                {
                    if(value.data!=null)
                    {
                        val addressModel = convertMapToAddressModel(value.data!!)
                        userAddressLiveData.postValue(Resource.Success(addressModel))
                    }
                    else
                    {
                        val map=mutableMapOf<String,String>()
                        map["userUid"] = ""
                        map["addressTitle"] = "You have no address please add new address"
                        map["phone"] = ""
                        map["city"] = ""
                        map["state"] = ""

                        val addressModel = convertMapToAddressModel(map)
                        userAddressLiveData.postValue(Resource.Success(addressModel))
                    }

                }
            }
    }




    fun getUserInformation(userInfoLiveData: MutableLiveData<Resource<UserInfoModel>>)
    {
        firebaseFirestore.collection(USERS_COLLECTION).document(userUid)
            .addSnapshotListener { value, _ ->
                if (value == null)
                {
                    userInfoLiveData.postValue(Resource.Error(context.getString(R.string.errorMessage)))
                }
                else
                {
                    val userInfoModel = convertMapToUserInfoModel(value.data!!)
                    userInfoLiveData.postValue(Resource.Success(userInfoModel))
                }
            }
    }


}
