package com.example.projekat45.cats.api.model

import kotlinx.serialization.Serializable

@Serializable
data class BreedsApiModel(
    val id: String,
    val name: String,
    val alt_names: String? = null,
    //val temperament: List<String>,
    val description: String,
    val origin: String,
    val life_span: String,
    val adaptability: Int,
    val affection_level: Int,
    val dog_friendly: Int,
    val wikipedia_url: String?="",
    val image: Image?  // Dodato polje image koje može biti BreedsImageApiModel
)

@Serializable
data class Image(
    val url: String,
    val id: String
)