package com.example.mathproject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

class SeriesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var confirmButton: Button
    private lateinit var backButton: Button
    private lateinit var layoutManager: LinearLayoutManager
    private val snapHelper = LinearSnapHelper()

    // Lista de números que queremos mostrar
    private val numbers = listOf("6°", "7°", "8°", "9°")
    private var currentSelectedValue = "7°" // Valor inicial que você quer focar (MUDADO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_series)

        // Encontre os componentes do seu layout XML
        // O RecyclerView agora tem o ID 'ga_carrossel'
        recyclerView = findViewById(R.id.ga_carrossel)
        confirmButton = findViewById(R.id.ga_btnConfirm)
        backButton = findViewById(R.id.ga_btnBack)

        // Configurar o RecyclerView
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = NumberAdapter(numbers)

        // Anexar o SnapHelper (mágica do "snap" central)
        snapHelper.attachToRecyclerView(recyclerView)

        // Encontrar a posição inicial (do número 6)
        val initialPosition = numbers.indexOf(currentSelectedValue)
        if (initialPosition != -1) {
            // Rolar para a posição inicial
            // Damos um post para garantir que o layout esteja pronto
            recyclerView.post {
                // --- LÓGICA SIMPLIFICADA ---
                // Apenas pule para a posição. Não precisa de cálculos complexos.
                layoutManager.scrollToPosition(initialPosition)

                // E então atualize a view imediatamente.
                // Como isso está dentro do 'post', o layout já estará pronto.
                updateCarouselView()
            }
        }

        // Adicionar o listener para mudar a aparência
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // Quando a rolagem parar
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    updateCarouselView()
                }
            }
        })

        // Atualizar a view inicial após o layout
        // ESTE BLOCO ESTAVA CAUSANDO O BUG (RACE CONDITION)
        /*
        recyclerView.post {
            updateCarouselView()
        }
        */

        // Ação do botão Confirmar
        confirmButton.setOnClickListener {
            Log.d("SeriesActivity", "Série selecionada: $currentSelectedValue")
            // Adicione sua lógica aqui (ex: salvar, próxima tela)
        }

        // Ação do botão Voltar
        backButton.setOnClickListener {
            finish() // Fecha a activity atual
        }
    }

    private fun updateCarouselView() {
        val centerView = snapHelper.findSnapView(layoutManager) ?: return
        val centerPosition = layoutManager.getPosition(centerView)

        if (centerPosition == RecyclerView.NO_POSITION) return

        currentSelectedValue = numbers[centerPosition]

        // Iterar por todos os itens visíveis e atualizar a aparência
        for (i in 0 until layoutManager.childCount) {
            val child = layoutManager.getChildAt(i) ?: continue
            val childPosition = layoutManager.getPosition(child)
            val textView = child.findViewById<TextView>(R.id.item_textView)

            if (childPosition == centerPosition) {
                // Item Central (Selecionado)
                textView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                // Aumenta o tamanho (usei 1.5x, ajuste como preferir)
                child.animate().scaleX(1.5f).scaleY(1.5f).setDuration(200).start()
            } else {
                // Itens Laterais (Não selecionados)
                textView.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
                // Volta ao tamanho normal
                child.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            }
        }
    }

    // --- Adapter Interno para o RecyclerView ---
    // Esta classe gerencia os dados (números) e os vincula ao item_number.xml
    inner class NumberAdapter(private val items: List<String>) :
        RecyclerView.Adapter<NumberAdapter.NumberViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
            // Cria a view usando o seu item_number.xml
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_number, parent, false)
            return NumberViewHolder(view)
        }

        override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
            // Define o texto do número para a posição atual
            holder.textView.text = items[position]
        }

        override fun getItemCount(): Int = items.size

        // Esta classe "segura" a view de cada item
        inner class NumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.item_textView)
        }
    }
}



