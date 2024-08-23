package com.example.geoquizapp_term2_v1

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.geoquizapp_term2_v1.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"

// Main Activity Class
class MainActivity : AppCompatActivity() {

//Binding and ViewModel initialization
    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()

//Passes and stores whether the user viewed the answer in 'CheatActivity'
    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {result ->
        if (result.resultCode == Activity.RESULT_OK){
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)?:false
        }
    }

    //variable  declaration
    private var correctScore = 0.0
    private var incorrectScore = 0.0
    private var cheatedScore = 0

//onCreate function (start of Main Activity) & Logs for onCreate and quizViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG,"Got a QuizViewModel: $quizViewModel")

    //Cheat Button Listener
        binding.cheatButton.setOnClickListener(){
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }

    //True Button Listener
        binding.trueButton.setOnClickListener() {
            if (quizViewModel.questionBank[quizViewModel.currentIndex].answered){
                questionAnswered()
            }else{
                checkAnswer(true)
            }
        }

    //false Button Listener
        binding.falseButton.setOnClickListener() {
            if (quizViewModel.questionBank[quizViewModel.currentIndex].answered){
                questionAnswered()
            }else{
                checkAnswer(false)
            }
        }

    //Next Button Listener
        binding.nextButton.setOnClickListener() {
            quizViewModel.moveToNext()
            updateQuestion()
        }

    //Previous Button Listener
        binding.previousButton.setOnClickListener(){
            quizViewModel.moveToPrevious()
            updateQuestion()
        }

//Updates the TextView to show first Question
        updateQuestion()
    }



// Logs for Debugging and general enquiries for onStart, onResume, ect
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestory() called")
    }



//The checkAnswer function - called when an answer is given to a question - takes user input as function input
    //Handles the players score, the Toast message and the number of times cheated
    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        quizViewModel.questionBank[quizViewModel.currentIndex].answered = true

//Setting Toast message string
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgement_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
            }

//Toast response to users answer
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
        .show()

//score & cheated handler
        when{
            quizViewModel.isCheater -> {
                ++cheatedScore
                quizViewModel.isCheater = false
            }
            userAnswer == correctAnswer -> ++correctScore
            else -> ++incorrectScore
        }

    }

//The UpdateQuestions Function - called when current question is updated using either the 'next' or 'previous' button - Updates the TextView to new question
    //checks if all Questions have been answered, if so, provides a Toast with total score as a percentage and number of times cheated
    private fun updateQuestion() {

        if (checkAllQuestionsAnswered(quizViewModel.questionBank)){
            val gradePercent = 100.00*(correctScore/(correctScore + incorrectScore))
            Toast.makeText(this, "Quiz Complete! Grade Percent: $gradePercent %   Times Cheated: $cheatedScore", Toast.LENGTH_LONG)
                .show()
        }
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
    }

//The checkAllQuestionsAnswered Function - called when updating the TextView to the next question and returns if EVERY question has been answered
    private fun checkAllQuestionsAnswered(instances: List<Question>): Boolean {
        return instances.all { it.answered }
    }

//The Question Answered Function - is called if the user has already provided an answer to a question
    private fun questionAnswered(){
        val alreadyAnsweredRes = R.string.question_already_answered
        Toast.makeText(this, alreadyAnsweredRes, Toast.LENGTH_SHORT)
            .show()


    }


}

