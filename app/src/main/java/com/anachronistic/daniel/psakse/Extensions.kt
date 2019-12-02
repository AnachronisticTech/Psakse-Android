package com.anachronistic.daniel.psakse

import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import android.widget.ImageButton

fun ImageButton.reset() {
    this.setAttrs("clear", R.color.gameWhite, 0, R.color.gameSBorder)
    this.isEnabled = true
}

fun ImageButton.setAttrs(image: String, bgColor: Int, sWidth: Int, sColor: Int) {
    val layer = GradientDrawable()
    layer.setColor(ContextCompat.getColor(this.context, bgColor))
    layer.setStroke(sWidth, ContextCompat.getColor(this.context, sColor))
    this.background = layer
    when (image) {
        "psi"   -> this.setImageResource(R.drawable.psi)
        "a"     -> this.setImageResource(R.drawable.a)
        "xi"    -> this.setImageResource(R.drawable.xi)
        "e"     -> this.setImageResource(R.drawable.e)
        "dot"   -> this.setImageResource(R.drawable.dot)
        "clear" -> this.setImageResource(R.drawable.clear)
        "empty" -> this.setImageResource(R.drawable.none)
    }
}
