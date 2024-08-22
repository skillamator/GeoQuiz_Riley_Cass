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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {result ->
        if (result.resultCode == Activity.RESULT_OK){
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)?:false
        }
    }




    private var correctScore = 0.0
    private var incorrectScore = 0.0
    private var cheatedScore = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG,"Got a QuizViewModel: $quizViewModel")

        binding.cheatButton.setOnClickListener(){
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }


        binding.trueButton.setOnClickListener() {
            if (quizViewModel.questionBank[quizViewModel.currentIndex].answered){
                questionAnswered()
            }else{
                checkAnswer(true)
            }
        }

        binding.falseButton.setOnClickListener() {
            if (quizViewModel.questionBank[quizViewModel.currentIndex].answered){
                questionAnswered()
            }else{
                checkAnswer(false)
            }
        }

        binding.nextButton.setOnClickListener() {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.previousButton.setOnClickListener(){
            quizViewModel.moveToPrevious()
            updateQuestion()
        }

        updateQuestion()
    }




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



    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        quizViewModel.questionBank[quizViewModel.currentIndex].answered = true

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgement_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
            }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
        .show()
//    if (userAnswer == correctAnswer) {
//        ++correctScore
//    } else {
//        ++incorrectScore
//    }
        when{
            quizViewModel.isCheater -> {
                ++cheatedScore
                quizViewModel.isCheater = false
            }
            userAnswer == correctAnswer -> ++correctScore
            else -> ++incorrectScore
        }

    }
//    val messageResId = when{
//        quizViewModel.isCheater -> R.string.judgement_toast
//        userAnswer == correctAnswer -> R.string.correct_toast
//        else -> R.string.incorrect_toast
//    }
//    Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
//    .show()
//    if (userAnswer == correctAnswer) {
//        ++correctScore
//    } else {
//        ++incorrectScore
//    }

    private fun updateQuestion() {

        if (checkAllQuestionsAnswered(quizViewModel.questionBank)){
            val gradePercent = 100.00*(correctScore/(correctScore + incorrectScore))
            Toast.makeText(this, "Quiz Complete! Grade Percent: $gradePercent %   Times Cheated: $cheatedScore", Toast.LENGTH_LONG)
                .show()
        }
        // test notification
//        else {
//
//            Toast.makeText(this, "Test Notification: $correctScore ", Toast.LENGTH_SHORT)
//                .show()
//        }
//        val questionTextResId = questionBank[currentIndex].textResID
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
    }

    private fun checkAllQuestionsAnswered(instances: List<Question>): Boolean {
        return instances.all { it.answered }
    }

    private fun questionAnswered(){
        val alreadyAnsweredRes = R.string.question_already_answered
        Toast.makeText(this, alreadyAnsweredRes, Toast.LENGTH_SHORT)
            .show()


    }


}

