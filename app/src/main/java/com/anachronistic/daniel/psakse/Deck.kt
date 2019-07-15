package com.anachronistic.daniel.psakse

class Deck {

    val allCards = arrayListOf<Card>(
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
    var cardQuantities = IntArray(17) { _ -> 2 }
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

    fun updateQuantities(card: Card) {}

    fun createDeckFromString(string: String): ArrayList<Card> {
        var deck = arrayListOf<Card>()
        return deck
    }

    fun stringToCard(col: String, sym: String): Card {
        var card: Card
        card = Wild()
        return card
    }

    fun finalShuffle() {
        this.arr.shuffle()
    }
}
