package com.example.mathproject

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val timerProgressView = findViewById<View>(R.id.timerProgress)

        val startColor = Color.parseColor("#7ACA6F")
        val endColor = Color.parseColor("#FF0000")
        val totalTime = 10000L // 10 segundos

        object : CountDownTimer(totalTime, 100) {
            override fun onTick(millisUntilFinished: Long) {
                val colorProgress = 1f - millisUntilFinished.toFloat() / totalTime
                val currentColor = interpolateColor(startColor, endColor, colorProgress)
                timerProgressView.setBackgroundColor(currentColor)

                val scaleProgress = millisUntilFinished.toFloat() / totalTime
                timerProgressView.scaleX = scaleProgress
            }

            override fun onFinish() {
                timerProgressView.setBackgroundColor(endColor)
                timerProgressView.scaleX = 0f
            }
        }.start()
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
