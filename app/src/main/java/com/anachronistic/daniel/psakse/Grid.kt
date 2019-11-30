package com.anachronistic.daniel.psakse

import android.content.Context
import android.widget.*

class Grid(private val gridSize: Int, mainGrid: FrameLayout, subGrid: FrameLayout, private val context: Context) {
    private val tileMargin = 10
    var grid = arrayOfNulls<Card>(30)
    var buttonGrid = arrayListOf<ImageButton>()

    init {
        this.grid = arrayOfNulls(gridSize * (gridSize + 1))
        drawMainGrid(mainGrid)
        drawSubGrid(subGrid)
    }

    private fun drawMainGrid(gridUI: FrameLayout) {
        val gridHeight = gridUI.width
        val tileHeight: Int = (gridHeight - ((gridSize - 1) * tileMargin)) / gridSize
        for (i in 0 until (gridSize * gridSize)) {
            val gridX: Int = i % gridSize
            val gridY: Int = (i / gridSize)
            val x = gridX * (tileHeight + tileMargin)
            val y = gridY * (tileHeight + tileMargin)
            val button = createButton(x, y, tileHeight, i, context)
            buttonGrid.add(button)
            gridUI.addView(button)
        }
    }

    private fun drawSubGrid(gridUI: FrameLayout) {
        val gridWidth = gridUI.width
        val tileHeight = (gridWidth - ((gridSize - 1) * tileMargin)) / gridSize
        setHeight(gridUI, tileHeight)
        for (i in 0 until 5) {
            val gridX = i % gridSize
            val x = gridX * (tileHeight + tileMargin)
            val button = createButton(x, 5, tileHeight, i, context)
            buttonGrid.add(button)
            gridUI.addView(button)
        }
    }

    private fun setHeight(gridUI: FrameLayout, height: Int) {
        val params = gridUI.layoutParams
        params.height = height
        gridUI.layoutParams = params
    }

    private fun createButton(x: Int, y: Int, height: Int, tag: Int, context: Context): ImageButton {
        val button = ImageButton(context)
        button.tag = tag
        val params = FrameLayout.LayoutParams(height, height)
        params.leftMargin = x
        params.topMargin = y
        button.layoutParams = params
        button.reset()
        return button
    }

    fun reset() {
        for (i in 0 until buttonGrid.size) {
            grid[i] = null
            buttonGrid[i].reset()
        }
    }
}
