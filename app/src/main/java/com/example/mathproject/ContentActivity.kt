package com.example.mathproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ContentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        val btnContent1: Button = findViewById(R.id.btnContent1)
        val btnContent2: Button = findViewById(R.id.btnContent2)
        val btnContent3: Button = findViewById(R.id.btnContent3)
        val btnContent4: Button = findViewById(R.id.btnContent4)
        val btnContent5: Button = findViewById(R.id.btnContent5)
        val btnBack: Button = findViewById(R.id.btnBack)

        val Serie = intent.getStringExtra("EXTRA_SERIE")
        val logSerie = Serie ?: "NÃO DEFINIDO"

        btnContent1.setOnClickListener {
            val difficulty = Intent(this, DifficultyActivity::class.java).apply {
                putExtra("EXTRA_SERIE", logSerie)
                putExtra("EXTRA_CONTEUDO", btnContent1.text)
            }
            startActivity(difficulty)
        }

        btnContent2.setOnClickListener {
            val difficulty = Intent(this, DifficultyActivity::class.java).apply {
                putExtra("EXTRA_SERIE", logSerie)
                putExtra("EXTRA_CONTEUDO", btnContent2.text)
            }
            startActivity(difficulty)
        }

        btnContent3.setOnClickListener {
            val difficulty = Intent(this, DifficultyActivity::class.java).apply {
                putExtra("EXTRA_SERIE", logSerie)
                putExtra("EXTRA_CONTEUDO", btnContent3.text)
            }
            startActivity(difficulty)
        }

        btnContent4.setOnClickListener {
            val difficulty = Intent(this, DifficultyActivity::class.java).apply {
                putExtra("EXTRA_SERIE", logSerie)
                putExtra("EXTRA_CONTEUDO", btnContent4.text)
            }
            startActivity(difficulty)
        }

        btnContent5.setOnClickListener {
            val difficulty = Intent(this, DifficultyActivity::class.java).apply {
                putExtra("EXTRA_SERIE", logSerie)
                putExtra("EXTRA_CONTEUDO", btnContent5.text)
            }
            startActivity(difficulty)
        }

        btnBack.setOnClickListener {
            val series = Intent(this, SeriesActivity::class.java)
            startActivity(series)
        }
    }
}
