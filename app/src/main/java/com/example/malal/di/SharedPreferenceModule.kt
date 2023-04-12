package com.example.malal.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class SharedPreferenceModule
{
    @ViewModelScoped
    @Provides
    fun provideSharedPreferences(@ApplicationContext context:Context):SharedPreferences
    {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}