package no.uio.ifi.in2000.johastoe.blackjackapp.ui.models

class Betting(var balance: Float) {
    var atStake: Float = 0.0f

    fun sufficientBalance(bet: Float): Boolean {
        return balance - bet >= 0
    }

    fun bet(amount: Float) {
        if (sufficientBalance(amount)) {
            atStake = amount
            balance -= amount
        } else {
            println("Insufficient balance")
        }
    }

    fun winBet() {
        balance += 2 * atStake
        atStake = 0.0f
    }

    fun tieBet() {
        balance += atStake
        atStake = 0.0f
    }

    fun loseBet() {
        atStake = 0.0f
    }
}