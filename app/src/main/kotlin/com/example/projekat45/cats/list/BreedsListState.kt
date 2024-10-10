package com.example.projekat45.cats.list

import com.example.projekat45.cats.db.BreedsData
import com.example.projekat45.users.User
import com.example.projekat45.users.UsersData

interface BreedList{
    data class BreedsListState(
        val loading: Boolean = true,
        val updating: Boolean = false,
        val breeds: List<BreedsData> = emptyList(),
        val usersData: UsersData,
        val darkTheme: Boolean,
        val breedsFilter: List<BreedsData> = emptyList(),
        val error: Error? = null,
        val isSearching: Boolean = false,
        val search: String = ""
    ){
        sealed class Error {
            data class DataUpdateFailed(val cause: Throwable? = null): Error()
        }
    }

    sealed class BreedsListUiEvent{
        data class SreachQuery(val query: String): BreedsListUiEvent()
        data class ChangeTheme(val bool:Boolean): BreedsListUiEvent()
    }
}

