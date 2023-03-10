package com.sherlock.androidprogramming4e.geoquiz

import androidx.annotation.StringRes

/**
 * Аннотация @StringRes не обязательна, но мы рекомендуем включить ее по двум причинам.
 * Во-первых, аннотация помогает встроенному в Android Studio инспектору кода (под названием Lint)
 * проверить во время компиляции, что в конструкторе используется правильный строковый идентификатор
 * ресурса. Это предотвращает сбои во время выполнения, когда конструктор используется с
 * недействительным идентификатором ресурса (например, если идентификатор указывает не на строку).
 * Во-вторых, аннотация делает ваш код более читаемым для других разработчиков.
 */
data class Question(@StringRes val textResId: Int, val answer: Boolean)