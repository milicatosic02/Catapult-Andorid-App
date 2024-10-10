package com.example.projekat45.users.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekat45.users.Result
import com.example.projekat45.users.UsersDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val usersDataStore: UsersDataStore
) : ViewModel() {

    private val _historyState = MutableStateFlow(HistoryListState.HistoryState(usersData = usersDataStore.data.value))
    val historyState = _historyState.asStateFlow()

    private fun setHistoryState(updateWith: HistoryListState.HistoryState.() -> HistoryListState.HistoryState) =
        _historyState.getAndUpdate(updateWith)


    fun getBestResult(quiz : String): String {
        val users = historyState.value.usersData.users
        val pick = historyState.value.usersData.pick

        return when (quiz) {
            "guessCat" -> users[pick].guessCat.bestResult.toString()
            else -> "0.0"
        }
    }

    fun getAllResults(quiz: String): List<com.example.projekat45.users.Result> {
        val users = historyState.value.usersData.users
        val pick = historyState.value.usersData.pick

        return when (quiz) {
            "guessCat" -> users[pick].guessCat.resultsHistory.reversed()
            else -> emptyList<Result>()
        }
    }

}
