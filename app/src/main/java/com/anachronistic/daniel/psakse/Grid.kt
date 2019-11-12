package com.anachronistic.daniel.psakse

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.widget.*

class Grid(gridSize: Int, display: Point) {
    private var gridSize = 5
    var grid = arrayOfNulls<Card>(30)
    var buttonGrid = arrayListOf<ImageButton>()
    private var display = Point()

    init {
        this.gridSize = gridSize
        this.grid = arrayOfNulls(gridSize * (gridSize + 1))
        this.display = display
    }

    fun create(view: Context): FrameLayout {
        val gridMargin = 60
        val tileMargin = 15
        val gameBoard = FrameLayout(view)
        val params = FrameLayout.LayoutParams(display.x, display.y)
        gameBoard.layoutParams = params
        val mainGrid = drawMainGrid(gridMargin, tileMargin, view)
        gameBoard.addView(mainGrid)
        val sideGrid = drawSideGrid(gridMargin, tileMargin, view)
        gameBoard.addView(sideGrid)
        return gameBoard
    }

    private fun drawMainGrid(gridMargin: Int, tileMargin: Int, view: Context): FrameLayout {
        val gridHeight = display.x - (2 * gridMargin)
        val gridView = FrameLayout(view)
        val params = FrameLayout.LayoutParams(gridHeight, gridHeight)
        gridView.layoutParams = params
        gridView.x = gridMargin.toFloat()
        gridView.y = gridMargin.toFloat()
        gridView.setBackgroundColor(Color.BLACK)
        val tileHeight = (gridHeight - ((gridSize - 1) * tileMargin)) / gridSize
        for (i in 0 until (gridSize * gridSize)) {
            val gridX = i % gridSize
            val gridY = (i / gridSize)
            val x = gridX * (tileHeight + tileMargin)
            val y = gridY * (tileHeight + tileMargin)
            val button = createButton(x, y, tileHeight, i, view)
            buttonGrid.add(button)
            gridView.addView(button)
        }
        return gridView
    }

    private fun drawSideGrid(gridMargin: Int, tileMargin: Int, view: Context): FrameLayout {
        val gridWidth = display.x - (2 * gridMargin)
        val tileHeight = (gridWidth - ((gridSize - 1) * tileMargin)) / gridSize
        val gridView = FrameLayout(view)
        val params = FrameLayout.LayoutParams(gridWidth, tileHeight + (2 * tileMargin))
        gridView.layoutParams = params
        gridView.x = gridMargin.toFloat()
        gridView.y = ((gridMargin * 2) + gridWidth).toFloat()
        gridView.setBackgroundColor(Color.BLACK)
        for (i in 0 until 5) {
            val gridX = i % gridSize
            val x = gridX * (tileHeight + tileMargin)
            val button = createButton(x, tileMargin, tileHeight, i + (gridSize * gridSize), view)
            buttonGrid.add(button)
            gridView.addView(button)
        }
        return gridView
    }

    private fun createButton(x: Int, y: Int, height: Int, tag: Int, view: Context): ImageButton {
        val button = ImageButton(view)
        val layer = GradientDrawable()
        layer.cornerRadius = 0.0f
        layer.setStroke(0, 0x00000000)
        button.background = layer
        button.scaleType = ImageView.ScaleType.CENTER_CROP
        button.setBackgroundColor(Color.WHITE)
        button.tag = tag
        val params = FrameLayout.LayoutParams(height, height)
        params.leftMargin = x
        params.topMargin = y
        button.layoutParams = params
        return button
    }

    fun drawControls(view: Context): Button {
        val button = Button(view)
        button.text = "New game"
        val layer = GradientDrawable()
        layer.cornerRadius = 90.0f
        layer.setStroke(9, Color.DKGRAY)
        layer.setColor(ContextCompat.getColor(view, R.color.gamePurple))
        button.background = layer
        val params = LinearLayout.LayoutParams(display.x - 120, 180)
        params.leftMargin = 60
        params.topMargin = (display.x * 1.25).toInt()
        button.layoutParams = params
        return button
    }
}
