package com.anachronistic.daniel.psakse

abstract class Card {
    abstract fun matches(other: Card): Boolean
    abstract fun getFilename(): String
    abstract fun getColor(): Int
}

class Normal(private var symbol: Symbols, private var color: Colors): Card() {

    override fun getFilename(): String {
        return when (symbol) {
            Symbols.Psi -> "psi"
            Symbols.A -> "a"
            Symbols.Xi -> "xi"
            Symbols.E -> "e"
        }
    }

    override fun getColor(): Int {
        return when (color) {
            Colors.Green -> R.color.gameGreen
            Colors.Yellow -> R.color.gameYellow
            Colors.Purple -> R.color.gamePurple
            Colors.Orange -> R.color.gameOrange
        }
    }

    override fun matches(other: Card): Boolean {
        if (other is Wild) {
            return true
        } else {
            val otherCard = other as Normal
            return (this.symbol == otherCard.symbol) || (this.color == otherCard.color)
        }
    }
}

class Wild: Card() {
    override fun getFilename(): String {
        return "dot"
    }

    override fun getColor(): Int {
        return R.color.gameWild
    }

    override fun matches(other: Card): Boolean {
        return true
    }
}
