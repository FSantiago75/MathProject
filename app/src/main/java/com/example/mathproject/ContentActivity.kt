package com.example.mathproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.IOException



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
        var logSerie = Serie ?: "NÃO DEFINIDO"
        logSerie = logSerie.substring(0,1)

        val listaConteudo = lerConteudoDaSerie(this, logSerie)
        val conteudo = listaConteudo?.split(',')

        btnContent1.text = conteudo?.get(0)
        btnContent2.text = conteudo?.get(1)
        btnContent3.text = conteudo?.get(2)
        btnContent4.text = conteudo?.get(3)
        btnContent5.text = conteudo?.get(4)

        //Log.d("tag", "Conteúdo: $jsonString")



        //Log.d("tag", "Serie $jsonString.7")

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

    fun lerConteudoDaSerie(context: Context, serie: String): String? {
        // 1. Leia o arquivo JSON como uma String
        val jsonString: String? = try {
            context.assets.open("conteudos.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            Log.e("JSON_READER", "Erro ao ler o arquivo JSON", ioException)
            null
        }

        // Se a string JSON foi lida com sucesso...
        if (jsonString != null) {
            try {
                // 2. Crie um objeto JSON a partir da string lida
                val jsonObject = JSONObject(jsonString)

                // 3. Acesse o objeto correspondente à chave "8"
                // Isso nos dá o objeto:
                // {
                //    "conteudo1": "Números Reais (Irracionais)",
                //    "conteudo2": "Expressões e Fatoração",
                //    ...
                // }
                val conteudoObject = jsonObject.getJSONObject(serie)

                // 4. Extraia os conteúdos individuais:
                val conteudo1 = conteudoObject.getString("conteudo1")
                val conteudo2 = conteudoObject.getString("conteudo2")
                val conteudo3 = conteudoObject.getString("conteudo3")
                val conteudo4 = conteudoObject.getString("conteudo4")
                val conteudo5 = conteudoObject.getString("conteudo5")


                val conteudo = conteudo1 + "," + conteudo2 + "," + conteudo3 + "," + conteudo4 + "," + conteudo5
                return conteudo

            } catch (e: Exception) {
                Log.e("JSON_READER", "Erro ao fazer o parsing ou acessar a chave '8'", e)
            }
        }
        return null
    }
}
