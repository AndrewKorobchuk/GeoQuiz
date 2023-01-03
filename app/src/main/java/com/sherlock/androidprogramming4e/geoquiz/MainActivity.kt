package com.sherlock.androidprogramming4e.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevButtonImg: ImageButton
    private lateinit var nextButtonImg: ImageButton

    private lateinit var questionTextView: TextView

    //private var responseBank = arrayOfNulls<Response>(questionBank.size)



    /**
     * Использование lazy допускает применение свойства quizViewModel как val, а не var.
     * Это здорово, потому что вам нужно захватить и сохранить QuizViewModel,
     * лишь когда создается экземпляр activity,
     * поэтому quizViewModel получает значение только один раз.
     * Что еще более важно, использование lazy означает, что расчет и назначение quizViewModel
     * не будет происходить, пока вы не запросите доступ к quizViewModel впервые.
     * Это хорошо, потому что вы не можете безопасно получить доступ к ViewModel до выполнения
     * Activity.onCreate(...).
     * Если вы пытаетесь вызвать ViewModelProviders.of(this).get(QuizViewModel::class.java) до
     * Activity.onCreate(...), ваше приложение вылетит с исключением IllegalStateException.
     */
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.setCurrentIndex(currentIndex)


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

        quizViewModel.move(index)
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        trueButton.isEnabled = !quizViewModel.answerEnable()
        falseButton.isEnabled = !quizViewModel.answerEnable()
    }

    private fun checkAnswer(userAnswer: Boolean) {


        //val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = if (quizViewModel.checkAnswer(userAnswer)) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        updateQuestion(1)
        if(quizViewModel.isAllAnswers()){
            showResult()
        }
    }


    private fun showResult(){
        val result = quizViewModel.getResult()
        Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show()
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

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.getCurrentIndex())
    }
}