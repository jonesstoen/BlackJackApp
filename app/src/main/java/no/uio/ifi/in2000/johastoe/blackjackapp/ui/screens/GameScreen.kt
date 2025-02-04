package no.uio.ifi.in2000.johastoe.blackjackapp.ui.screens


import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.johastoe.blackjackapp.R


import no.uio.ifi.in2000.johastoe.blackjackapp.ui.components.CardComponent
import no.uio.ifi.in2000.johastoe.blackjackapp.ui.components.getCardDrawableResId
import no.uio.ifi.in2000.johastoe.blackjackapp.viewModel.GameViewModel
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun GameScreen(viewModel: GameViewModel) {
    val context = LocalContext.current
    val playerHand by viewModel.playerHand.observeAsState(emptyList())
    val dealerHand by viewModel.dealerHand.observeAsState(emptyList())
    val isGameInProgress by viewModel.isGameInProgress.observeAsState(true)
    val isInitialState by viewModel.isInitialState.observeAsState(true)
    val playerHits by viewModel.playerHits.observeAsState(0)
    val gameResult by viewModel.gameResult.observeAsState(null)
    val balance by viewModel.balance.observeAsState(0.00f)
    val atStake by viewModel.atStake.observeAsState(0.00f)
    val showHighScoreDialog by viewModel.showHighScoreDialog.observeAsState(false)

    val hitSound = remember { MediaPlayer.create(context, R.raw.card_draw) }
    val winSound = remember { MediaPlayer.create(context, R.raw.coins_spilling) }
    val loseSound = remember { MediaPlayer.create(context, R.raw.lose) }
    val betPlacedSound = remember { MediaPlayer.create(context, R.raw.bet_placed) }

    var customBet by remember { mutableStateOf("") }
    var playerName by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(gameResult) {
        gameResult?.let {
            if (it == "Player Wins") {
                winSound.start()
            } else if (it == "Dealer Wins") {
                loseSound.start()
            }
        }
    }

    if (showHighScoreDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setShowHighScoreDialog(false) },
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
                    viewModel.setShowHighScoreDialog(false)
                }) {
                    Text("OK")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF35654D))
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                })
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Balance: ${String.format("%.2f", balance)}$\nAt Stake: ${String.format("%.2f", atStake)}$",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 12.sp),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Dealer's Hand:",
                color = Color.White
                )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(dealerHand) { card ->
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInHorizontally() + fadeIn(),
                        exit = slideOutHorizontally() + fadeOut()
                    ) {
                        val cardResId = if (isInitialState || dealerHand.indexOf(card) == 0) {
                            R.drawable.back
                        } else {
                            getCardDrawableResId(card)
                        }
                        CardComponent(cardResId = cardResId)
                    }
                }
            }
            gameResult?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = it, style = MaterialTheme.typography.headlineMedium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Player's Hand:",
                color = Color.White)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(if (playerHand.size >= 4) 2.dp else 8.dp)
            ) {
                items(playerHand) { card ->
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInHorizontally() + fadeIn(),
                        exit = slideOutHorizontally() + fadeOut()
                    ) {
                        CardComponent(cardResId = if (isInitialState) R.drawable.back else getCardDrawableResId(card))
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(onClick = {
                    viewModel.hit()
                    hitSound.start()
                },
                    enabled = isGameInProgress
                ) {
                    Text(text = "Hit")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {
                    viewModel.stay()
                },
                    enabled = isGameInProgress
                ) {
                    Text(text = "Stay")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(
                visible = gameResult != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Button(onClick = { viewModel.startGame() }, modifier = Modifier.padding(bottom = 16.dp)) {
                    Text(text = "Play again")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (isInitialState) {
                Row {
                    TextField(
                        value = customBet,
                        onValueChange = { customBet = it },
                        label = { Text("Enter Amount") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        ),
                        modifier = Modifier.width(150.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp).width(16.dp))
                    Button(onClick = {
                        val betAmount = customBet.toFloatOrNull() ?: 0.0f
                        viewModel.placeBet(betAmount)
                        betPlacedSound.start()
                    }) {
                        Text(text = "Place Bet")
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}




//@Preview
//@Composable
//fun PreviewGameScreen() {
//   GameScreen(GameViewModel())
//}