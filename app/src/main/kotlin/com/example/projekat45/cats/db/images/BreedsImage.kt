package com.example.projekat45.cats.db.images

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Entity(tableName = "images")
@Serializable
data class BreedsGallery (
    @PrimaryKey
    val url: String,
    @Transient
    val id: String = ""
)