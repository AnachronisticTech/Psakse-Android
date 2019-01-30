package uk.co.anachronistic_tech.daniel.psakse

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutCompat
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout


class Psakse : AppCompatActivity() {

    val gridSizeMaster = 5
    val wildcards = 2
    var numberSymbols = 4
    var numberColors = 4
    var gridExists = false
    var grid: Grid? = null
    var deck: Deck? = null
    var activeCard: Card? = null
    var lastSelected = -1
    var gameComplete = false
    var mode = "Random"
    var puzzleID = -1
    var overrideDeck: ArrayList<Card>? = null
    var overrideRandArray: ArrayList<Int>? = null

    var view: View? = null

    class Grid {
        private var gridSize = 5
        var grid = ArrayList<Card>()
        var buttonGrid = ArrayList<Button>()

        constructor(gridSize: Int) {
            this.gridSize = gridSize
        }

        fun drawMainGrid() {
            val width = DisplayMetrics().widthPixels
//            val height = DisplayMetrics().heightPixels
            val gridBorder = 20
            val gridHeight = width - (2 * gridBorder)
            val tileSpacing = 5
            val tileHeight = (gridHeight - ((this.gridSize - 1) * tileSpacing)) / this.gridSize
            for (i in 0..((this.gridSize * this.gridSize) - 1)) {
                val gridX = i % this.gridSize
                val gridY = (i / this.gridSize)
                val x = gridBorder + (gridX * tileHeight) + (gridX * tileSpacing)
                val y = (gridBorder * 3) + (gridY * tileHeight) + (gridY * tileSpacing)
                val button = createButton(x, y, tileHeight, i)
                this.buttonGrid.add(button)
            }
        }

        fun drawMainBg(): View {
            val gridBorder = 20
            val width = DisplayMetrics().widthPixels
            val gridHeight = width - (2 * gridBorder)
            val top = gridBorder * 3
            val right = width - gridBorder
            val rect = Rect(gridBorder, top, right, right)
            val shape = GradientDrawable()
            shape.bounds = rect
            shape.setColor(Color.BLACK)
            val view = View(this as Context)
            view.background = shape
            return view
        }

        fun drawSideGrid() {
            val width = DisplayMetrics().widthPixels
            val gridBorder = 20
            val gridHeight = width - (2 * gridBorder)
            val tileSpacing = 5
            val tileHeight = (gridHeight - ((this.gridSize - 1) * tileSpacing)) / this.gridSize
            for (i in 0..4) {
                val gridX = i % this.gridSize
                val x = gridBorder + (gridX * tileHeight) + (gridX * tileSpacing)
                val y = gridHeight + 100
                val button = createButton(x, y, tileHeight, i)
                this.buttonGrid.add(button)
            }
        }

        fun drawSideBg(): View {
            val gridBorder = 20
            val width = DisplayMetrics().widthPixels
            val gridHeight = width - (2 * gridBorder)
            val top = gridHeight + 95
            val right = width - gridBorder
            val tileSpacing = 5
            val tileHeight = (gridHeight - ((this.gridSize - 1) * tileSpacing)) / this.gridSize
            val bottom = tileHeight + (2 * tileSpacing)
            val rect = Rect(gridBorder, top, right, bottom)
            val shape = GradientDrawable()
            shape.bounds = rect
            shape.setColor(Color.BLACK)
            val view = View(this as Context)
            view.background = shape
            return view
        }

        private fun createButton(x: Int, y: Int, height: Int, tag: Int): Button {
//            val button = Button(frame: CGRect(x: x, y: y, width: height, height: height))
            val button = Button(this as Context)
            button.width = height
            button.height = height
            button.x = x as Float
            button.y = y as Float
            button.setBackgroundColor(Color.WHITE)
//            button.adjustsImageWhenDisabled = false
            button.setText(tag)
            button.tag = tag
//            button.titleLabel?.adjustsFontSizeToFitWidth = true
//            button.layer.borderColor = UIColor.black.cgColor
//            button.layer.borderWidth = 0
            return button
        }

    }

    enum class Symbols {
        Psi,
        A,
        Xi,
        E;
    }

    enum class Colors {
        Green,
        Yellow,
        Purple,
        Orange;
    }

    abstract class Card {
         abstract fun matches(other: Card): Boolean
         abstract fun getFilename(): String
         abstract fun getColor(): Color
    }

    class Normal : Card {
        var symbol: Symbols
        var color: Colors

        constructor(symbol: Symbols, color: Colors) {
            this.symbol = symbol
            this.color = color
        }

        override fun getFilename(): String {
            return when (symbol) {
                Symbols.Psi -> "psi.png"
                Symbols.A -> "a.png"
                Symbols.Xi -> "xi.png"
                Symbols.E -> "e.png"
            }
        }

        override fun getColor(): Color {
            return when (color) {
                Colors.Green -> Color.valueOf(175.0f/255.0f, 227.0f/255.0f, 70.0f/255.0f, 1.0f)
                Colors.Yellow -> Color.valueOf(255.0f/255.0f, 220.0f/255.0f, 115.0f/255.0f, 1.0f)
                Colors.Purple -> Color.valueOf(236.0f/255.0f, 167.0f/255.0f, 238.0f/255.0f, 1.0f)
                Colors.Orange -> Color.valueOf(255.0f/255.0f, 153.0f/255.0f, 51.0f/255.0f, 1.0f)
            }
        }

        override fun matches(other: Card): Boolean {
            if (other is Wild) {
                return true
            } else {
                val otherCard = other as Normal
                return (this.symbol == otherCard.symbol) || (this.color == otherCard.color)
            }
        }
    }

    class Wild : Card {
        constructor() {

        }

        override fun getFilename(): String {
            return "dot.png"
        }

        override fun getColor(): Color {
            return Color.valueOf(255.0f/255.0f, 180.0f/255.0f, 188.0f/255.0f, 1.0f) // Get actual colour
        }

        override fun matches(other: Card): Boolean {
            return true
        }
    }

    class Deck {

        var arr = arrayListOf<Card>()
        var numberSymbols: Int
        var numberColors: Int

        constructor(numberSymbols: Int, numberColors: Int) {
            this.numberSymbols = numberSymbols
            this.numberColors = numberColors
        }

        fun populateDeck() {
            for (symbol in Symbols.values()) {
                for (color in Colors.values()) {
                    this.arr.add(Normal(symbol, color))
                    this.arr.add(Normal(symbol, color))
                }
            }
//			print("\(self.arr.count) cards added to the deck")
        }

        fun addWildCards(count: Int)  {
            for (i in 0..(count - 1)) {
                this.arr.add(Wild())
            }
//			print("\(count) wildcards added to the deck")
        }

        fun removeCards(gridSize: Int, wildcards: Int) {
            val gridTotal = this.arr.count() - (gridSize * gridSize) + wildcards - 1
            for (i in 0..gridTotal) {
                this.arr.removeAt(this.arr.lastIndex)
            }
//			print("\(self.arr.count) cards remaining in the deck")
        }

        fun createOverrideDeck(overrideDeck: ArrayList<Card>) {
            for (i in 3..(overrideDeck.size -  1)) {
                val card = overrideDeck[i]
                this.arr.add(card)
            }
        }

        fun finalShuffle() {
//            var last = this.arr.size - 1
//            while (last > 0) {
//                val rand = (Int)(Math.random(last))
//                this.arr.swapAt(last, rand)
//                last -= 1
//            }
            this.arr.shuffle()
//			print("Deck prepared. \(self.arr.count) cards available.")
        }
    }

    fun resetGame() {
        gameComplete = false
        deck = Deck(numberSymbols, numberColors)
        if (!gridExists) {
            val layout = findViewById<ConstraintLayout>(R.id.background)
            grid = Grid(gridSizeMaster)
            grid!!.drawMainGrid()
            grid!!.drawMainBg()
            for (button in grid!!.buttonGrid) {
                button.isEnabled = true
                layout.addView(button)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_psakse)
//        resetGame()
        val button = Button(this)
        button.width = 100
        button.height = 100
        button.x = 100.0f
        button.y = 100.0f
        button.setBackgroundColor(Color.BLACK)
        val layout = findViewById<ConstraintLayout>(R.id.background)
        layout.addView(button)
    }

}
