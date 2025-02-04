package no.uio.ifi.in2000.johastoe.blackjackapp.ui.models

import kotlin.random.Random

class Deck {
    private val deck: MutableList<Card> = mutableListOf()

    init {
        buildDeck()
        shuffleDeck()
    }

    fun buildDeck() {
        val values = listOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        val suits = listOf("clubs", "diamonds", "hearts", "spades")
        for (suit in suits) {
            for (value in values) {
                deck.add(Card(value, suit))
            }
        }
    }

    fun shuffleDeck() {
        deck.shuffle(Random(System.currentTimeMillis()))
    }

    fun drawCard(): Card {
        return deck.removeAt(deck.size - 1)
    }
}