package no.uio.ifi.in2000.johastoe.blackjackapp

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.johastoe.blackjackapp.ui.screens.GameScreen
import no.uio.ifi.in2000.johastoe.blackjackapp.ui.screens.HighScoreScreen
import no.uio.ifi.in2000.johastoe.blackjackapp.ui.screens.HomeScreen
import no.uio.ifi.in2000.johastoe.blackjackapp.ui.theme.BlackJackAppTheme
import no.uio.ifi.in2000.johastoe.blackjackapp.viewModel.GameViewModel

class MainActivity : ComponentActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private val viewModel: GameViewModel by viewModels { SavedStateViewModelFactory(application, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlackJackAppTheme {
                val navController = rememberNavController()
                SetupNavGraph(navController = navController, viewModel = viewModel)
            }
        }

        // Initialize and start the MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music)
        mediaPlayer.setVolume(0.3f, 0.3f)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the MediaPlayer resources
        mediaPlayer.release()
    }
    override fun onStop() {
        super.onStop()
        // Pause the MediaPlayer when the app is closed
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        // Resume the MediaPlayer when the app is opened
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }
    }
}
@Composable
fun SetupNavGraph(navController: NavHostController, viewModel: GameViewModel) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController = navController) }
        composable("game") { GameScreen(viewModel = viewModel) }
        composable("high_scores") { HighScoreScreen(navController = navController, viewModel = viewModel) }
    }
}
