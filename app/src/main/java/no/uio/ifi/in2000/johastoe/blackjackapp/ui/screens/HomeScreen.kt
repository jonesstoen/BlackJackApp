package no.uio.ifi.in2000.johastoe.blackjackapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.in2000.johastoe.blackjackapp.viewModel.GameViewModel

//import androidx.navigation.compose.rememberNavController


@Composable
fun HomeScreen(navController: NavController, viewModel: GameViewModel = viewModel()) {
    val highScore = viewModel.highScore.observeAsState(0)

    Box(
        modifier = Modifier.fillMaxSize().background(color = androidx.compose.ui.graphics.Color(0xFF35654D)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = no.uio.ifi.in2000.johastoe.blackjackapp.R.drawable.game),
                contentDescription = "BlackJack Logo",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Current High Score: ${highScore.value}",
                color = Color.White
                )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("game") }) {
                Text(text = "Start Game")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("high_scores") }) {
                Text(text = "High Scores")
            }
        }
    }
}