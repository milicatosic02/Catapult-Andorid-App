package com.example.projekat45.cats.details

import com.example.projekat45.cats.db.BreedsData

interface BreedsDetailsListState {

    data class BreedsDetailsState(
        val breedsId: String,
        val loading: Boolean = false,
        val data: BreedsData? = null,
        val error: DetailsError? = null,
    )
    {
        sealed class DetailsError {
            data class DataUpdateFailed(val cause: Throwable? = null): DetailsError()
        }
    }
}