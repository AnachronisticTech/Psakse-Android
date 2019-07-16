package com.anachronistic.daniel.psakse

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.*

class HomeViewController: Activity() {

    private fun goToGame(): View.OnClickListener {
        return View.OnClickListener {
            startActivity(Intent(this, GameViewController::class.java))
        }
    }

    private fun goToPuzzleSelect(): View.OnClickListener {
        return View.OnClickListener {
            startActivity(Intent(this, SelectViewController::class.java))
        }
    }

    private fun createMenu(size: Point): FrameLayout {
        val frame = FrameLayout(this)
        val fParams = FrameLayout.LayoutParams(size.x, size.y)
        frame.layoutParams = fParams
        val options = arrayOf("Tutorial", "Challenge Mode", "Random Puzzle")
        for (i in 0 until options.size) {
            val x = (size.x / 2) - 300
            val y = ((i + 3) * size.y / (options.size + 4)) - 90
            val button = Button(this)
            val bParams = FrameLayout.LayoutParams(600, 180)
            bParams.leftMargin = x
            bParams.topMargin = y
            button.layoutParams = bParams
            button.text = options[i]
            val layer = GradientDrawable()
            layer.setStroke(9, Color.DKGRAY)
            when (i) {
                0 -> {
                    layer.setColor(ContextCompat.getColor(this, R.color.gameGreen))
                    button.setOnClickListener(comingSoon())
                }
                1 -> {
                    layer.setColor(ContextCompat.getColor(this, R.color.gameYellow))
                    button.setOnClickListener(comingSoon())
                }
                2 -> {
                    layer.setColor(ContextCompat.getColor(this, R.color.gamePurple))
                    button.setOnClickListener(goToGame())
                }
            }
            button.background = layer
            frame.addView(button)
        }
        val logo = ImageView(this)
        val lParams = FrameLayout.LayoutParams(size.x - 180, 450)
        lParams.leftMargin = 90
        lParams.topMargin = (size.y / (options.size + 4)) - 90
        logo.layoutParams = lParams
        logo.setImageResource(R.drawable.logo_large_alt)
        logo.scaleType = ImageView.ScaleType.FIT_CENTER
        frame.addView(logo)
        return frame
    }

    private fun comingSoon(): View.OnClickListener {
        return View.OnClickListener {
            Toast.makeText(this, "This feature is coming soon!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        val menu = createMenu(size)
        val params = LinearLayout.LayoutParams(size.x, size.y)
        this.addContentView(menu, params)
    }
}