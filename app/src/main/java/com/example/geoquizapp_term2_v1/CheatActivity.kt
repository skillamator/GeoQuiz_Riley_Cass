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

private const val EXTRA_ANSWER_IS_TRUE = "com.example.geoquizapp_term2_v1.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.example.geoquizapp_term2_v1.answer_shown"

class CheatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheatBinding

    private var answerIsTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        binding.showAnswerButton.setOnClickListener(){
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            binding.answerTextView.setText(answerText)
            setAnswerShownResult(true)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setAnswerShownResult(isAnswerShown:Boolean){
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object{
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent{
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}