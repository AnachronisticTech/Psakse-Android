package com.anachronistic.daniel.psakse

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.Button

fun Button.reset() {
    this.text = ""
    this.setTextColor(Color.WHITE)
    this.setBackgroundColor(Color.WHITE)
    this.setBorder(0, Color.WHITE)
}

fun Button.setAttrs(image: String, bgColor: Int) {
    when (image) {
        "psi"   -> this.text = "psi"//this.setBackgroundResource(R.drawable.psi)
        "a"     -> this.setBackgroundResource(R.drawable.a)
        "xi"    -> this.setBackgroundResource(R.drawable.xi)
        "e"     -> this.setBackgroundResource(R.drawable.e)
        "clear" -> this.setBackgroundResource(R.drawable.clear)
        "empty" -> this.setBackgroundResource(R.drawable.none)
    }
    this.setBackgroundColor(bgColor)
}

fun Button.setBorder(width: Int, color: Int) {
    val layer = GradientDrawable()
    layer.cornerRadius = 0.0f
    layer.setStroke(width, color)
    layer.setColor(Color.GREEN)
    this.background = layer
}
