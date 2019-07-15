package com.anachronistic.daniel.psakse

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.widget.ImageButton

fun ImageButton.reset() {
    this.setBackgroundColor(Color.WHITE)
    this.setBorder(0, Color.WHITE)
}

fun ImageButton.setAttrs(image: String, bgColor: Int) {
    if (bgColor == Color.WHITE) {
        this.setBackgroundColor(bgColor)
    } else {
        this.setBackgroundColor(ContextCompat.getColor(this.context, bgColor))
    }
    when (image) {
        "psi"   -> this.setImageResource(R.drawable.psi)
        "a"     -> this.setImageResource(R.drawable.a)
        "xi"    -> this.setImageResource(R.drawable.xi)
        "e"     -> this.setImageResource(R.drawable.e)
        "clear" -> this.setImageResource(R.drawable.clear)
        "empty" -> this.setImageResource(R.drawable.none)
    }
}

fun ImageButton.setBorder(width: Int, color: Int) {
//    val layer = GradientDrawable()
//    layer.cornerRadius = 0.0f
//    layer.setStroke(width, color)
//    layer.setColor(Color.GREEN)
//    this.background = layer
}
