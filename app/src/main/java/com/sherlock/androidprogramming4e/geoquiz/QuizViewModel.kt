package com.sherlock.androidprogramming4e.geoquiz

import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

/**
 * Функция onCleared() вызывается непосредственно перед уничтожением ViewModel.
 * В этом месте удобно выполнять уборку мусора, например снимать наблюдение с источника данных.
 */
class QuizViewModel : ViewModel(){

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    private var currentIndex = 0
    var isCheater = false
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

    fun checkAnswer(userAnswer: Boolean): Boolean{

        val messageResId = if (userAnswer == currentQuestionAnswer) {
            responseBank[currentIndex] = Response(questionBank[currentIndex],1)
            true
        } else {
            responseBank[currentIndex] = Response(questionBank[currentIndex],0)
            false
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
            i = element!!.answer + i
        }
        return (i/countAnswers)*100
    }

    fun answerEnable():Boolean {
        return responseBank[currentIndex]!=null
    }

}