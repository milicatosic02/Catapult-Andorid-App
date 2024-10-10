package com.example.projekat45.cats.quiz.result

interface ResultListState {
    data class ResultState(
        val isLoading: Boolean = false,
        val error: DetailsError? = null,
        val points: Float = 0F,
        val username : String = "",
        val category : Int = 0,
        val isPosted: Boolean = false
    ) {
        sealed class DetailsError {
            data class DataUpdateFailed(val cause: Throwable? = null): DetailsError()
        }
    }
    sealed class ResultUIEvent {
        data object PostResult : ResultUIEvent()
    }
}
