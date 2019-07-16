package com.anachronistic.daniel.psakse

enum class Colors {
    Green,
    Yellow,
    Purple,
    Orange;

    fun getColor(): Int {
        return when (this) {
            Green  -> R.color.gameGreen
            Yellow -> R.color.gameYellow
            Purple -> R.color.gamePurple
            Orange -> R.color.gameOrange
        }
    }

    fun getID(): String {
        return when (this) {
            Green  -> "g"
            Yellow -> "y"
            Purple -> "p"
            Orange -> "o"
        }
    }
}
