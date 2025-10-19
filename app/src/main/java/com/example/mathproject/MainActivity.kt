package com.example.mathproject

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.select_school_year)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

// Enum para as matérias
enum class Subject {
    NUMEROS, ALGEBRA, GEOMETRIA, GRANDEZAS, PROBABILIDADE
}

// Enum para as dificuldades
enum class Difficulty {
    FACIL, MEDIO, DIFICIL
}

// Modelo de dados para representar uma questão do quiz
data class Question(
    val number: Int,
    val content: String,
    val answers: List<String>,
    val correctAnswerIndex: Int,
    val subject: Subject,
    val difficulty: Difficulty,
    val basePoints: Int // Pontos base por acerto (varia por dificuldade)
)

class quiz : AppCompatActivity() {

    // --- Variáveis para os componentes da tela ---
    private lateinit var questionNumberTextView: TextView
    private lateinit var questionContentTextView: TextView
    private lateinit var timerTextView: TextView
    private lateinit var timerProgressView: View
    private lateinit var answerButtons: List<Button>

    // --- Variáveis de controle do Quiz ---
    private var countDownTimer: CountDownTimer? = null
    private var currentQuestionIndex = 0
    private var totalPoints = 0
    private var timeLeft: Long = 0
    private var selectedSubject: Subject = Subject.NUMEROS
    private var selectedDifficulty: Difficulty = Difficulty.MEDIO

    // --- Banco de questões organizado por matéria e dificuldade ---
    private val allQuestions = mapOf(
        Subject.NUMEROS to listOf(
            // Fácil
            Question(1, "Qual é o resultado de 15 + 27?", listOf("32", "42", "38", "45"), 1, Subject.NUMEROS, Difficulty.FACIL, 10),
            Question(2, "Quanto é 8 × 9?", listOf("72", "81", "64", "76"), 0, Subject.NUMEROS, Difficulty.FACIL, 10),
            Question(3, "Qual é o dobro de 24?", listOf("46", "48", "52", "44"), 1, Subject.NUMEROS, Difficulty.FACIL, 10),
            Question(4, "Quanto é 100 ÷ 4?", listOf("20", "25", "30", "15"), 1, Subject.NUMEROS, Difficulty.FACIL, 10),

            // Médio
            Question(5, "Qual é o MMC de 12 e 18?", listOf("24", "36", "48", "54"), 1, Subject.NUMEROS, Difficulty.MEDIO, 20),
            Question(6, "Qual é a raiz quadrada de 169?", listOf("11", "13", "15", "17"), 1, Subject.NUMEROS, Difficulty.MEDIO, 20),
            Question(7, "Quanto é 3³ + 4²?", listOf("25", "33", "43", "37"), 2, Subject.NUMEROS, Difficulty.MEDIO, 20),

            // Difícil
            Question(8, "Qual é o resto da divisão de 247 por 13?", listOf("0", "1", "2", "3"), 1, Subject.NUMEROS, Difficulty.DIFICIL, 40),
            Question(9, "Quantos divisores positivos tem o número 72?", listOf("10", "12", "14", "16"), 1, Subject.NUMEROS, Difficulty.DIFICIL, 40),
            Question(10, "Qual é o valor de 1 + 2 + 3 + ... + 50?", listOf("1250", "1275", "1300", "1325"), 1, Subject.NUMEROS, Difficulty.DIFICIL, 40)
        ),

        Subject.ALGEBRA to listOf(
            // Fácil
            Question(1, "Se x + 5 = 12, qual é o valor de x?", listOf("5", "6", "7", "8"), 2, Subject.ALGEBRA, Difficulty.FACIL, 10),
            Question(2, "Qual é o resultado de 3x + 2x?", listOf("5x", "6x", "x⁵", "5"), 0, Subject.ALGEBRA, Difficulty.FACIL, 10),
            Question(3, "Se y = 2x + 1 e x = 3, qual é o valor de y?", listOf("5", "6", "7", "8"), 2, Subject.ALGEBRA, Difficulty.FACIL, 10),

            // Médio
            Question(4, "Qual é a solução da equação x² - 5x + 6 = 0?", listOf("1 e 6", "2 e 3", "-2 e -3", "0 e 5"), 1, Subject.ALGEBRA, Difficulty.MEDIO, 20),
            Question(5, "Qual é o valor de (a + b)²?", listOf("a² + b²", "a² + 2ab + b²", "a² - 2ab + b²", "a² + ab + b²"), 1, Subject.ALGEBRA, Difficulty.MEDIO, 20),
            Question(6, "Se 2x - 3 = 7, qual é o valor de x?", listOf("2", "3", "4", "5"), 3, Subject.ALGEBRA, Difficulty.MEDIO, 20),

            // Difícil
            Question(7, "Qual é o valor de x na equação log₂(x) = 5?", listOf("16", "25", "32", "64"), 2, Subject.ALGEBRA, Difficulty.DIFICIL, 40),
            Question(8, "Qual é a soma das raízes da equação 2x² - 8x + 6 = 0?", listOf("2", "3", "4", "5"), 2, Subject.ALGEBRA, Difficulty.DIFICIL, 40),
            Question(9, "Se f(x) = x³ - 3x² + 2x - 1, qual é f(2)?", listOf("-1", "0", "1", "2"), 0, Subject.ALGEBRA, Difficulty.DIFICIL, 40),
            Question(10, "Qual é o determinante da matriz [[2, 3], [1, 4]]?", listOf("5", "6", "7", "8"), 0, Subject.ALGEBRA, Difficulty.DIFICIL, 40)
        ),

        Subject.GEOMETRIA to listOf(
            // Fácil
            Question(1, "Quantos lados tem um triângulo?", listOf("2", "3", "4", "5"), 1, Subject.GEOMETRIA, Difficulty.FACIL, 10),
            Question(2, "Qual é a área de um quadrado de lado 5 cm?", listOf("20 cm²", "25 cm²", "30 cm²", "35 cm²"), 1, Subject.GEOMETRIA, Difficulty.FACIL, 10),
            Question(3, "Qual é o perímetro de um retângulo de 4 cm por 6 cm?", listOf("16 cm", "18 cm", "20 cm", "24 cm"), 2, Subject.GEOMETRIA, Difficulty.FACIL, 10),

            // Médio
            Question(4, "Qual é o volume de um cubo de aresta 3 cm?", listOf("9 cm³", "18 cm³", "27 cm³", "36 cm³"), 2, Subject.GEOMETRIA, Difficulty.MEDIO, 20),
            Question(5, "Qual é a área de um círculo com raio 7 cm? (Use π = 3,14)", listOf("143,86 cm²", "153,86 cm²", "163,86 cm²", "173,86 cm²"), 1, Subject.GEOMETRIA, Difficulty.MEDIO, 20),
            Question(6, "Quantas diagonais tem um pentágono?", listOf("3", "4", "5", "6"), 2, Subject.GEOMETRIA, Difficulty.MEDIO, 20),

            // Difícil
            Question(7, "Qual é a área total de um cilindro com raio 4 cm e altura 10 cm? (Use π = 3,14)", listOf("251,2 cm²", "301,44 cm²", "351,68 cm²", "401,92 cm²"), 2, Subject.GEOMETRIA, Difficulty.DIFICIL, 40),
            Question(8, "Qual é o volume de uma esfera com raio 6 cm? (Use π = 3,14)", listOf("904,32 cm³", "823,68 cm³", "753,6 cm³", "678,24 cm³"), 0, Subject.GEOMETRIA, Difficulty.DIFICIL, 40),
            Question(9, "Qual é a medida do ângulo interno de um octógono regular?", listOf("120°", "135°", "140°", "150°"), 1, Subject.GEOMETRIA, Difficulty.DIFICIL, 40),
            Question(10, "Qual é a altura de um triângulo equilátero de lado 10 cm?", listOf("5√3 cm", "8 cm", "8,66 cm", "10 cm"), 0, Subject.GEOMETRIA, Difficulty.DIFICIL, 40)
        ),

        Subject.GRANDEZAS to listOf(
            // Fácil
            Question(1, "Quantos metros há em 2 quilômetros?", listOf("200 m", "2000 m", "20 m", "20000 m"), 1, Subject.GRANDEZAS, Difficulty.FACIL, 10),
            Question(2, "Quantos litros há em 500 mililitros?", listOf("0,5 L", "5 L", "50 L", "500 L"), 0, Subject.GRANDEZAS, Difficulty.FACIL, 10),
            Question(3, "Quantos gramas há em 3 quilogramas?", listOf("300 g", "3000 g", "30 g", "30000 g"), 1, Subject.GRANDEZAS, Difficulty.FACIL, 10),

            // Médio
            Question(4, "Se 1 dólar vale R$ 5,50, quantos reais são 20 dólares?", listOf("R$ 100,00", "R$ 110,00", "R$ 120,00", "R$ 130,00"), 1, Subject.GRANDEZAS, Difficulty.MEDIO, 20),
            Question(5, "Um carro percorre 240 km em 3 horas. Qual é sua velocidade média?", listOf("60 km/h", "70 km/h", "80 km/h", "90 km/h"), 2, Subject.GRANDEZAS, Difficulty.MEDIO, 20),
            Question(6, "Quantos centímetros cúbicos há em 1 litro?", listOf("10 cm³", "100 cm³", "1000 cm³", "10000 cm³"), 2, Subject.GRANDEZAS, Difficulty.MEDIO, 20),

            // Difícil
            Question(7, "Se 1 polegada equivale a 2,54 cm, quantos centímetros há em 15 polegadas?", listOf("35,1 cm", "37,2 cm", "38,1 cm", "40,2 cm"), 2, Subject.GRANDEZAS, Difficulty.DIFICIL, 40),
            Question(8, "Um tanque tem capacidade para 5000 litros. Quantos metros cúbicos isso representa?", listOf("5 m³", "50 m³", "500 m³", "5000 m³"), 0, Subject.GRANDEZAS, Difficulty.DIFICIL, 40),
            Question(9, "Qual é a densidade de um objeto que tem massa de 300 g e volume de 150 cm³?", listOf("1 g/cm³", "2 g/cm³", "3 g/cm³", "4 g/cm³"), 1, Subject.GRANDEZAS, Difficulty.DIFICIL, 40),
            Question(10, "Converta 72 km/h para m/s.", listOf("15 m/s", "18 m/s", "20 m/s", "25 m/s"), 2, Subject.GRANDEZAS, Difficulty.DIFICIL, 40)
        ),

        Subject.PROBABILIDADE to listOf(
            // Fácil
            Question(1, "Qual é a probabilidade de sair cara no lançamento de uma moeda?", listOf("25%", "50%", "75%", "100%"), 1, Subject.PROBABILIDADE, Difficulty.FACIL, 10),
            Question(2, "Quantos resultados possíveis há no lançamento de um dado?", listOf("4", "5", "6", "7"), 2, Subject.PROBABILIDADE, Difficulty.FACIL, 10),
            Question(3, "Qual é a probabilidade de sair um número par no lançamento de um dado?", listOf("1/6", "1/3", "1/2", "2/3"), 2, Subject.PROBABILIDADE, Difficulty.FACIL, 10),

            // Médio
            Question(4, "Qual é a probabilidade de sair soma 7 no lançamento de dois dados?", listOf("1/6", "1/9", "1/12", "1/18"), 0, Subject.PROBABILIDADE, Difficulty.MEDIO, 20),
            Question(5, "Se retirarmos uma carta de um baralho de 52 cartas, qual a probabilidade de ser um Ás?", listOf("1/13", "1/26", "1/52", "4/52"), 0, Subject.PROBABILIDADE, Difficulty.MEDIO, 20),
            Question(6, "Qual é a probabilidade de não sair 6 no lançamento de um dado?", listOf("1/6", "2/6", "4/6", "5/6"), 3, Subject.PROBABILIDADE, Difficulty.MEDIO, 20),

            // Difícil
            Question(7, "Qual é a probabilidade de sair pelo menos uma cara no lançamento de duas moedas?", listOf("1/4", "1/2", "3/4", "1"), 2, Subject.PROBABILIDADE, Difficulty.DIFICIL, 40),
            Question(8, "Em uma urna com 5 bolas vermelhas e 3 azuis, qual a probabilidade de retirar 2 vermelhas consecutivamente sem reposição?", listOf("5/14", "10/28", "15/56", "20/64"), 0, Subject.PROBABILIDADE, Difficulty.DIFICIL, 40),
            Question(9, "Qual é a probabilidade de um casal ter 2 filhos do mesmo sexo?", listOf("1/4", "1/2", "3/4", "1"), 1, Subject.PROBABILIDADE, Difficulty.DIFICIL, 40),
            Question(10, "Qual é a probabilidade de acertar exatamente 2 questões em um teste de 5 questões com 4 alternativas cada?", listOf("≈ 0,26", "≈ 0,18", "≈ 0,12", "≈ 0,08"), 0, Subject.PROBABILIDADE, Difficulty.DIFICIL, 40)
        )
    )

    // Lista de questões selecionadas para o quiz atual
    private lateinit var selectedQuestions: List<Question>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Aqui você receberia as escolhas da tela inicial (por enquanto fixas para teste)
        // selectedSubject = Subject.NUMEROS // Exemplo: Matéria escolhida
        // selectedDifficulty = Difficulty.MEDIO // Exemplo: Dificuldade escolhida

        // Para teste, vamos usar valores fixos
        selectedSubject = Subject.NUMEROS
        selectedDifficulty = Difficulty.MEDIO

        // Seleciona 10 questões da matéria escolhida, balanceadas por dificuldade
        selectedQuestions = selectQuestionsForQuiz(selectedSubject, selectedDifficulty)

        initializeViews()
        setupAnswerButtons()
        loadQuestion(currentQuestionIndex)
    }

    private fun selectQuestionsForQuiz(subject: Subject, difficulty: Difficulty): List<Question> {
        val subjectQuestions = allQuestions[subject] ?: return emptyList()

        // Filtra questões pela dificuldade escolhida e mistura com outras dificuldades
        val easyQuestions = subjectQuestions.filter { it.difficulty == Difficulty.FACIL }.shuffled()
        val mediumQuestions = subjectQuestions.filter { it.difficulty == Difficulty.MEDIO }.shuffled()
        val hardQuestions = subjectQuestions.filter { it.difficulty == Difficulty.DIFICIL }.shuffled()

        // Distribuição baseada na dificuldade escolhida
        return when (difficulty) {
            Difficulty.FACIL -> {
                // 5 fáceis, 3 médias, 2 difíceis
                (easyQuestions.take(5) + mediumQuestions.take(3) + hardQuestions.take(2)).shuffled()
            }
            Difficulty.MEDIO -> {
                // 3 fáceis, 4 médias, 3 difíceis
                (easyQuestions.take(3) + mediumQuestions.take(4) + hardQuestions.take(3)).shuffled()
            }
            Difficulty.DIFICIL -> {
                // 2 fáceis, 3 médias, 5 difíceis
                (easyQuestions.take(2) + mediumQuestions.take(3) + hardQuestions.take(5)).shuffled()
            }
        }.take(10) // Garante que teremos exatamente 10 questões
    }

    private fun initializeViews() {
        questionNumberTextView = findViewById(R.id.textViewQuestionNumber)
        questionContentTextView = findViewById(R.id.textViewQuestionContent)
        timerTextView = findViewById(R.id.textViewTimer)
        timerProgressView = findViewById(R.id.timerProgress)
        answerButtons = listOf(
            findViewById(R.id.buttonAns1),
            findViewById(R.id.buttonAns2),
            findViewById(R.id.buttonAns3),
            findViewById(R.id.buttonAns4)
        )
    }

    private fun setupAnswerButtons() {
        answerButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                checkAnswer(index)
            }
        }
    }

    private fun loadQuestion(questionIndex: Int) {
        countDownTimer?.cancel()
        resetButtonColors()

        val question = selectedQuestions[questionIndex]

        // Atualiza os textos na tela
        questionNumberTextView.text = "Questão ${questionIndex + 1}}"
        questionContentTextView.text = question.content

        for (i in answerButtons.indices) {
            answerButtons[i].text = question.answers[i]
        }

        startTimer()
    }

    private fun startTimer() {
        val totalTimeInMillis: Long = 30000 // 30 segundos por questão

        timeLeft = totalTimeInMillis

        countDownTimer = object : CountDownTimer(totalTimeInMillis, 50) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                val secondsLeft = (millisUntilFinished / 1000) + 1
                timerTextView.text = String.format("0:%02d", secondsLeft)

                val progress = millisUntilFinished.toFloat() / totalTimeInMillis.toFloat()
                timerProgressView.scaleX = progress
            }

            override fun onFinish() {
                timerTextView.text = "0:00"
                timerProgressView.scaleX = 0f
                moveToNextQuestion(true)
            }
        }.start()
    }

    private fun checkAnswer(selectedIndex: Int) {
        countDownTimer?.cancel()

        val question = selectedQuestions[currentQuestionIndex]
        val correctIndex = question.correctAnswerIndex

        // Calcula pontos baseados na dificuldade e tempo restante
        val timeBonus = calculateTimeBonus()
        val pointsEarned = if (selectedIndex == correctIndex) {
            question.basePoints + timeBonus
        } else {
            0
        }

        totalPoints += pointsEarned

        // Mostra feedback visual
        if (selectedIndex == correctIndex) {
            answerButtons[selectedIndex].setBackgroundColor(Color.GREEN)
        } else {
            answerButtons[selectedIndex].setBackgroundColor(Color.RED)
            answerButtons[correctIndex].setBackgroundColor(Color.GREEN)
        }

        answerButtons.forEach { it.isEnabled = false }

        // Mostra pontos ganhos (opcional - você pode adicionar um TextView para isso)
        // pointsTextView.text = "Pontos: $totalPoints (+$pointsEarned)"

        moveToNextQuestion(false)
    }

    private fun calculateTimeBonus(): Int {
        // Bônus baseado no tempo restante (até 50% dos pontos base)
        val currentQuestion = selectedQuestions[currentQuestionIndex]
        val maxBonus = currentQuestion.basePoints / 2
        val timeRatio = timeLeft.toFloat() / 30000f // 30 segundos totais

        return (maxBonus * timeRatio).toInt()
    }

    private fun moveToNextQuestion(timeRanOut: Boolean) {
        val delay = if (timeRanOut) 0L else 2000L

        Handler(Looper.getMainLooper()).postDelayed({
            currentQuestionIndex++
            if (currentQuestionIndex < selectedQuestions.size) {
                loadQuestion(currentQuestionIndex)
            } else {
                showQuizEndScreen()
            }
        }, delay)
    }

    private fun showQuizEndScreen() {
        // Tela teste para ver se a pontuação esta funcionando
        questionContentTextView.text = "Quiz Finalizado!\nPontuação: $totalPoints"
        questionNumberTextView.visibility = View.GONE
        timerProgressView.visibility = View.GONE
        timerTextView.visibility = View.GONE
        findViewById<View>(R.id.gridLayoutAnswers).visibility = View.GONE


    }

    private fun resetButtonColors() {
        answerButtons.forEach {
            it.setBackgroundColor(Color.parseColor("#FF6200EE"))
            it.isEnabled = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}