package com.example.projekat45.cats.quiz.guessCat

import com.example.projekat45.cats.db.BreedsData
import com.example.projekat45.cats.quiz.Timer
import com.example.projekat45.users.Result
import com.example.projekat45.users.UsersData

interface GuessCatListState {
    data class GuessCatState(
        val loading: Boolean = false,
        val usersData: UsersData,
        val result: Result? = null,
        val breeds: List<BreedsData> = emptyList(),
        val questions: List<GuessCatQuestion> = emptyList(),
        val points: Float = 0f,
        val questionIndex: Int = 0,
        val timer: Int = 60 * Timer.MINUTES
    ){
        fun getTimeAsFormat(): String {
            val min = timer/60
            val sec = if (timer%60 < 10) "0${timer%60}" else timer%60
            return "${min}:${sec}"
        }
    }
    data class GuessCatQuestion(
        val cats: List<BreedsData> = emptyList(),
        val images: List<String> = emptyList(),
        val questionText: String,
        val correctAnswer: String,
    )

    sealed class GuessCatUiEvent {
        data class QuestionAnswered(val catAnswer: BreedsData) : GuessCatUiEvent()
        data class AddResult(val result: Result) : GuessCatUiEvent()
    }
}