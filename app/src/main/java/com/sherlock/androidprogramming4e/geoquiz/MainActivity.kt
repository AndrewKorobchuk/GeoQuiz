package com.sherlock.androidprogramming4e.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevButtonImg: ImageButton
    private lateinit var nextButtonImg: ImageButton

    private lateinit var questionTextView: TextView


    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    var currentIndex = 0

    private var responseBank = arrayOfNulls<Response>(questionBank.size)

    private var countAnswers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val provider: ViewModelProvider = ViewModelProviders.of(this)
        val quizViewModel = provider.get(QuizViewModel::class.java)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        prevButton = findViewById(R.id.prev_button)
        nextButton = findViewById(R.id.next_button)
        prevButtonImg = findViewById(R.id.prev_button_img)
        nextButtonImg = findViewById(R.id.next_button_img)

        questionTextView = findViewById(R.id.question_text_view)


        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener { view: View ->
           checkAnswer(false)
        }

        prevButton.setOnClickListener {
            updateQuestion(-1)
        }

        nextButton.setOnClickListener {
            updateQuestion(1)
        }

        prevButtonImg.setOnClickListener {
            updateQuestion(-1)
        }

        nextButtonImg.setOnClickListener {
            updateQuestion(1)
        }

        questionTextView.setOnClickListener {view: View ->
            updateQuestion(1)
        }

        updateQuestion(0)
    }

    private fun updateQuestion(index: Int) {

        if(currentIndex==0 && index==-1){
            currentIndex = questionBank.size-1
        }else{
            currentIndex = (currentIndex + index) % questionBank.size
        }

        //currentIndex = (currentIndex + 1) % questionBank.size
        //currentIndex = (currentIndex + index) % questionBank.size
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
        trueButton.isEnabled = responseBank[currentIndex]==null
        falseButton.isEnabled = responseBank[currentIndex]==null

    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if (userAnswer == correctAnswer) {
            responseBank[currentIndex] = Response(questionBank[currentIndex],1)
            R.string.correct_toast
        } else {
            responseBank[currentIndex] = Response(questionBank[currentIndex],0)
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        updateQuestion(1)
        countAnswers++
        if(countAnswers == responseBank.size){
            showResult()
        }
    }

    private fun showResult(){
        var i =0.0
        responseBank.forEach { element ->
            i = element!!.answer + i
        }
        Toast.makeText(this, ((i/countAnswers)*100).toString(), Toast.LENGTH_SHORT).show()
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
        Log.d(TAG, "onDestroy() called")
    }
}