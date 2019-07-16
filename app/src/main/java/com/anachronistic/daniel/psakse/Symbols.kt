package com.anachronistic.daniel.psakse

enum class Symbols {
    Psi,
    A,
    Xi,
    E;

    fun getFilename(): String {
        return when (this) {
            Psi -> "psi"
            A   -> "a"
            Xi  -> "xi"
            E   -> "e"
        }
    }

    fun getID(): String {
        return when (this) {
            Psi  -> "p"
            A    -> "a"
            Xi   -> "x"
            E    -> "e"
        }
    }
}
