package com.example.projekat45.cats.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ResultModel (
    val nickname: String,
    val result: Float,
    val category: Int
)