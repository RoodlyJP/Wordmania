package com.example.wordmania.ui

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


@Composable
fun WordmaniaApp(
    navController: NavHostController = rememberNavController(),
) {
    val wordmaniaViewModel: WordmaniaViewModel = viewModel()
    val wordmaniaUiState by wordmaniaViewModel.uiState.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val context = LocalContext.current

    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.Start.name
    )

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            WordmaniaTopAppBar(
                currentScreen = currentScreen.title,
                navigateUP = {
                    if(currentScreen != Screen.Game) {
                        navController.navigateUp()
                        wordmaniaViewModel.resetGame()
                    } else {
                        showDialog = true
                    }
                             },
                canNavigateBack = navController.previousBackStackEntry != null,
                modifier = Modifier
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.Start.name) {
                StartScreen(
                    startButtonClicked = { navController.navigate(Screen.Mode.name) },
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            composable(route = Screen.Mode.name) {

                GameMode(
                    onHideVowelsClicked = {
                        wordmaniaViewModel.updateGameMode(GameMode.HiddenVowels)
                        wordmaniaViewModel.startPlay()
                        navController.navigate(Screen.Game.name)
                                          },
                    onHideConsonantsClicked = {
                        wordmaniaViewModel.updateGameMode(GameMode.HiddenConsonants)
                        wordmaniaViewModel.startPlay()
                        navController.navigate(Screen.Game.name)
                                              },
                    modifier = Modifier
                        .fillMaxSize()
                )

            }
            composable(route = Screen.Game.name) {
                if(wordmaniaUiState.endGame) {
                    ResultScreen(
                        starCount = wordmaniaViewModel.starCalc(),
                        finalScore = wordmaniaUiState.score,
                        percentage = wordmaniaViewModel.percentageCalc(),
                        wordSkipped = wordmaniaUiState.skipCount,
                        onExitClicked = {
                            navController.popBackStack()
                            wordmaniaViewModel.resetGame()
                            (context as Activity).finish()
                        },
                        onRestartClicked = {
                            wordmaniaViewModel.resetGame()
                            navController.popBackStack()
                            navController.navigate(Screen.Mode.name)
                                           },
                        modifier = Modifier
                            .fillMaxSize()
                    )
                } else {
                    MainScreen(
                        currentWord = wordmaniaUiState.currentWord,
                        score = wordmaniaUiState.score,
                        wordCount = wordmaniaUiState.wordCount,
                        userGuess = wordmaniaUiState.userGuess,
                        isUserGuessWrong = wordmaniaUiState.isGuessWrong,
                        onUserGuessChanged = { wordmaniaViewModel.updateUserGuess(it) },
                        onSkipClicked = { wordmaniaViewModel.skipWord() },
                        onSubmitClicked = { wordmaniaViewModel.checkUserGuess() },
                        modifier = Modifier
                    )
                    if(showDialog) {
                        DialogBox(
                            onResumeGame = { showDialog = false },
                            onQuitGame = {
                                showDialog = false
                                navController.popBackStack()
                                wordmaniaViewModel.resetGame()
                                navController.navigate(Screen.Mode.name)
                            },
                            modifier = Modifier
                        )
                    }
                }
            }
        }

    }
}