package com.anachronistic.daniel.psakse

abstract class Card {
    abstract fun matches(other: Card): Boolean
    abstract fun getFilename(): String
    abstract fun getColor(): Int
}

class Normal : Card {
    var symbol: Symbols
    var color: Colors

    constructor(symbol: Symbols, color: Colors) {
        this.symbol = symbol
        this.color = color
    }

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
            Colors.Green -> 0xAFE346
            Colors.Yellow -> 0xFFDC73
            Colors.Purple -> 0xECA7EE
            Colors.Orange -> 0xFF9933
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

class Wild : Card {
    constructor() {

    }

    override fun getFilename(): String {
        return "dot"
    }

    override fun getColor(): Int {
        return 0xFFB4BC
    }

    override fun matches(other: Card): Boolean {
        return true
    }
}
