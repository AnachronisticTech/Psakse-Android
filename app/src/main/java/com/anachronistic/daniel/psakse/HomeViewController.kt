package com.anachronistic.daniel.psakse

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout

class HomeViewController : AppCompatActivity() {

    private var tutorialView: Button? = null
    private var challengeView: Button? = null
    private var randomView: Button? = null

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

    private fun comingSoon(): View.OnClickListener {
        return View.OnClickListener {
            Toast.makeText(this, "This feature is coming soon!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_view_controller)
        supportActionBar?.hide()

        window.decorView.findViewById<View>(R.id.root).doOnLayout {
            tutorialView = findViewById(R.id.tutorialView)
            challengeView = findViewById(R.id.challengeView)
            randomView = findViewById(R.id.randomView)
            setupButtonView(tutorialView!!, "Tutorial", Colors.Green, comingSoon())
            setupButtonView(challengeView!!, "Challenge Mode", Colors.Yellow, goToPuzzleSelect())
            setupButtonView(randomView!!, "Random Puzzle", Colors.Purple, goToGame())
        }
    }
}
