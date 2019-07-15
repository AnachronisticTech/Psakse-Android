package com.anachronistic.daniel.psakse

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast

class GameViewController: Activity() {

    val gridSize = 5
    val wildcards = 2
//    var gridExists = false
    var grid:Grid? = null
    var deck:Deck? = null
    var activeCard:Card? = null
    var lastSelected = -1
    var gameComplete = false
    var puzzleID: String? = null
    var override: String? = null
    var puzzleSig: String = ""
    var dSize = Point()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        this.dSize = size
//        var linearLayout = LinearLayout(this)
//        linearLayout.orientation = LinearLayout.VERTICAL
////
//        var layoutParams = LinearLayout.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
//        this.addContentView(linearLayout, layoutParams)
        resetGame(this, dSize)

//
//        var button = Button(this)
//        button.text = "My button"
//        button.layoutParams = params
//
//        linearLayout.addView(button)

    }

    fun resetGame(view: Context, display: Point) {
        gameComplete = false
        deck = Deck()
        if (grid != null) {
            for (i in 0 until grid?.buttonGrid!!.size) {
                grid?.buttonGrid!![i].reset()
                grid?.buttonGrid!![i].setAttrs("clear", Color.WHITE)
                grid?.buttonGrid!![i].setBorder(0, Color.BLACK)
                grid?.buttonGrid!![i].isEnabled = true
            }
        } else {
            grid = Grid(gridSize, display)
//            grid?.create(view)
            val params = LinearLayout.LayoutParams(display.x - 60, display.y)
            val gridView = grid?.create(view)
            gridView?.layoutParams = params
            this.addContentView(gridView, params)
            for (button in grid?.buttonGrid!!) {
                button.reset()
                // add target
                button.setOnClickListener(select(button))
                button.setOnClickListener() {
                    Toast.makeText(this, ""+button.tag, Toast.LENGTH_SHORT).show()
                }
                button.setAttrs("clear", Color.WHITE)
                button.isEnabled = true
            }

            // Create in-game controls here
        }

        // Create deck from override or procedurally
        if (override != null) {
            // override deck creation here
        } else {
            deck?.populateDeck()
            deck?.finalShuffle()
            deck?.removeCards(gridSize, wildcards)
            // three random starting cards section
            val randArray = IntArray(3)
            for (i in 0 until 3) {
                var randPosition = (Math.random() * (gridSize * gridSize)).toInt()
                while (randArray.contains(randPosition) ||
                        randPosition == gridSize * gridSize ||
                        randArray.contains(randPosition - 1) ||
                        randArray.contains(randPosition + 1) ||
                        randArray.contains(randPosition - gridSize) ||
                        randArray.contains(randPosition + gridSize)) {
                    randPosition = (Math.random() * (gridSize * gridSize - 1)).toInt()
                    if (randPosition == 0) {
                        randPosition = 1
                    }
                }
                randArray[i] = randPosition
            }
            for (i in randArray) {
                val image = deck!!.arr[0].getFilename()
                val bgcolor = deck!!.arr[0].getColor()
                grid!!.buttonGrid[i].setAttrs(image, bgcolor)
                grid!!.buttonGrid[i].setBorder(9, Color.YELLOW)
                grid!!.buttonGrid[i].isEnabled = false
                grid!!.grid[i] = deck!!.arr[0]
                val card = deck!!.arr.removeAt(0)
                // update quantities for puzzlesig
                //
            }
            deck?.addWildCards(wildcards)
        }

        // Shuffle deck and draw tile
        deck?.finalShuffle()
        val image = deck!!.arr[0].getFilename()
        val bgcolor = deck!!.arr[0].getColor()
        grid!!.grid[gridSize * gridSize] = deck!!.arr[0]
        grid!!.buttonGrid[gridSize * gridSize].setAttrs(image, bgcolor)
    }

    fun select(sender: Button): View.OnClickListener {
        return View.OnClickListener {

        }
    }

    fun deselect() {
        activeCard = null
        grid!!.buttonGrid[lastSelected].setBorder(0, Color.BLACK)
        lastSelected = -1
    }

    fun clearTile(position: Int) {
        if (position == (gridSize * gridSize)) {
            deck!!.arr.removeAt(0)
            if (deck!!.arr.size >= 1) {
                val image = deck!!.arr[0].getFilename()
                val color = deck!!.arr[0].getColor()
                grid!!.grid[gridSize * gridSize] = deck!!.arr[0]
                grid!!.buttonGrid[gridSize * gridSize].setAttrs(image, color)
            } else {
                grid!!.grid[gridSize * gridSize] = null
                grid!!.buttonGrid[gridSize * gridSize].setAttrs("none", Color.WHITE)
                grid!!.buttonGrid[gridSize * gridSize].isEnabled = false
            }
        } else {
            grid!!.grid[position] = null
            grid!!.buttonGrid[position].setAttrs("empty", Color.WHITE)
        }
    }

    fun left(x: Int): Int {
        return x + 1
    }
    fun right(x: Int): Int {
        return x - 1
    }
    fun up(x: Int): Int {
        return x + gridSize
    }
    fun down(x: Int): Int {
        return x - gridSize
    }

    fun checker(position: Int, card: Card): Boolean {
        val validArray = arrayListOf<Boolean>()
        if (position < gridSize) {
            if (position == 0) {
                // Bottom right corner; check tiles left and above
                validArray.add(checkTile(left(position), card))
                validArray.add(checkTile(up(position), card))
            } else if (position == gridSize - 1) {
                // Bottom left corner; check tiles right and above
                validArray.add(checkTile(right(position), card))
                validArray.add(checkTile(up(position), card))
            } else {
                // Bottom edge; check tiles left, right and above
                validArray.add(checkTile(left(position), card))
                validArray.add(checkTile(right(position), card))
                validArray.add(checkTile(up(position), card))
            }
        } else if (position % gridSize == 0) {
            if (position == gridSize * (gridSize - 1)) {
                // Top right corner; check tiles left and below
                validArray.add(checkTile(left(position), card))
                validArray.add(checkTile(down(position), card))
            } else {
                // Right edge; check tiles left, above and below
                validArray.add(checkTile(left(position), card))
                validArray.add(checkTile(up(position), card))
                validArray.add(checkTile(down(position), card))
            }
        } else if (position % gridSize == gridSize - 1) {
            if (position == (gridSize * gridSize) - 1) {
                // Top left corner; check tiles right and below
                validArray.add(checkTile(right(position), card))
                validArray.add(checkTile(down(position), card))
            } else {
                // Left edge; check tiles right, above and below
                validArray.add(checkTile(right(position), card))
                validArray.add(checkTile(up(position), card))
                validArray.add(checkTile(down(position), card))
            }
        } else if (position > gridSize * (gridSize - 1)) {
            // Top edge; check tiles left, right and below
            validArray.add(checkTile(left(position), card))
            validArray.add(checkTile(right(position), card))
            validArray.add(checkTile(down(position), card))
        } else {
            // Central tile; check tiles left, right, above and below
            validArray.add(checkTile(left(position), card))
            validArray.add(checkTile(right(position), card))
            validArray.add(checkTile(up(position), card))
            validArray.add(checkTile(down(position), card))
        }
        return !validArray.contains(false)
    }

    fun checkTile(position: Int, card: Card): Boolean {
        if (position > (gridSize * gridSize)) {
            return true
        }
        if (grid!!.grid[position] != null) {
            return card.matches(grid!!.grid[position]!!)
        } else {
            return true
        }
    }
}