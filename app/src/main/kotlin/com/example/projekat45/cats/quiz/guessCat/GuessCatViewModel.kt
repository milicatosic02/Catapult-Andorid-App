package com.example.projekat45.cats.quiz.guessCat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekat45.cats.db.BreedsData
import com.example.projekat45.cats.repository.BreedsRepository
import com.example.projekat45.users.Result
import com.example.projekat45.users.UsersDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GuessCatViewModel @Inject constructor(
    private val usersDataStore: UsersDataStore,
    private val repository: BreedsRepository
) : ViewModel() {
    private val _questionState =
        MutableStateFlow(GuessCatListState.GuessCatState(usersData = usersDataStore.data.value))
    val questionState = _questionState.asStateFlow()

    private val _questionEvent = MutableSharedFlow<GuessCatListState.GuessCatUiEvent>()

    private var timerJob: Job? = null

    private val invalid = "invalid"

    private fun setQuestionState(update: GuessCatListState.GuessCatState.() -> GuessCatListState.GuessCatState) =
        _questionState.getAndUpdate(update)

    fun setQuestionEvent(even: GuessCatListState.GuessCatUiEvent) =
        viewModelScope.launch { _questionEvent.emit(even) }

    init {
        getAllCats()
        observeCatsEvent()
        startTimer()
    }
    fun seeResults(time: Int, points: Int): Float {
        //UBP = BTO * 2.5 * (1 + (PVT + 120) / MVT)
        var ubp: Float = points * 2.5F * (1 + (time + 120F) / 300)
        if (ubp > 100) ubp = 100f
        return ubp
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                setQuestionState { copy(timer = timer - 1) }

                if (questionState.value.timer <= 0) {
                    pauseTimer()
                    addResult(
                        com.example.projekat45.users.Result(
                            result = seeResults(
                                questionState.value.timer,
                                questionState.value.points.toInt()
                            ),
                            createdAt = System.currentTimeMillis()
                        )
                    )
                }
            }

        }
    }

    private fun addResult(result: com.example.projekat45.users.Result) {
        viewModelScope.launch {
            usersDataStore.addGuessCatResult(result)
            setQuestionState { copy(result = result) }
        }
    }

    fun isCorrectAnswer(catId: String): Boolean {
        val questionIndex = questionState.value.questionIndex
        val question = questionState.value.questions[questionIndex]
        return catId == question.correctAnswer
    }

    private fun pauseTimer() {
        timerJob?.cancel()
    }

    private fun getAllCats() {
        viewModelScope.launch {
            setQuestionState { copy(loading = true) }
            val breeds = repository.getAllBreedsFlowFromDb().first().shuffled()
            setQuestionState { copy(breeds = breeds) }
            createQuestions()
            setQuestionState { copy(loading = false) }
        }
    }

    private fun observeCatsEvent() {
        viewModelScope.launch {
            _questionEvent.collect {
                when (it) {
                    is GuessCatListState.GuessCatUiEvent.QuestionAnswered -> checkAnswer(it.catAnswer)
                    is GuessCatListState.GuessCatUiEvent.AddResult -> addResult(it.result)
                }
            }
        }
    }

    private fun checkAnswer(catAnswer: BreedsData) {
        var questionIndex = questionState.value.questionIndex
        val question = questionState.value.questions[questionIndex]
        var points = questionState.value.points
        if (catAnswer.id == question.correctAnswer)
            points++

        if (questionIndex < 19)
            questionIndex++
        else { //End Screen
            pauseTimer()
            addResult(
                Result(
                    result = seeResults(questionState.value.timer, points.toInt()),
                    createdAt = System.currentTimeMillis()
                )
            )
        }
        setQuestionState {
            copy(
                questionIndex = questionIndex,
                points = points,
            )
        }

    }

    private suspend fun getPictures(id: String): List<String> {
        val photos = repository.getAllBreedsPhotoByIdFLow(id = id).first()

        if (photos.isNotEmpty())
            return photos

        return withContext(Dispatchers.IO) {
            repository.getAllBreedsPhotosApi(id = id).map { it.url }
        }
    }


    private suspend fun createQuestions() {
        val breeds = questionState.value.breeds
        val questions: MutableList<GuessCatListState.GuessCatQuestion> = ArrayList()
        val len = breeds.size
        var skip = 0
        var i = 2
        var questionNumber = 0
        var pictureIndex = -1

        var cat1photos = getPictures(breeds[0].id)
        var cat2photos = getPictures(breeds[1].id)
        var cat3photos = getPictures(breeds[2].id)

        while (++i < 23 + skip) {
            pictureIndex++
            val cat4photos = getPictures(breeds[i].id)
            if (cat4photos.isEmpty()) {
                skip++
                continue
            }

            if (cat1photos.isEmpty() || cat2photos.isEmpty() || cat3photos.isEmpty()) {
                skip++
                cat1photos = cat2photos
                cat2photos = cat3photos
                cat3photos = cat4photos
                continue
            }

            var question = ""
            var answer: Pair<String, String> = Pair(invalid, invalid) //(id, answer)
            for (j in 1..2) {
                questionNumber = (questionNumber + 1) % 2

                answer = giveAnswer(questionNumber, i - 3)
                if (answer.first != invalid) {
                    question = giveQuestion(questionNumber, answer.second)
                    break
                }
            }

            if (answer.first == invalid) {
                skip++
                cat1photos = cat2photos
                cat2photos = cat3photos
                cat3photos = cat4photos
                Log.d("SLIKE","Skip: cat1photos = ${cat1photos.joinToString(", ")}, cat2photos = ${cat2photos.joinToString(", ")}, cat3photos = ${cat3photos.joinToString(", ")}, cat4photos = ${cat4photos.joinToString(", ")}")

                continue
            }

            questions.add(
                GuessCatListState.GuessCatQuestion(
                    cats = breeds.slice(i - 3..i),
                    images = listOf(
                        cat1photos[pictureIndex % cat1photos.size],
                        cat2photos[pictureIndex % cat2photos.size],
                        cat3photos[pictureIndex % cat3photos.size],
                        cat4photos[pictureIndex % cat4photos.size],
                    ),
                    questionText = question,
                    correctAnswer = answer.first,

                    )
            )

            cat1photos = cat2photos
            cat2photos = cat3photos
            cat3photos = cat4photos
            Log.d("SLIKE KAD JE SLEDECA ITERACIJA","Skip: cat1photos = ${cat1photos.joinToString(", ")}, cat2photos = ${cat2photos.joinToString(", ")}, cat3photos = ${cat3photos.joinToString(", ")}, cat4photos = ${cat4photos.joinToString(", ")}")

        }
        setQuestionState { copy(questions = questions.shuffled()) }
    }

    private fun giveAnswer(questionNumber: Int, index: Int): Pair<String, String> {
        return when (questionNumber) {
            0 -> giveTemperament(index)
            else -> giveRace(index)
        }
    }

    private fun giveTemperament(index: Int): Pair<String, String> {
        val cats = questionState.value.breeds
        val allTemps = cats[index].getListOfTemperaments() +
                cats[index + 1].getListOfTemperaments() +
                cats[index + 2].getListOfTemperaments() +
                cats[index + 3].getListOfTemperaments()

        val uniqueTemps = allTemps
            .groupBy { it.lowercase() }
            .filter { it.value.size == 1 }
            .flatMap { it.value }
            .shuffled()

        var currNum = Random.nextInt(0, 4)
        for (j in 0..3) {
            if (cats[index + currNum].getListOfTemperaments().contains(uniqueTemps[0]))
                return Pair(cats[index + currNum].id, uniqueTemps[0])
            currNum = (currNum + 1) % 4
        }
        return Pair(invalid, invalid)
    }

    private fun giveRace(i: Int): Pair<String, String> {
        val cats = questionState.value.breeds
        val rand = Random.nextInt(0, 4)
        return Pair(cats[i + rand].id, cats[i + rand].name)
    }


    private fun giveQuestion(num: Int, answer: String): String {
        return when (num) {
            0 -> "Which cat is $answer?"
            else -> "Which cat belongs to a race $answer?"
        }
    }


}
