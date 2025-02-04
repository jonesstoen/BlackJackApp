package no.uio.ifi.in2000.johastoe.blackjackapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.uio.ifi.in2000.johastoe.blackjackapp.viewModel.GameViewModel

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun HighScoreScreen(navController: NavController, viewModel: GameViewModel) {
    val highScore = viewModel.highScore.observeAsState(0)
    val highScoreName = viewModel.highScoreName.observeAsState(null)
    val previousHighScores = viewModel.previousHighScores.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var playerName by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "New High Score!") },
            text = {
                Column {
                    Text(text = "Enter your name:")
                    TextField(
                        value = playerName,
                        onValueChange = { playerName = it },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        )
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.updateHighScore(playerName)
                    showDialog = false
                }) {
                    Text("OK")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF35654D)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "High Scores",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
//            Text(
//                text = "High Score: ${highScore.value} $ by ${highScoreName.value ?: "Unknown"}",
//                style = MaterialTheme.typography.headlineMedium,
//                color = MaterialTheme.colorScheme.onPrimary
//            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(previousHighScores.value.sortedByDescending { it.first }) { (score, name) ->
                    Text(
                        text = "Score: $score $ by ${name ?: "Unknown"}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("home") }) {
                Text(text = "Back to Home")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.resetHighScores() }) {
                Text(text = "Reset High Scores")
            }
        }
    }
}