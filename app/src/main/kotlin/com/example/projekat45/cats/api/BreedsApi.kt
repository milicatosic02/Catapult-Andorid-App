package com.example.projekat45.cats.api

import com.example.projekat45.cats.db.BreedsData
import com.example.projekat45.cats.db.images.BreedsGallery
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BreedsApi {

    @GET("breeds")
    suspend fun getAllBreeds() : List<BreedsData>

    @GET("breeds/{id}")
    suspend fun getBreed(@Path("id") id: String): BreedsData

    @GET("images/search?limit=20")
    suspend fun getAllBreedsPhotos(@Query("breed_ids") id: String): List<BreedsGallery>
}