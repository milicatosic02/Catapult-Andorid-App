package com.example.projekat45.cats.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekat45.cats.repository.BreedsRepository
import com.example.projekat45.navigation.breedsId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BreedsDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: BreedsRepository,
): ViewModel() {
    private val breedsId: String = savedStateHandle.breedsId
    private val _stateDetails = MutableStateFlow(BreedsDetailsListState.BreedsDetailsState(breedsId = breedsId))
    val stateDetails = _stateDetails.asStateFlow()

    private fun setDetailsState(update: BreedsDetailsListState.BreedsDetailsState.()-> BreedsDetailsListState.BreedsDetailsState)=
        _stateDetails.getAndUpdate(update)

    init {
        observeBreedsDetails()
    }
    private fun observeBreedsDetails(){
        viewModelScope.launch {
            setDetailsState { copy(loading = true) }
            repository.getBreedsByIdFlow(id = breedsId).collect{
                breedsInfoDetail->
                setDetailsState { copy(data = breedsInfoDetail, loading = false) }
            }
        }
    }
}