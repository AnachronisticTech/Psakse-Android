package com.anachronistic.daniel.psakse

abstract class Card {
    abstract fun matches(other: Card): Boolean
    abstract fun getFilename(): String
    abstract fun getColor(): Int
    abstract fun getID(): String
}

class Normal(private var symbol: Symbols, private var color: Colors): Card() {

    override fun getFilename(): String {
        return symbol.getFilename()
    }

    override fun getColor(): Int {
        return color.getColor()
    }

    override fun matches(other: Card): Boolean {
        if (other is Wild) {
            return true
        } else {
            val otherCard = other as Normal
            return (this.symbol == otherCard.symbol) || (this.color == otherCard.color)
        }
    }

    override fun getID(): String {
        return color.getID() + symbol.getID()
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

    override fun getID(): String {
        return ""
    }
}
