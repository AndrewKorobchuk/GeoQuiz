package com.sherlock.androidprogramming4e.geoquiz

import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel(){

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    private var currentIndex = 0
    private var responseBank = arrayOfNulls<Response>(questionBank.size)
    private var countAnswers = 0
    private var allAnswers = false

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun setCurrentIndex(index: Int){
        currentIndex = index
    }

    fun getCurrentIndex() :Int {
        return currentIndex
    }

    fun move(index: Int) {
        if(currentIndex==0 && index==-1){
            currentIndex = questionBank.size-1
        }else{
            currentIndex = (currentIndex + index) % questionBank.size
        }
    }
    fun getCheater():Boolean?{
        if(responseBank[currentIndex]==null){
            return null
        }else {
            return responseBank[currentIndex]!!.isCheater
        }
    }

    fun checkAnswer(userAnswer: Boolean, isCheater: Boolean): Boolean{
        val messageResId = if(isCheater){
            responseBank[currentIndex] = Response(questionBank[currentIndex], 0, isCheater)
            false
        }else{
            if (userAnswer == currentQuestionAnswer) {
                responseBank[currentIndex] = Response(questionBank[currentIndex], 1, isCheater)
                true
            } else {
                responseBank[currentIndex] = Response(questionBank[currentIndex], 0, isCheater)
                false
            }
        }

        countAnswers++
        if(countAnswers == responseBank.size){
            allAnswers = true
        }
        return messageResId
    }

    fun isAllAnswers(): Boolean {
        return allAnswers
    }

    fun getResult(): Double {
        var i =0.0
        responseBank.forEach { element ->
            if(!element!!.isCheater){
                i = element!!.answer + i
            }
        }
        return (i/countAnswers)*100
    }

    fun answerEnable():Boolean {
        return responseBank[currentIndex]!=null
    }

}