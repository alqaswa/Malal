package com.example.malal.di

import android.content.Context
import com.example.malal.R
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule
{

    @Singleton
    @Provides
    fun provideFirebaseAuth():FirebaseAuth
    {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseStorage():FirebaseStorage
    {
        return FirebaseStorage.getInstance()
    }


    @Singleton
    @Provides
    fun provideFirebaseFirestore():FirebaseFirestore
    {
        return FirebaseFirestore.getInstance()
    }

}