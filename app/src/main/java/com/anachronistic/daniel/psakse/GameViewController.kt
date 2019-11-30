package com.anachronistic.daniel.psakse

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.doOnLayout

class GameViewController: AppCompatActivity() {

    private val gridSize = 5
    private val wildcards = 2
    private var grid: Grid? = null
    private var deck: Deck? = null
    private var activeCard: Card? = null
    private var lastSelected = -1
    private var gameComplete = false
    private var puzzleID: String? = null
    private var override: String? = null
    private var puzzleSig: String = ""

    // TODO: Add button views
    private var mainGrid: FrameLayout? = null
    private var subGrid: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_view_controller)

        window.decorView.findViewById<View>(R.id.root).doOnLayout {
            mainGrid = findViewById(R.id.mainGrid)
            subGrid = findViewById(R.id.subGrid)
            resetGame(it.context)
        }
    }

    // TODO: Implement
    private fun setupButtonView(button: ImageButton, title: String, color: Colors) {}

    private fun resetGame(context: Context) {
        gameComplete = false
        deck = Deck()
        if (grid != null) {
            // Reset all buttons in main and side grids
            grid!!.reset()
        } else {
            // Create main and side grids with all buttons
            grid = Grid(gridSize, mainGrid!!, subGrid!!, context)
            for (button in grid!!.buttonGrid) {
                button.setOnClickListener(select(button))
            }

            // Create in game controls
            // TODO: Pass subviews
            if (puzzleID != null) {
//                setupButtonView(, "Reset", Colors.Purple)
            } else {
//                setupButtonView(, "New Game", Colors.Purple)
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
                val color = card.getColor()
                grid!!.buttonGrid[pos].setAttrs(image, color, 9, R.color.gameFBorder)
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
                val color = deck!!.arr[0].getColor()
                grid!!.buttonGrid[i].setAttrs(image, color, 9, R.color.gameFBorder)
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
        setCard((gridSize * gridSize), deck!!.arr[0])
    }

    // TODO: Implement
    private fun sendToServer(puzzle: String) {}

    private fun select(sender: ImageButton): View.OnClickListener {
        return View.OnClickListener {
            if (activeCard != null) {
                // if sender == gridSize^2 or sender == lastSelected
                // deselect
                // else if sender empty
                // try move()
                // else
                // try swap()
                if (it.tag == (gridSize * gridSize) || it.tag == lastSelected) {
                    // If location is card location or deck location deselect
                    deselect()
                } else {
                    val location = it.tag as Int
                    if (grid!!.grid[location] == null) {
                        // If location empty try move
                        if (checker(location, activeCard!!) || location > (gridSize * gridSize)) {
                            // If no tile conflicts place card
                            setCard(location, activeCard)
                            // clear previous location
                            clearTile(lastSelected)
                        } else {
                            // Warn of tile move conflict
                            Toast.makeText(this, "That tile can't be placed there", Toast.LENGTH_SHORT).show()
                        }
                        deselect()
                        if (deck!!.arr.size == 0) {
                            // If deck empty check grid full
                            // TODO: This don't work!
                            val arr: List<Boolean> = grid!!.grid.map { a -> a != null }
                            arr.dropLast(5)
                            if (arr.contains(false)) {
                                // If grid full, game is complete
                                gameComplete = true
                                // TODO: save completion or send to server
                                for (i in grid!!.buttonGrid) {
                                    i.isEnabled = false
                                }
                                Toast.makeText(this, "You solved the puzzle!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // If location not empty try swap
                        if (lastSelected != gridSize * gridSize) {
                            // If last selected card was not from the deck
                            if ((checker(lastSelected, grid!!.grid[location]!!) || lastSelected >
                                        (gridSize * gridSize)) && (checker(location, activeCard!!) ||  location > (gridSize * gridSize))) {
                                // If cards won't conflict when swapped, swap cards
                                setCard(lastSelected, grid!!.grid[location])
                                setCard(location, activeCard)
                            } else {
                                // Warn of tile swap conflict
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
                }
                lastSelected = location
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

    private fun setCard(location: Int, card: Card?) {
        if (card != null) {
            grid!!.grid[location] = card
            grid!!.buttonGrid[location].setAttrs(card.getFilename(), card.getColor(), 0, R.color.gameSBorder)
        } else {
            grid!!.grid[location] = null
            grid!!.buttonGrid[location].setAttrs("clear", R.color.gameWhite, 0, R.color.gameSBorder)
        }
    }

    private fun clearTile(position: Int) {
        if (position == (gridSize * gridSize)) {
            deck!!.arr.removeAt(0)
            if (deck!!.arr.size >= 1) {
                setCard((gridSize * gridSize), deck!!.arr[0])
            } else {
                grid!!.grid[gridSize * gridSize] = null
                grid!!.buttonGrid[gridSize * gridSize].setAttrs("empty", R.color.gameWhite, 0, R.color.gameSBorder)
                grid!!.buttonGrid[gridSize * gridSize].isEnabled = false
            }
        } else {
            setCard(position, null)
        }
    }

    private fun checker(position: Int, card: Card): Boolean {
        fun checkTile(position: Int, card: Card): Boolean {
            if (position > (gridSize * gridSize)) { return true }
            return if (grid!!.grid[position] != null) {
                card.matches(grid!!.grid[position]!!)
            } else { true }
        }
        fun left(x: Int): Int { return x + 1 }
        fun right(x: Int): Int { return x - 1 }
        fun up(x: Int): Int { return x + gridSize }
        fun down(x: Int): Int { return x - gridSize }

        if (position < gridSize) {
            if (position == 0) {
                // Bottom right corner; check tiles left and above
                if (!checkTile(left(position), card)) { return false }
                if (!checkTile(up(position), card)) { return false }
            } else if (position == gridSize - 1) {
                // Bottom left corner; check tiles right and above
                if (!checkTile(right(position), card)) { return false }
                if (!checkTile(up(position), card)) { return false }
            } else {
                // Bottom edge; check tiles left, right and above
                if (!checkTile(left(position), card)) { return false }
                if (!checkTile(right(position), card)) { return false }
                if (!checkTile(up(position), card)) { return false }
            }
        } else if (position % gridSize == 0) {
            if (position == gridSize * (gridSize - 1)) {
                // Top right corner; check tiles left and below
                if (!checkTile(left(position), card)) { return false }
                if (!checkTile(down(position), card)) { return false }
            } else {
                // Right edge; check tiles left, above and below
                if (!checkTile(left(position), card)) { return false }
                if (!checkTile(up(position), card)) { return false }
                if (!checkTile(down(position), card)) { return false }
            }
        } else if (position % gridSize == gridSize - 1) {
            if (position == (gridSize * gridSize) - 1) {
                // Top left corner; check tiles right and below
                if (!checkTile(right(position), card)) { return false }
                if (!checkTile(down(position), card)) { return false }
            } else {
                // Left edge; check tiles right, above and below
                if (!checkTile(right(position), card)) { return false }
                if (!checkTile(up(position), card)) { return false }
                if (!checkTile(down(position), card)) { return false }
            }
        } else if (position > gridSize * (gridSize - 1)) {
            // Top edge; check tiles left, right and below
            if (!checkTile(left(position), card)) { return false }
            if (!checkTile(right(position), card)) { return false }
            if (!checkTile(down(position), card)) { return false }
        } else {
            // Central tile; check tiles left, right, above and below
            if (!checkTile(left(position), card)) { return false }
            if (!checkTile(right(position), card)) { return false }
            if (!checkTile(up(position), card)) { return false }
            if (!checkTile(down(position), card)) { return false }
        }
        return true
    }

    fun resetHandler(): View.OnClickListener {
        return View.OnClickListener {
            resetGame(this)
        }
    }
}