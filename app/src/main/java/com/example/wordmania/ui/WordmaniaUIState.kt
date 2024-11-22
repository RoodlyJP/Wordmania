package com.example.wordmania.ui

data class WordmaniaUIState(
    val currentWord: String = "",
    val gameMode: GameMode? = null,
    val userGuess: String = "",
    val isGuessWrong: Boolean = false,
    val score: Int = 0,
    val wordCount: Int = 1,
    val endGame: Boolean = false,
    val skipCount: Int = 0,
    val scorePercentage: Double = 0.0,

)
