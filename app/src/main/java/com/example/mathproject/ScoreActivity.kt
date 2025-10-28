package com.example.mathproject

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ScoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        val txtScore = findViewById<TextView>(R.id.scoreField)
        val txtTime = findViewById<TextView>(R.id.timeField)

        val pontuacao = intent.getStringExtra("EXTRA_PONTUACAO")
        val tempo = intent.getStringExtra("EXTRA_TEMPO")


        val logPontuacao = pontuacao ?: "00"
        val logTempo = tempo ?: "00:00"

        Log.d("tag", "Pontuacao:" + pontuacao+ "tempo" + logTempo)

        txtScore.text = logPontuacao
        txtTime.text = logTempo
    }
}
