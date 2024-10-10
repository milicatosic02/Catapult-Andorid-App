package com.example.projekat45.cats.gallery

interface BreedsGalleryListState {
    data class BreedsGalleryState(
        val loading: Boolean = false,
        val photos: List<String> = emptyList(),
        val error: DetailsError? = null,
        val breedsId:String
    )
    {
        sealed class DetailsError {
            data class DataUpdateFailed(val cause: Throwable? = null): DetailsError()
        }
    }
}