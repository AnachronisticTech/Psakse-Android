package com.anachronistic.daniel.psakse

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL

class SelectViewController : AppCompatActivity() {

    private var tableData = arrayListOf<Puzzle>()
    private var tableDataAdapter: PuzzlesAdapter? = null
    private var challengeView: ListView? = null
    private var homeView: Button? = null

    data class Puzzle(val numID: Int, val id: String, val properties: String)

    class PuzzlesAdapter(context: Context, puzzles: ArrayList<Puzzle>): ArrayAdapter<Puzzle>(context, android.R.layout.simple_list_item_1, puzzles) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val newView: View = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
            val textView = newView.findViewById<TextView>(android.R.id.text1)
            textView.text = "Puzzle ${position + 1}"

            return newView
        }
    }

    private fun setupButtonView(button: Button, title: String, color: Colors, action: View.OnClickListener) {
        button.text = title
        val layer = GradientDrawable()
        layer.cornerRadius = 90.0f
        layer.setStroke(9, Color.DKGRAY)
        layer.setColor(ContextCompat.getColor(this, color.getColor()))
        button.background = layer
        button.isSoundEffectsEnabled = false
        button.setOnClickListener(action)
    }

    private fun goToHome(): View.OnClickListener {
        return View.OnClickListener {
            startActivity(Intent(this, HomeViewController::class.java))
        }
    }

    private fun getJSON() {
        doAsync {
            val url = URL("https://anachronistic-tech.co.uk/projects/psakse/get_puzzles.php")
            val jps = JSONArray(url.readText())
            val puzzles = arrayListOf<Puzzle>()
            for (i in 0 until jps.length()) {
                val jp = JSONTokener(jps[i].toString()).nextValue() as JSONObject
                puzzles.add(Puzzle(jp.getInt("numID"), jp.getString("id"), jp.getString("properties")))
            }
            puzzles.sortBy { it.numID }
            tableData = puzzles
            uiThread {
                tableDataAdapter!!.addAll(puzzles)
                tableDataAdapter!!.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_view_controller)
        supportActionBar?.hide()

        window.decorView.findViewById<View>(R.id.root).doOnLayout {
            challengeView = findViewById(R.id.challengeView)
            tableDataAdapter = PuzzlesAdapter(this, tableData)
            challengeView!!.adapter = tableDataAdapter
            challengeView!!.setOnItemClickListener { _, _, position, _ ->
                val puzzle = tableData[position]
                val intent = Intent(this, GameViewController::class.java)
                intent.putExtra("puzzleID", puzzle.id)
                intent.putExtra("override", puzzle.properties)
                startActivity(intent)
            }
            homeView = findViewById(R.id.homeView)
            setupButtonView(homeView!!, "Home", Colors.Green, goToHome())
            getJSON()
        }
    }
}
