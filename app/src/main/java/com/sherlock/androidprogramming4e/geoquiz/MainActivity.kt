package com.sherlock.androidprogramming4e.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
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
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private lateinit var cheatButton: Button
    private lateinit var prevButtonImg: ImageButton
    private lateinit var nextButtonImg: ImageButton

    private lateinit var questionTextView: TextView

    private lateinit var aboutButton: Button
    private var isCheater = false

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

    @SuppressLint("RestrictedApi")
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
        cheatButton = findViewById(R.id.cheat_button)
        prevButtonImg = findViewById(R.id.prev_button_img)
        nextButtonImg = findViewById(R.id.next_button_img)

        questionTextView = findViewById(R.id.question_text_view)

        aboutButton = findViewById(R.id.about_button)
        aboutButton.setOnClickListener { view: View ->
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

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

        cheatButton.setOnClickListener{view: View ->
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                /**
                 * Используем класс ActivityOptions, чтобы настроить запуск activity.
                 * В приведенном выше коде вы вызываете функцию makeClipRevealAnimation(...),
                 * чтобы указать, что CheatActivity должен использовать анимацию отображения.
                 * Значения, которые вы передаете в makeClipRevealAnimation(...), указывают на объект,
                 * который следует использовать как источник анимации (в данном случае кнопка CHEAT!),
                 * положение x и y (относительно источника) для начала отображения новой activity,
                 * а также начальную ширину и высоту новой activity
                 */
                val options = ActivityOptions
                    .makeClipRevealAnimation(view, 0, 0, view.width, view.height)

                /**
                 * Вызваем функцию options.toBundle(), чтобы упаковать ActivityOptions в объект Bundle,
                 * а затем передаем их в функцию startActivityForResult(...).
                 * ActivityManager использует пакет опций, чтобы определить, как вывести вашу activity
                 * на экран
                 */
                startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
            }else{
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }
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

        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId_ = if (quizViewModel.checkAnswer(userAnswer,false)) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        val messageResId = when {
                quizViewModel.getCheater()==true -> R.string.judgment_toast
                userAnswer == correctAnswer -> R.string.correct_toast
                else -> R.string.incorrect_toast
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

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            if(isCheater){
                if(quizViewModel.getCheater()==null) {
                    quizViewModel.checkAnswer(false,true)
                    if(quizViewModel.addCountCheaters()==2){
                        cheatButton.isEnabled = false
                    }
                    trueButton.isEnabled = false
                    falseButton.isEnabled = false

                    if(quizViewModel.isAllAnswers()){
                        showResult()
                    }
                }
            }
        }
    }
}