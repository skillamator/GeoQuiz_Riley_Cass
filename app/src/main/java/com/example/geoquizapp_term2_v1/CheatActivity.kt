package com.example.geoquizapp_term2_v1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
//import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.geoquizapp_term2_v1.databinding.ActivityCheatBinding

//Key initialization
private const val EXTRA_ANSWER_IS_TRUE = "com.example.geoquizapp_term2_v1.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.example.geoquizapp_term2_v1.answer_shown"

//CheatActivity class & binding initialization
class CheatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheatBinding

//Variable initialization
    private var answerIsTrue = false

//onCreate function for CheatActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)

//Stores whether the current Question is 'true' or 'false'
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

//showAnswerButton listener
        binding.showAnswerButton.setOnClickListener(){
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
// Sets/Shows the answer to current question
            binding.answerTextView.setText(answerText)
            setAnswerShownResult(true)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

//setAnswerShownResults function - passes data between CheatActivity and MainActivity - passes whether the user ACTUALLY cheated or not
    private fun setAnswerShownResult(isAnswerShown:Boolean){
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

//newIntent function - passes data between MainActivity and CheatActivity - passes whether current question is 'true' or 'false'
    companion object{
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent{
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}