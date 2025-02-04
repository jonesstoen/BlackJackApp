package no.uio.ifi.in2000.johastoe.blackjackapp.ui.models

data class Card(val rank: String, val suit: String) {
    fun getValue(): Int {
        return when (rank) {
            "A" -> 11
            "J", "Q", "K" -> 10
            else -> rank.toInt()
        }
    }

    fun isAce(): Boolean {
        return rank == "A"
    }

    fun getImagePath(): String {
        return "cards/${toString()}.png"
    }

    override fun toString(): String {
        return "${suit.first()}_${rank}"
    }
}