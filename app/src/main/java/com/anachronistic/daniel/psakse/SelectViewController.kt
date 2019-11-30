package com.anachronistic.daniel.psakse

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ListView

class SelectViewController: Activity() {

    var puzzleSelected = -1
    var tableData: Any? = null
    var override: Boolean = false

    private fun goToHome(): View.OnClickListener {
        return View.OnClickListener {
            startActivity(Intent(this, HomeViewController::class.java))
        }
    }



    fun getJson() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        val frame = FrameLayout(this)
        val fParams = FrameLayout.LayoutParams(size.x, size.y)
        val x = (size.x / 2) - 300
        val y = size.y - 600
        val button = Button(this)
        val bParams = FrameLayout.LayoutParams(600, 180)
        bParams.leftMargin = x
        bParams.topMargin = y
        button.layoutParams = bParams
        button.text = "Home"
        val layer = GradientDrawable()
        layer.cornerRadius = 90.0f
        layer.setStroke(9, Color.DKGRAY)
        layer.setColor(ContextCompat.getColor(this, R.color.gameGreen))
        button.setOnClickListener(goToHome())
        button.background = layer
        frame.addView(button)

        // add table here
//        val table = ListView(this)
//        table.adapter = ArrayAdapter(this, tableData)
        //
        this.addContentView(frame, fParams)
    }
}