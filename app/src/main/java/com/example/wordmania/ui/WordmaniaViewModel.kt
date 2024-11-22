package com.example.wordmania.ui

import androidx.lifecycle.ViewModel
import com.example.wordmania.data.MAX_WORD
import com.example.wordmania.data.SCORE_INCREASE
import com.example.wordmania.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WordmaniaViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(WordmaniaUIState())
    val uiState: StateFlow<WordmaniaUIState> = _uiState.asStateFlow()
//  The word with hole
    private lateinit var currentWord: String
//    List of already used words
    private var usedWords: MutableSet<String> = mutableSetOf()


    fun pickRandomWordAndMakeHole(): String {
        currentWord = allWords.random()
        if(usedWords.contains(currentWord)) {
            return pickRandomWordAndMakeHole()
        } else {
            usedWords.add(currentWord)
            return makeHole(currentWord)
        }
    }
    private fun makeHole(word: String): String {
        val vowels = setOf('a', 'e', 'i', 'o', 'u', 'y')
        val consonants = setOf('b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'z')
        val builder = StringBuilder(word)
        if(uiState.value.gameMode == GameMode.HiddenVowels) {
            for (v in word) {
                if (vowels.contains(v)) {
                    builder.replace(builder.indexOf(v), builder.indexOf(v) + 1, "*")
                }
            }
        } else {
            for (v in word) {
                if (consonants.contains(v)) {
                    builder.replace(builder.indexOf(v), builder.indexOf(v) + 1, "*")
                }
            }
        }
        return builder.toString()
    }

    fun updateGameMode(gameMode: GameMode) {
        _uiState.value = WordmaniaUIState(gameMode = gameMode)
    }

    fun startPlay() {
        _uiState.update { currentState ->
            currentState.copy(
                currentWord = pickRandomWordAndMakeHole()
            )
        }
    }
    fun updateUserGuess(guessedWord: String) {
        _uiState.update { currentState ->
            currentState.copy(
                userGuess = guessedWord.trim()
            )
        }
    }
    fun checkUserGuess() {
        if(_uiState.value.wordCount == MAX_WORD) {
            if(uiState.value.userGuess.equals(currentWord, ignoreCase = true)) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isGuessWrong = false,
                        score = currentState.score.plus(SCORE_INCREASE),
                        endGame = true
                    )
                }
            } else {
                _uiState.update { currentState ->
                    currentState.copy(
                        isGuessWrong = true
                    )
                }
            }
        } else {
            if(uiState.value.userGuess.equals(currentWord, ignoreCase = true)) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isGuessWrong = false,
                        score = currentState.score.plus(SCORE_INCREASE),
                        wordCount = currentState.wordCount.inc(),
                        currentWord = pickRandomWordAndMakeHole()
                    )
                }
            } else {
                _uiState.update { currentState ->
                    currentState.copy(
                        isGuessWrong = true
                    )
                }
            }
        }
        updateUserGuess("")
    }
    fun skipWord() {
        _uiState.update { currentState ->
            if(_uiState.value.wordCount == MAX_WORD) {
                currentState.copy(
                    endGame = true,
                    skipCount = currentState.skipCount.inc(),
                )
            } else {
                currentState.copy(
                    wordCount = currentState.wordCount.inc(),
                    skipCount = currentState.skipCount.inc(),
                    currentWord = pickRandomWordAndMakeHole()
                )
            }
        }
    }
    fun percentageCalc(): Double {
        return (_uiState.value.score.toDouble() / _uiState.value.wordCount.toDouble())*10
    }
    fun starCalc(): Int {
        return (_uiState.value.score.toDouble() / 20).toInt()
    }

    fun resetGame() {
        usedWords.clear()
        _uiState.value = WordmaniaUIState()

    }
}