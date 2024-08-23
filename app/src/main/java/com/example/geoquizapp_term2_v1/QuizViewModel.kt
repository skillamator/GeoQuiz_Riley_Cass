package com.example.geoquizapp_term2_v1

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

//Key initialization
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"

//ViewModel saveStateHandler
class QuizViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {

//list of instances of Question class
    val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
    )

//saveStateHandle for currentIndex - returns the current index and saves currentIndex if activity is destroyed
    var currentIndex : Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY)?:0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

//saveStateHandle for currentQuestionAnswer - returns the answer to the current question
    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

//saveStateHandle for currentQuestionText - returns the ID of current Question
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResID

//saveStateHandle for isCheater - returns if user is a 'cheater' and saves isCheater if activity is destroyed
    var isCheater:Boolean
        get()= savedStateHandle.get(IS_CHEATER_KEY)?:false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

//moveToNext function - increases current Index by 1 (and loops)
    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionBank.size
    }

//moveToPrevious function - decreases current Index by 1 (and loops)
    fun moveToPrevious(){
        currentIndex = if (currentIndex == 0) {
            questionBank.size - 1
        } else {
            --currentIndex
        }
    }





}