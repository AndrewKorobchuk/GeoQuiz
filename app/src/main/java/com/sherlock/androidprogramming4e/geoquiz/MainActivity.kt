package com.sherlock.androidprogramming4e.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.makeText

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        prevButton = findViewById(R.id.prev_button)
        nextButton = findViewById(R.id.next_button)
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
            //currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion(1)
        }

        questionTextView.setOnClickListener {view: View ->
            //currentIndex = (currentIndex + 1) % questionBank.size
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
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
    }
}