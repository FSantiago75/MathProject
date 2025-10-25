package com.example.mathproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DifficultyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_difficulty)

        val btnEasy: Button = findViewById(R.id.btnEasy)
        val btnMedium: Button = findViewById(R.id.btnMedium)
        val btnHard: Button = findViewById(R.id.btnHard)
        val btnBack: Button = findViewById(R.id.btnBack)

        val Serie = intent.getStringExtra("EXTRA_SERIE")
        val Conteudo = intent.getStringExtra("EXTRA_CONTEUDO")
        val logSerie = Serie ?: "NÃO DEFINIDO"
        val logConteudo = Conteudo ?: "NÃO DEFINIDO"

        btnEasy.setOnClickListener {

            val quiz = Intent(this, QuizActivity::class.java).apply {
                putExtra("EXTRA_SERIE", logSerie)
                putExtra("EXTRA_CONTEUDO", logConteudo)
                putExtra("EXTRA_DIFICULDADE", btnEasy.text)
            }

            // (Opcional) Adiciona dados para a próxima tela, como o tema escolhido
            //intent.putExtra("EXTRA_TEMA", "Matemática Básica")

            // Inicia a nova Activity
            startActivity(quiz)
        }

        btnMedium.setOnClickListener {

            val quiz = Intent(this, QuizActivity::class.java).apply {
                putExtra("EXTRA_SERIE", logSerie)
                putExtra("EXTRA_CONTEUDO", logConteudo)
                putExtra("EXTRA_DIFICULDADE", btnMedium.text)
            }

            // (Opcional) Adiciona dados para a próxima tela, como o tema escolhido
            //intent.putExtra("EXTRA_TEMA", "Matemática Básica")

            // Inicia a nova Activity
            startActivity(quiz)
        }

        btnHard.setOnClickListener {

            val quiz = Intent(this, QuizActivity::class.java).apply {
                putExtra("EXTRA_SERIE", logSerie)
                putExtra("EXTRA_CONTEUDO", logConteudo)
                putExtra("EXTRA_DIFICULDADE", btnHard.text)
            }

            // (Opcional) Adiciona dados para a próxima tela, como o tema escolhido
            //intent.putExtra("EXTRA_TEMA", "Matemática Básica")

            // Inicia a nova Activity
            startActivity(quiz)
        }

        btnBack.setOnClickListener {
            val content = Intent(this, ContentActivity::class.java).apply{
                putExtra("EXTRA_SERIE", logSerie)
            }
            startActivity(content)
        }
    }


}
