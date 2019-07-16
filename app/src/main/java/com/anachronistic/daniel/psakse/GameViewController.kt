package com.anachronistic.daniel.psakse

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast

class GameViewController: Activity() {

    val gridSize = 5
    val wildcards = 2
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
        resetGame(this, dSize)

//        var linearLayout = LinearLayout(this)
//        linearLayout.orientation = LinearLayout.VERTICAL
////
//        var layoutParams = LinearLayout.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
//        this.addContentView(linearLayout, layoutParams)
        
//        var button = ImageButton(this)
//        button.text = "My button"
//        button.layoutParams = params
//
//        linearLayout.addView(button)

    }

    private fun resetGame(view: Context, display: Point) {
        gameComplete = false
        deck = Deck()
        if (grid != null) {
            for (i in 0 until grid?.buttonGrid!!.size) {
                grid?.buttonGrid!![i].reset()
                grid?.buttonGrid!![i].isEnabled = true
            }
        } else {
            grid = Grid(gridSize, display)
            val params = LinearLayout.LayoutParams(display.x - 60, display.y)
            val gridView = grid?.create(view)
            gridView?.layoutParams = params
            val resetBtn = grid!!.drawControls(this)
            resetBtn.setOnClickListener(resetHandler())
            gridView?.addView(resetBtn)
            this.addContentView(gridView, params)
            for (button in grid?.buttonGrid!!) {
                button.reset()
                button.setOnClickListener(select(button))
                button.isEnabled = true
            }
        }

        // Create deck from override or procedurally
        if (override != null) {
            var locked = override!!.dropLast(17)
            for (i in 0 until 3) {
                val pos = Integer.parseInt(locked.take(2))
                val col = locked.take(4).take(1)
                val sym = locked.take(4).takeLast(1)
                locked = locked.drop(4)
                val card = deck!!.stringToCard(col, sym)
                val image = card.getFilename()
                val bgcolor = card.getColor()
                grid!!.buttonGrid[pos].setAttrs(image, bgcolor, 9, R.color.gameFBorder)
                grid!!.grid[pos] = card
            }
            deck!!.arr = deck!!.createDeckFromString(override!!.drop(12))
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
                grid!!.buttonGrid[i].setAttrs(image, bgcolor, 9, R.color.gameFBorder)
                grid!!.buttonGrid[i].isEnabled = false
                grid!!.grid[i] = deck!!.arr[0]
                val card = deck!!.arr.removeAt(0)
//                deck!!.updateQuantities(card)
                puzzleSig += (if (i < 10) {"0"} else {""}) + i + card.getID()
            }
            puzzleSig += deck!!.cardQuantities.joinToString("")
            deck?.addWildCards(wildcards)
        }

        // Shuffle deck and draw tile
        deck?.finalShuffle()
        val image = deck!!.arr[0].getFilename()
        val bgcolor = deck!!.arr[0].getColor()
        grid!!.grid[gridSize * gridSize] = deck!!.arr[0]
        grid!!.buttonGrid[gridSize * gridSize].setAttrs(image, bgcolor, 0, R.color.gameSBorder)
    }

    private fun select(sender: ImageButton): View.OnClickListener {
        return View.OnClickListener {
            if (activeCard != null) {
                if (it.tag == (gridSize * gridSize) || it.tag == lastSelected) {
                    deselect()
                } else {
                    val location = it.tag as Int
                    if (grid!!.grid[location] == null) {
                        // try move
                        if (checker(location, activeCard!!) || location > (gridSize * gridSize)) {
                            grid!!.grid[location] = activeCard
                            grid!!.buttonGrid[location].setAttrs(activeCard!!.getFilename(), activeCard!!.getColor(),
                                0, R.color.gameSBorder)
                            clearTile(lastSelected)
                        } else {
                            Toast.makeText(this, "That tile can't be placed there", Toast.LENGTH_SHORT).show()
                        }
                        deselect()
                        val finishedArray = arrayListOf<Boolean>()
                        if (deck!!.arr.size == 0) {
                            for (i in 0 until (gridSize * (gridSize + 1))) {
                                if (i < gridSize * gridSize) {
                                    finishedArray.add(grid!!.grid[i] != null)
                                } else {
                                    finishedArray.add(grid!!.grid[i] == null)
                                }
                            }
                            if (!finishedArray.contains(false)) {
                                gameComplete = true
                                // save completion or send to server
                                for (i in grid!!.buttonGrid) {
                                    i.isEnabled = false
                                }
                                Toast.makeText(this, "You solved the puzzle!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // try swap
                        if (lastSelected != gridSize * gridSize) {
                            if ((checker(lastSelected, grid!!.grid[location]!!) || lastSelected > (gridSize *
                                        gridSize)) && (checker(location, activeCard!!) ||  location > (gridSize *
                                        gridSize))) {
                                grid!!.grid[lastSelected] = grid!!.grid[location]
                                var image = grid!!.grid[lastSelected]!!.getFilename()
                                var color = grid!!.grid[lastSelected]!!.getColor()
                                grid!!.buttonGrid[lastSelected].setAttrs(image, color, 0, R.color.gameSBorder)
                                grid!!.grid[location] = activeCard
                                image = grid!!.grid[location]!!.getFilename()
                                color = grid!!.grid[location]!!.getColor()
                                grid!!.buttonGrid[location].setAttrs(image, color, 0, R.color.gameSBorder)
                            } else {
                                Toast.makeText(this, "Those tiles can't be swapped", Toast.LENGTH_SHORT).show()
                            }
                        }
                        deselect()
                    }
                }
            } else {
                // set card to active
                // set border
                // last selected = tag
                val location = sender.tag as Int
                if (grid!!.grid[location] != null) {
                    activeCard = grid!!.grid[location]
                    val image = activeCard!!.getFilename()
                    val color = activeCard!!.getColor()
                    grid!!.buttonGrid[location].setAttrs(image, color, 9, R.color.gameSBorder)
                    lastSelected = location
                }
            }
        }
    }

    private fun deselect() {
        activeCard = null
        if (grid!!.grid[lastSelected] != null) {
            val image = grid!!.grid[lastSelected]!!.getFilename()
            val color = grid!!.grid[lastSelected]!!.getColor()
            grid!!.buttonGrid[lastSelected].setAttrs(image, color, 0, R.color.gameSBorder)
        }
        lastSelected = -1
    }

    private fun clearTile(position: Int) {
        if (position == (gridSize * gridSize)) {
            deck!!.arr.removeAt(0)
            if (deck!!.arr.size >= 1) {
                val image = deck!!.arr[0].getFilename()
                val color = deck!!.arr[0].getColor()
                grid!!.grid[gridSize * gridSize] = deck!!.arr[0]
                grid!!.buttonGrid[gridSize * gridSize].setAttrs(image, color, 0, R.color.gameSBorder)
            } else {
                grid!!.grid[gridSize * gridSize] = null
                grid!!.buttonGrid[gridSize * gridSize].setAttrs("empty", R.color.gameWhite, 0, R.color.gameSBorder)
                grid!!.buttonGrid[gridSize * gridSize].isEnabled = false
            }
        } else {
            grid!!.grid[position] = null
            grid!!.buttonGrid[position].setAttrs("clear", R.color.gameWhite, 0, R.color.gameSBorder)
        }
    }

    private fun left(x: Int): Int {
        return x + 1
    }
    private fun right(x: Int): Int {
        return x - 1
    }
    private fun up(x: Int): Int {
        return x + gridSize
    }
    private fun down(x: Int): Int {
        return x - gridSize
    }

    private fun checker(position: Int, card: Card): Boolean {
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

    private fun checkTile(position: Int, card: Card): Boolean {
        if (position > (gridSize * gridSize)) {
            return true
        }
        if (grid!!.grid[position] != null) {
            return card.matches(grid!!.grid[position]!!)
        } else {
            return true
        }
    }

    fun resetHandler(): View.OnClickListener {
        return View.OnClickListener {
            resetGame(this, dSize)
        }
    }
}