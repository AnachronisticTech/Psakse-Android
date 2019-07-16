package com.anachronistic.daniel.psakse

class Deck {

    private val allCards = arrayListOf(
        Normal(Symbols.Psi, Colors.Green),
        Normal(Symbols.A, Colors.Green),
        Normal(Symbols.Xi, Colors.Green),
        Normal(Symbols.E, Colors.Green),
        Normal(Symbols.Psi, Colors.Yellow),
        Normal(Symbols.A, Colors.Yellow),
        Normal(Symbols.Xi, Colors.Yellow),
        Normal(Symbols.E, Colors.Yellow),
        Normal(Symbols.Psi, Colors.Purple),
        Normal(Symbols.A, Colors.Purple),
        Normal(Symbols.Xi, Colors.Purple),
        Normal(Symbols.E, Colors.Purple),
        Normal(Symbols.Psi, Colors.Orange),
        Normal(Symbols.A, Colors.Orange),
        Normal(Symbols.Xi, Colors.Orange),
        Normal(Symbols.E, Colors.Orange),
        Wild()
    )
    var cardQuantities = IntArray(17) { 2 }
    var arr = arrayListOf<Card>()

    fun populateDeck() {
        for (symbol in Symbols.values()) {
            for (color in Colors.values()) {
                this.arr.add(Normal(symbol, color))
                this.arr.add(Normal(symbol, color))
            }
        }
    }

    fun addWildCards(count: Int)  {
        for (i in 0 until count) {
            this.arr.add(Wild())
        }
    }

    fun removeCards(gridSize: Int, wildcards: Int) {
        val gridTotal = this.arr.count() - (gridSize * gridSize) + wildcards - 1
        for (i in 0..gridTotal) {
            this.arr.removeAt(this.arr.lastIndex)
        }
    }

    fun updateQuantities(card: Card) {
        val index = allCards.indexOf(card)
        cardQuantities[index] -= 1
    }

    fun createDeckFromString(string: String): ArrayList<Card> {
        var str = string
        val cards = allCards
        val deck = arrayListOf<Card>()
        for (i in 0 until cards.size) {
            val quantity = Integer.parseInt(str.take(1))
            str = str.drop(1)
            val arr = Array(quantity) { cards[i]}
            deck.addAll(arr)
        }
        return deck
    }

    fun stringToCard(col: String, sym: String): Card {
        var card: Card = Wild()
        when (col) {
            "g" -> {
                when (sym) {
                    "p" -> card = Normal(Symbols.Psi, Colors.Green)
                    "a" -> card = Normal(Symbols.A, Colors.Green)
                    "x" -> card = Normal(Symbols.Xi, Colors.Green)
                    "e" -> card = Normal(Symbols.E, Colors.Green)
                }
            }
            "y" -> {
                when (sym) {
                    "p" -> card = Normal(Symbols.Psi, Colors.Yellow)
                    "a" -> card = Normal(Symbols.A, Colors.Yellow)
                    "x" -> card = Normal(Symbols.Xi, Colors.Yellow)
                    "e" -> card = Normal(Symbols.E, Colors.Yellow)
                }
            }
            "p" -> {
                when (sym) {
                    "p" -> card = Normal(Symbols.Psi, Colors.Purple)
                    "a" -> card = Normal(Symbols.A, Colors.Purple)
                    "x" -> card = Normal(Symbols.Xi, Colors.Purple)
                    "e" -> card = Normal(Symbols.E, Colors.Purple)
                }
            }
            "o" -> {
                when (sym) {
                    "p" -> card = Normal(Symbols.Psi, Colors.Orange)
                    "a" -> card = Normal(Symbols.A, Colors.Orange)
                    "x" -> card = Normal(Symbols.Xi, Colors.Orange)
                    "e" -> card = Normal(Symbols.E, Colors.Orange)
                }
            }
        }
        return card
    }

    fun finalShuffle() {
        this.arr.shuffle()
    }
}
