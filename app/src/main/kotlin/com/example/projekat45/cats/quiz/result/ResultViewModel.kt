package com.example.projekat45.cats.quiz.result

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekat45.cats.repository.BreedsRepository
import com.example.projekat45.navigation.category
import com.example.projekat45.navigation.result
import com.example.projekat45.users.UsersDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: BreedsRepository,
    private val usersDataStore: UsersDataStore
) : ViewModel() {
    private val result: Float = savedStateHandle.result
    private val _resultState = MutableStateFlow(ResultListState.ResultState())
    val resultState = _resultState.asStateFlow()
    private val _resultEvents = MutableSharedFlow<ResultListState.ResultUIEvent>()
    fun setEvent(event: ResultListState.ResultUIEvent) = viewModelScope.launch { _resultEvents.emit(event) }

    private fun setResultSate (update: ResultListState.ResultState.() -> ResultListState.ResultState) =
        _resultState.getAndUpdate(update)

    init {
        observeResult()
        observeEvents()
    }
    private fun observeEvents() {
        viewModelScope.launch {
            _resultEvents.collect { resultUIEvent ->
                when (resultUIEvent) {
                    is ResultListState.ResultUIEvent.PostResult -> post()
                }
            }
        }
    }

    private fun observeResult() {

        viewModelScope.launch {
            setResultSate { copy(isLoading = true) }
            try {
                setResultSate {
                    copy(
                        category = category,
                        username =usersDataStore.data.value.users[usersDataStore.data.value.pick].nickname,
                        points = result
                    )
                }
            }catch (error: IOException){
                setResultSate { copy(error = ResultListState.ResultState.DetailsError.DataUpdateFailed(cause = error)) }
            }finally {
                setResultSate { copy( isLoading = false) }
            }

        }
    }
    private fun post(){
        viewModelScope.launch {
            setResultSate { copy(isLoading = true) }
            withContext(Dispatchers.IO) {
                val state = resultState.value
                repository.postResult(state.username,state.points,1)
            }
            setResultSate { copy(isPosted = true, isLoading = false) }
        }
    }



}
