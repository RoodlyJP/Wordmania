package com.example.wordmania.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wordmania.R
import com.example.wordmania.ui.theme.WordmaniaTheme

//The different game modes
enum class GameMode {
    HiddenVowels,
    HiddenConsonants,
}
enum class Screen(val title: String) {
    Start(title = ""),
    Mode(title = "Mode"),
    Game(title = "Game"),
    Result(title = "Result")
}

//The first screen the user sees with a start button and a welcome message
@Composable
fun StartScreen(
    startButtonClicked: () -> Unit,
    modifier: Modifier
) {
    Surface(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.welcome_to))
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold)
                    ) {
                        append(stringResource(R.string.app_name_upper))
                    }
                },
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier

            )
            Spacer(
                modifier = Modifier
                    .height(24.dp)
            )
            Button(
                onClick = startButtonClicked,
                elevation = ButtonDefaults.buttonElevation(4.dp),
                modifier = Modifier
                    .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
            ) {
                Text(
                    text = stringResource(R.string.start),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}

//A screen to display the game mode buttons
@Composable
fun GameMode(
    onHideVowelsClicked: (GameMode) -> Unit,
    onHideConsonantsClicked: (GameMode) -> Unit,
    modifier: Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        ModeButton(
            buttonText = stringResource(R.string.hide_vowels),
            buttonValue = GameMode.HiddenVowels,
            onButtonClicked = onHideVowelsClicked,
            modifier = Modifier
        )
        ModeButton(
            buttonText = stringResource(R.string.hide_consonants),
            buttonValue = GameMode.HiddenConsonants,
            onButtonClicked = onHideConsonantsClicked,
            modifier = Modifier
        )
    }
}

//A centered top app bar with an optional back button
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordmaniaTopAppBar(
    currentScreen: String,
    navigateUP: () -> Unit,
    canNavigateBack: Boolean,
    modifier: Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = currentScreen,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            if(canNavigateBack) {
                IconButton(
                    onClick = navigateUP
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Composable
fun MainScreen(
    currentWord: String,
    score: Int,
    wordCount: Int,
    userGuess: String,
    isUserGuessWrong: Boolean,
    onUserGuessChanged: (String) -> Unit,
    onSubmitClicked: () -> Unit,
    onSkipClicked: () -> Unit,
    modifier: Modifier
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxHeight()
    ) {
//      Top row with word count and score
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.word, wordCount),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(R.string.score, score),
                style = MaterialTheme.typography.titleLarge
            )
        }

//      Card for the word with hole and a text field to enter the user guess
        Card(
            modifier = modifier
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier

            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = currentWord,
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = stringResource(R.string.instruction),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                    )
                    OutlinedTextField(
                        value = userGuess,
                        onValueChange = onUserGuessChanged,
                        shape = MaterialTheme.shapes.large,
                        singleLine = true,
                        label = {
                            if(isUserGuessWrong) {
                                Text(
                                    text = stringResource(R.string.guess_is_wrong_try_again)
                                )
                            } else {
                                Text(
                                    text = stringResource(R.string.enter_your_guess)
                                )
                            }
                        },
                        keyboardActions = KeyboardActions(
                            onDone = { onSubmitClicked() }
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        isError = isUserGuessWrong,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }

//      Submit and skip buttons
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            OutlinedButton(
                onClick = onSkipClicked,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.skip),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .width(16.dp)
            )
            Button(
                onClick = onSubmitClicked,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.submit),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// A screen to show the results for the player
@Composable
fun ResultScreen(
    starCount: Int,
    finalScore: Int,
    percentage: Double,
    wordSkipped: Int,
    onRestartClicked: () -> Unit,
    onExitClicked: () -> Unit,
    modifier: Modifier
) {
    var i = 1
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Card {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.game_over),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                ) {
                    while(i <= 5) {
                        if(i <= starCount) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.Yellow,
                                modifier = Modifier
                                    .size(60.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier
                                    .size(60.dp)
                            )
                        }
                        i++
                    }
                }
                Text(
                    text = stringResource(R.string.final_score),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                )
                Text(
                    text = stringResource(R.string.d_10, finalScore),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
        }
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        Column {
            Text(
                text = "Game Statistics",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Score",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = "$finalScore",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            HorizontalDivider(
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color,
                modifier = Modifier
                    .padding(vertical = 4.dp,horizontal = 16.dp)
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Skipped",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = wordSkipped.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            HorizontalDivider(
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color,
                modifier = Modifier
                    .padding(vertical = 4.dp,horizontal = 16.dp)
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Percentage",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)
                )
            }

        }
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            OutlinedButton(
                onClick = onExitClicked,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.exit),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .width(16.dp)
            )
            Button(
                onClick = onRestartClicked,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.restart),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}

//A mode button composable
@Composable
fun ModeButton(
    buttonText: String,
    buttonValue: GameMode,
    onButtonClicked: (GameMode) -> Unit,
    modifier: Modifier
) {
    Button(
        onClick = { onButtonClicked(buttonValue) },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = buttonText,
            style = MaterialTheme.typography.headlineSmall,
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogBox(
    onQuitGame: () -> Unit,
    onResumeGame: () -> Unit,
    modifier: Modifier
) {
    BasicAlertDialog(
        onDismissRequest = onResumeGame,
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .sizeIn(maxHeight = 300.dp, maxWidth = 300.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.alert),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(8.dp)
            )
            Text(
                text = stringResource(R.string.do_you_want_to_end_the_game),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextButton(
                    onClick = onQuitGame,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = "Yes",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(
                    modifier = Modifier.width(16.dp)
                )
                TextButton(
                    onClick = onResumeGame,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = "No"
                    )
                }
            }
        }
    }
}
@Composable
@Preview(showBackground = true)
fun DialogBoxPreview() {
    DialogBox(
        onQuitGame = {},
        onResumeGame = {},
        modifier = Modifier
    )
}
@Composable
@Preview(showBackground = true)
fun ResultScreenPreview() {
    ResultScreen(
        percentage = 70.0,
        wordSkipped = 2,
        starCount = 3,
        finalScore = 7,
        onExitClicked = {},
        onRestartClicked = {},
        modifier = Modifier
    )
}
@Composable
@Preview(showBackground = true)
fun MainScreenPreview() {
    MainScreen(
        currentWord = "M*NT*GN*",
        score = 0,
        wordCount = 0,
        userGuess = "",
        isUserGuessWrong = false,
        onUserGuessChanged = {},
        onSubmitClicked = {},
        onSkipClicked = {},
        modifier = Modifier
    )
}
@Composable
@Preview(showBackground = true)
fun WordmaniaTopAppBarPreview() {
    WordmaniaTheme {
        WordmaniaTopAppBar(
            currentScreen = stringResource(R.string.app_name_upper),
            navigateUP = {},
            canNavigateBack = true,
            modifier = Modifier
        )
    }
}

@Composable
@Preview(showBackground = true)
fun GameModePreview() {
    WordmaniaTheme {
        GameMode(
            onHideVowelsClicked = {},
            onHideConsonantsClicked = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
@Preview(showBackground = true)
fun StartScreenPreview() {
    WordmaniaTheme {
        StartScreen(
            startButtonClicked = {},
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

