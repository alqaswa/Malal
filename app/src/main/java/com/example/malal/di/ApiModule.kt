package com.example.malal.di


import com.example.malal.data.networking.MakeUpApi
import com.example.malal.util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule
{

    @Provides
    @Singleton
    fun providesRetrofit():Retrofit
    {
       return Retrofit.Builder()
           .baseUrl(BASE_URL)
           .addConverterFactory(GsonConverterFactory.create())
           .build()
    }

    @Singleton
    @Provides
    fun providesMakeUpApi(retrofit:Retrofit): MakeUpApi
    {
        return retrofit.create(MakeUpApi::class.java)
    }



}