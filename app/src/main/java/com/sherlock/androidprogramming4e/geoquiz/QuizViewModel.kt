package com.sherlock.androidprogramming4e.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

/**
 * Функция onCleared() вызывается непосредственно перед уничтожением ViewModel.
 * В этом месте удобно выполнять уборку мусора, например снимать наблюдение с источника данных.
 */
class QuizViewModel : ViewModel(){



    init {
        Log.d(TAG, "ViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }
}