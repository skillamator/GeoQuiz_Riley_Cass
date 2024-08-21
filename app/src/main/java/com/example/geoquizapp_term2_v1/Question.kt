package com.example.geoquizapp_term2_v1
//
//class Question(questionAustralia: Int, b: Boolean) {
//
//}
import androidx.annotation.StringRes

data class Question(@StringRes val textResID: Int, val answer: Boolean, var answered: Boolean = false)