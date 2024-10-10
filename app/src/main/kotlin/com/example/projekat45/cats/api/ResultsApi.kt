package com.example.projekat45.cats.api

import com.example.projekat45.cats.api.model.ResultModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ResultsApi {
    @GET("leaderboard")
    suspend fun getAllResultsForCategory(@Query("category") category: Int): List<ResultModel>

    @POST("leaderboard")
    suspend fun postResult(@Body obj:ResultModel)
}