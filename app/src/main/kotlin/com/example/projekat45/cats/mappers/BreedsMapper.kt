package com.example.projekat45.cats.mappers

import androidx.room.PrimaryKey
import com.example.projekat45.cats.api.model.BreedsApiModel
import com.example.projekat45.cats.db.BreedsData
import com.example.projekat45.cats.db.BreedsImage

//fun BreedsApiModel.asBreedDbModel() : BreedsData{
//    val breedsData = BreedsData(
//        id = this.id,
//        name = this.name,
//        alt_names = this.alt_names,
//        description = this.description,
//        origin = this.origin,
//        life_span = this.life_span,
//        adaptability = this.adaptability,
//        affection_level = this.affection_level,
//        dog_friendly = this.dog_friendly,
//        wikipedia_url = this.wikipedia_url,
//        image = this.image?.let { BreedsImage(it.url) } // Convert image data
//    )
//    println("Converted BreedsApiModel to BreedsData: $breedsData")
//
//    return breedsData
//}
