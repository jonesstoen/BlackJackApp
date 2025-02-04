package no.uio.ifi.in2000.johastoe.blackjackapp.viewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import no.uio.ifi.in2000.johastoe.blackjackapp.ui.models.Betting
import no.uio.ifi.in2000.johastoe.blackjackapp.ui.models.Card
import no.uio.ifi.in2000.johastoe.blackjackapp.ui.models.Deck


import android.app.Application
import androidx.lifecycle.AndroidViewModel

import no.uio.ifi.in2000.johastoe.blackjackapp.Utils.HighScoreManager

class GameViewModel(application: Application, private val savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    private val highScoreManager = HighScoreManager(application)

    private val deck = Deck()
    private val betting = Betting(100.0f) // Initial balance

    private val _isInitialState = MutableLiveData(true)
    val isInitialState: LiveData<Boolean> = _isInitialState

    private val _isGameInProgress = MutableLiveData(true)
    val isGameInProgress: LiveData<Boolean> = _isGameInProgress

    private val _playerHand = MutableLiveData<List<Card>>(emptyList())
    val playerHand: LiveData<List<Card>> = _playerHand

    private val _dealerHand = MutableLiveData<List<Card>>(emptyList())
    val dealerHand: LiveData<List<Card>> = _dealerHand

    private val _playerSum = MutableLiveData(0)
    val playerSum: LiveData<Int> = _playerSum

    private val _dealerSum = MutableLiveData(0)
    val dealerSum: LiveData<Int> = _dealerSum

    private val _playerHits = MutableLiveData(0)
    val playerHits: LiveData<Int> = _playerHits

    private val _gameResult = MutableLiveData<String?>(null)
    val gameResult: LiveData<String?> = _gameResult

    private val _balance = savedStateHandle.getLiveData("balance", 100.0f)
    val balance: LiveData<Float> = _balance

    private val _atStake = savedStateHandle.getLiveData("atStake", 0.0f)
    val atStake: LiveData<Float> = _atStake

    //hih score stuff
    private val _highScore = MutableLiveData(highScoreManager.getHighScore().first)
    val highScore: LiveData<Int> = _highScore

    private val _highScoreName = MutableLiveData(highScoreManager.getHighScore().second)
    val highScoreName: LiveData<String?> = _highScoreName

    private val _previousHighScores = MutableLiveData<List<Pair<Int, String?>>>(highScoreManager.getPreviousHighScores())
    val previousHighScores: LiveData<List<Pair<Int, String?>>> = _previousHighScores

    private val _showHighScoreDialog = MutableLiveData(false)
    val showHighScoreDialog: LiveData<Boolean> = _showHighScoreDialog

    private var playerAceCount = 0
    private var dealerAceCount = 0

    init {
        startGame()
    }

    fun startGame() {
        resetGame()
        dealInitialCards()
        _isInitialState.value = true
        _isGameInProgress.value = true
        _playerHits.value = 0
        _gameResult.value = null
    }

    private fun resetGame() {
        deck.buildDeck()
        deck.shuffleDeck()
        _playerHand.value = emptyList()
        _dealerHand.value = emptyList()
        _playerSum.value = 0
        _dealerSum.value = 0
        playerAceCount = 0
        dealerAceCount = 0
    }

    private fun dealInitialCards() {
        val newPlayerHand = mutableListOf<Card>()
        val newDealerHand = mutableListOf<Card>()
        var newPlayerSum = 0
        var newDealerSum = 0

        for (i in 0 until 2) {
            val playerCard = deck.drawCard()
            newPlayerHand.add(playerCard)
            newPlayerSum += playerCard.getValue()
            if (playerCard.isAce()) playerAceCount++

            val dealerCard = deck.drawCard()
            newDealerHand.add(dealerCard)
            newDealerSum += dealerCard.getValue()
            if (dealerCard.isAce()) dealerAceCount++
        }

        _playerHand.value = newPlayerHand
        _dealerHand.value = newDealerHand
        _playerSum.value = newPlayerSum
        _dealerSum.value = newDealerSum
    }

    fun hit() {
        _isInitialState.value = false
        val card = deck.drawCard()
        val newPlayerHand = _playerHand.value?.toMutableList() ?: mutableListOf()
        newPlayerHand.add(card)
        _playerHand.value = newPlayerHand

        _playerSum.value = (_playerSum.value ?: 0) + card.getValue()
        if (card.isAce()) playerAceCount++
        if (reducePlayerAce() > 21) {
            _isGameInProgress.value = false
            _gameResult.value = "Dealer Wins"
            betting.loseBet()
            updateBettingState()
        }

        _playerHits.value = (_playerHits.value ?: 0) + 1
        if (_playerHits.value == 3) {
            _isGameInProgress.value = false
            determineWinner()
        }
    }

    fun stay() {
        _isInitialState.value = false
        _isGameInProgress.value = false
        while ((_dealerSum.value ?: 0) < 17) {
            val card = deck.drawCard()
            val newDealerHand = _dealerHand.value?.toMutableList() ?: mutableListOf()
            newDealerHand.add(card)
            _dealerHand.value = newDealerHand

            _dealerSum.value = (_dealerSum.value ?: 0) + card.getValue()
            if (card.isAce()) dealerAceCount++
        }
        determineWinner()
    }

    private fun determineWinner() {
        val playerTotal = reducePlayerAce()
        val dealerTotal = reduceDealerAce()

        _gameResult.value = when {
            playerTotal > 21 -> "Dealer Wins"
            dealerTotal > 21 -> "Player Wins"
            playerTotal > dealerTotal -> "Player Wins"
            playerTotal < dealerTotal -> "Dealer Wins"
            else -> "It's a Tie"
        }

        when (_gameResult.value) {
            "Player Wins" -> {
                betting.winBet()
                checkHighScore()
            }
            "Dealer Wins" -> betting.loseBet()
            "It's a Tie" -> betting.tieBet()
        }
        updateBettingState()
    }

    private fun reducePlayerAce(): Int {
        while ((_playerSum.value ?: 0) > 21 && playerAceCount > 0) {
            _playerSum.value = (_playerSum.value ?: 0) - 10
            playerAceCount--
        }
        return _playerSum.value ?: 0
    }

    private fun reduceDealerAce(): Int {
        while ((_dealerSum.value ?: 0) > 21 && dealerAceCount > 0) {
            _dealerSum.value = (_dealerSum.value ?: 0) - 10
            dealerAceCount--
        }
        return _dealerSum.value ?: 0
    }

    fun placeBet(amount: Float) {
        if (betting.sufficientBalance(amount)) {
            betting.bet(amount)
            _isInitialState.value = false
            updateBettingState()
        } else {
            println("Insufficient balance")
        }
    }

    private fun updateBettingState() {
        _balance.value = betting.balance
        _atStake.value = betting.atStake
        savedStateHandle["balance"] = betting.balance
        savedStateHandle["atStake"] = betting.atStake
    }

    fun updateHighScore(name: String) {
        val currentScore = _balance.value?.toInt() ?: 0
        if (currentScore > highScoreManager.getHighScore().first) {
            highScoreManager.setHighScore(currentScore, name)
            _highScore.value = currentScore
            _highScoreName.value = name
            _previousHighScores.value = highScoreManager.getPreviousHighScores()
        }
    }
    fun setShowHighScoreDialog(show: Boolean) {
        _showHighScoreDialog.value = show
    }
    fun checkHighScore() {
        val currentScore = _balance.value?.toInt() ?: 0
        if (currentScore > highScoreManager.getHighScore().first) {
            _showHighScoreDialog.value = true
        }
    }
    fun resetHighScores() {
        highScoreManager.resetHighScores()
        _highScore.value = 0
        _highScoreName.value = null
        _previousHighScores.value = emptyList()
    }

}