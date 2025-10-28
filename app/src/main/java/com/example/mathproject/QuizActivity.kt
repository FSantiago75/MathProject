package com.example.mathproject

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val timerProgressView = findViewById<View>(R.id.timerProgress)
        var totalTime = 0L
        val timerTextView = findViewById<TextView>(R.id.textViewTimer)
        val btnBack = findViewById<Button>(R.id.ga_btnBack)

        val Dificuldade = intent.getStringExtra("EXTRA_DIFICULDADE")
        val logDificuldade = Dificuldade ?: "NÃO DEFINIDO"
        when(logDificuldade) {
            "Fácil" -> totalTime = 90000L
            "Médio" -> totalTime = 60000L
            "Difícil" -> totalTime = 30000L
        }


        val startColor = Color.parseColor("#7ACA6F")
        val endColor = Color.parseColor("#FF0000")

        object : CountDownTimer(totalTime, 100) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                val timeFormatted = String.format("%02d:%02d", minutes, seconds)
                timerTextView.text = timeFormatted

                val colorProgress = 1f - millisUntilFinished.toFloat() / totalTime
                val currentColor = interpolateColor(startColor, endColor, colorProgress)
                timerProgressView.setBackgroundColor(currentColor)

                val scaleProgress = millisUntilFinished.toFloat() / totalTime
                timerProgressView.scaleX = scaleProgress
            }

            override fun onFinish() {
                timerProgressView.setBackgroundColor(endColor)
                timerProgressView.scaleX = 0f
                timerTextView.text = "00:00"
            }
        }.start()

        val Serie = intent.getStringExtra("EXTRA_SERIE")
        val Conteudo = intent.getStringExtra("EXTRA_CONTEUDO")

        val logSerie = Serie ?: "NÃO DEFINIDO"
        val logConteudo = Conteudo ?: "NÃO DEFINIDO"


        Log.d("tag", "Serie: $logSerie, Conteudo: $logConteudo, Dificuldade: $logDificuldade")

        btnBack.setOnClickListener {
            val difficulty = Intent(this, DifficultyActivity::class.java).apply{
                putExtra("EXTRA_SERIE", logSerie)
                putExtra("EXTRA_CONTEUDO", logConteudo)
            }
            startActivity(difficulty)
        }
    }

    fun interpolateColor(startColor: Int, endColor: Int, progress: Float): Int {
        val startR = (startColor shr 16) and 0xFF
        val startG = (startColor shr 8) and 0xFF
        val startB = startColor and 0xFF

        val endR = (endColor shr 16) and 0xFF
        val endG = (endColor shr 8) and 0xFF
        val endB = endColor and 0xFF

        val r = (startR + (endR - startR) * progress).toInt()
        val g = (startG + (endG - startG) * progress).toInt()
        val b = (startB + (endB - startB) * progress).toInt()

        return (0xFF shl 24) or (r shl 16) or (g shl 8) or b
    }
}
