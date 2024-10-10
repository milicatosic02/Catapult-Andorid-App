package com.example.projekat45.cats.quiz.leaderboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekat45.cats.repository.BreedsRepository
import com.example.projekat45.navigation.category
import com.example.projekat45.users.UsersDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: BreedsRepository,
    private val usersDataStore: UsersDataStore
) : ViewModel() {

    private val category: Int = savedStateHandle.category
    private val _leaderboardState = MutableStateFlow(LeaderboradListState.LeaderboardState())
    val leaderboardState = _leaderboardState.asStateFlow()

    private fun setLeaderboardState (update: LeaderboradListState.LeaderboardState.() -> LeaderboradListState.LeaderboardState) =
        _leaderboardState.getAndUpdate(update)

    init {
        observeCatPhoto()
    }

    private fun observeCatPhoto() {

        viewModelScope.launch {
            setLeaderboardState { copy(loading = true) }
            try {
                val list = repository.fetchAllResultsForCategory(category = category)
                setLeaderboardState { copy(results = list, nick = usersDataStore.data.value.users[usersDataStore.data.value.pick].nickname) }
            }catch (error: IOException){
                setLeaderboardState { copy(error = LeaderboradListState.LeaderboardState.DetailsError.DataUpdateFailed(cause = error)) }
            }finally {
                setLeaderboardState { copy(loading = false) }
            }

        }
    }

}