package com.example.mathproject

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.res.ColorStateList
import org.json.JSONObject

import java.io.IOException

class QuizActivity : AppCompatActivity() {

    private lateinit var buttonAns1: Button
    private lateinit var buttonAns2: Button
    private lateinit var buttonAns3: Button
    private lateinit var buttonAns4: Button
    private lateinit var txtQuestion: TextView
    private lateinit var txtQuestionNumber: TextView

    private lateinit var opCorreta: String
    private var pontos: Int = 0
    private var pontuacao: Int = 0
    private var numeroQuestao: Int = 0
    private var timeFormatted = "00:00"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val timerProgressView = findViewById<View>(R.id.timerProgress)
        var totalTime = 0L
        val timerTextView = findViewById<TextView>(R.id.textViewTimer)
        val btnBack = findViewById<Button>(R.id.ga_btnBack)

        buttonAns1 = findViewById<Button>(R.id.buttonAns1)
        buttonAns2 = findViewById<Button>(R.id.buttonAns2)
        buttonAns3 = findViewById<Button>(R.id.buttonAns3)
        buttonAns4 = findViewById<Button>(R.id.buttonAns4)
        txtQuestion = findViewById<TextView>(R.id.textViewQuestionContent)
        txtQuestionNumber = findViewById<TextView>(R.id.textViewQuestionNumber)




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
                timeFormatted = String.format("%02d:%02d", minutes, seconds)
                timerTextView.text = timeFormatted

                val colorProgress = 1f - millisUntilFinished.toFloat() / totalTime
                val currentColor = interpolateColor(startColor, endColor, colorProgress)
                timerProgressView.backgroundTintList = ColorStateList.valueOf(currentColor)

                val scaleProgress = millisUntilFinished.toFloat() / totalTime
                timerProgressView.scaleX = scaleProgress
            }

            override fun onFinish() {
                timerProgressView.backgroundTintList = ColorStateList.valueOf(endColor)
                timerProgressView.scaleX = 0f
                timerTextView.text = "00:00"
                val score = Intent(this@QuizActivity, ScoreActivity::class.java).apply {
                    putExtra("EXTRA_PONTUACAO", pontuacao.toString())
                    putExtra("EXTRA_TEMPO", timeFormatted)
                }
                startActivity(score)
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

        val quizData = loadQuizData(this)
        atualizaQuiz(numeroQuestao, quizData, logConteudo)



        buttonAns1.setOnClickListener {
            numeroQuestao++
            if(buttonAns1.text == opCorreta) {
                Log.d("tag", "certo")
                pontuacao += pontos

            } else {
                Log.d("tag", "errado")
            }
            if(!atualizaQuiz(numeroQuestao, quizData, logConteudo)) {
                Log.d("tag", "Pontuacao:" + pontuacao)
                val score = Intent(this, ScoreActivity::class.java).apply {
                    putExtra("EXTRA_PONTUACAO", pontuacao.toString())
                    putExtra("EXTRA_TEMPO", timeFormatted)
                }
                startActivity(score)
            }
        }

        buttonAns2.setOnClickListener {
            numeroQuestao++
            if(buttonAns2.text == opCorreta) {
                Log.d("tag", "certo")
                pontuacao += pontos
            } else {
                Log.d("tag", "errado")
            }
            if(!atualizaQuiz(numeroQuestao, quizData, logConteudo)) {
                Log.d("tag", "Pontuacao:" + pontuacao)
                val score = Intent(this, ScoreActivity::class.java).apply {
                    putExtra("EXTRA_PONTUACAO", pontuacao.toString())
                    putExtra("EXTRA_TEMPO", timeFormatted)
                }
                startActivity(score)
            }

        }

        buttonAns3.setOnClickListener {
            numeroQuestao++
            if(buttonAns3.text == opCorreta) {
                Log.d("tag", "certo")
                pontuacao += pontos
            } else {
                Log.d("tag", "errado")
            }
            if(!atualizaQuiz(numeroQuestao, quizData, logConteudo)) {
                Log.d("tag", "Pontuacao:" + pontuacao)
                val score = Intent(this, ScoreActivity::class.java).apply {
                    putExtra("EXTRA_PONTUACAO", pontuacao.toString())
                    putExtra("EXTRA_TEMPO", timeFormatted)
                }
                startActivity(score)
            }
        }

        buttonAns4.setOnClickListener {
            numeroQuestao++
            if(buttonAns4.text == opCorreta) {
                Log.d("tag", "certo")
                pontuacao += pontos

            } else {
                Log.d("tag", "errado")
            }
            if(!atualizaQuiz(numeroQuestao, quizData, logConteudo)) {
                Log.d("tag", "Pontuacao:" + pontuacao)
                val score = Intent(this, ScoreActivity::class.java).apply {
                    putExtra("EXTRA_PONTUACAO", pontuacao.toString())
                    putExtra("EXTRA_TEMPO", timeFormatted)
                }
                startActivity(score)
            }
        }



    }



    fun loadQuizData(context: Context): QuizData? {
        // 1. Acesso ao Asset Manager
        val inputStream = context.assets.open("perguntas_quiz.json")
        val reader = InputStreamReader(inputStream)

        // 2. Definindo o tipo para Gson (Muito Importante para o Map!)
        val type = object : TypeToken<QuizData>() {}.type

        // 3. Desserialização
        return try {
            Gson().fromJson<QuizData>(reader, type)
        } catch (e: Exception) {
            // Se o JSON estiver malformado ou as classes estiverem erradas
            e.printStackTrace()
            null
        } finally {
            // Fechar o leitor/stream para liberar recursos
            reader.close()
            inputStream.close()
        }
    }

    fun atualizaQuiz(numero: Int, quizData: QuizData?, logConteudo: String): Boolean {
        if (quizData != null) {
            val categoriaSelecionada: Categoria? = quizData[logConteudo]
            if (categoriaSelecionada != null) {
                val listaDePerguntas: List<Pergunta> = categoriaSelecionada.perguntas
                //Log.d("tag", "Total de perguntas em '$logConteudo': ${listaDePerguntas.size}")
                if (numero < listaDePerguntas.size) {
                    val pergunta = listaDePerguntas[numero]
                    txtQuestion.text = pergunta.pergunta
                    txtQuestionNumber.text = "Questão " + pergunta.id
                    buttonAns1.text = pergunta.opcoes[0]
                    buttonAns2.text = pergunta.opcoes[1]
                    buttonAns3.text = pergunta.opcoes[2]
                    buttonAns4.text = pergunta.opcoes[3]
                    opCorreta = pergunta.resposta
                    pontos = pergunta.pontos
                    return true
                } else {
                    return false
                }
            }
        }
        return false
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

data class Pergunta(
    val id: Int,
    val pergunta: String, // Sim, "pergunta"
    val pontos: Int,
    val opcoes: List<String>, // 'opcoes' é uma lista
    val resposta: String // 'resposta' é uma String
)

data class Categoria(
    val perguntas: List<Pergunta>
)

typealias QuizData = Map<String, Categoria>
