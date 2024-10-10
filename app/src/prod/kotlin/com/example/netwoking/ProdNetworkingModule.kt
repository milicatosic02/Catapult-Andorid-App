package com.example.netwoking

import com.example.projekat45.networking.di.BaseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ProdNetworkingModule {

    @BaseUrl
    @Provides
    fun provideProdUrl(): String = "https://api.thecatapi.com/v1/"
}