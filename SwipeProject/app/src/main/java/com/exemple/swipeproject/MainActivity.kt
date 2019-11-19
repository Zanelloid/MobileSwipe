package com.exemple.swipeproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

const val AVANCAR = "avancar"
const val VOLTAR =  "voltar"

class MainActivity : AppCompatActivity() {
    lateinit  var acertos: ArrayList<Int>
    lateinit  var erros: ArrayList<Int>
    private var contador = 0

    private val perguntas = arrayListOf<Pergunta>(
        Pergunta(
            pergunta = "Júpiter é o maior planeta do nosso Sistema Solar?",
            resposta = "sim",
            id = 1
        ),
        Pergunta(
            pergunta = "O planeta Netuno é o mais próximo do Sol??",
            resposta = "nao",
            id = 2
        ),
        Pergunta(
            pergunta = "Júpiter possui 79 satélites?",
            resposta = "sim",
            id = 3
        ),
        Pergunta(
            pergunta = "Saturno possui anéis ao seu redor?",
            resposta = "sim",
            id = 4
        ),
        Pergunta(
            pergunta = "Plutão é planeta?",
            resposta = "nao",
            id = 5
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swipe()
        acertos = arrayListOf()
        erros = arrayListOf()
        pergunta.text = perguntas[0].pergunta
    }

    private fun swipe() {
        tela.setOnTouchListener(object: OnSwipeTouchListener(this) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                controlarPerguntas(AVANCAR)
            }

            override fun onSwipeLeft() {
                super.onSwipeLeft()
                controlarPerguntas(VOLTAR)
            }

            override fun onSwipeTop() {
                super.onSwipeTop()
                perguntas[contador].respostaUsuario = "sim"
                registrarResposta(perguntas[contador], "sim")

            }

            override fun onSwipeBottom() {
                super.onSwipeBottom()
                perguntas[contador].respostaUsuario = "nao"
                registrarResposta(perguntas[contador], "nao")
            }
        })
    }

    private fun controlarPerguntas(acao: String) {
        when {
            contador == perguntas.size - 1 && acao == AVANCAR -> {
                contador = 0
            }
            contador < perguntas.size - 1 && acao == AVANCAR -> {
                contador += 1
            }
            contador == 0 && acao == VOLTAR -> {
                contador = perguntas.size - 1
            }
            contador > 0 && acao == VOLTAR -> {
                contador -= 1
            }
        }
        pergunta.text = perguntas[contador].pergunta
        feedbackUsuario()
    }

    private fun feedbackUsuario() {

        when (perguntas[contador].respostaUsuario) {


                "sim" -> pergunta.setTextColor(getColor(R.color.verde))
                "nao" ->  pergunta.setTextColor(getColor(R.color.vermelho))
                else -> pergunta.setTextColor(getColor(R.color.branco))
            }

    }

    fun registrarResposta(pergunta: Pergunta, resposta: String) {
        val respostaCerta = pergunta.resposta == resposta

        when {
            erros.contains(pergunta.id) && !respostaCerta -> return
            acertos.contains(pergunta.id) && respostaCerta -> return
            erros.contains(pergunta.id) && respostaCerta -> {
                erros.remove(pergunta.id)
                acertos.add(pergunta.id)
            }
            acertos.contains(pergunta.id) && !respostaCerta -> {
                acertos.remove(pergunta.id)
                erros.add(pergunta.id)
            }
            respostaCerta-> {
                acertos.add(pergunta.id)
            }
            else -> {
                erros.add(pergunta.id)
            }
        }
        feedbackUsuario()
        fimDeJogo()
    }

    private fun reestart() {
        erros.clear()
        acertos.clear()
        perguntas.forEach {
            it.respostaUsuario = null
        }
        pergunta.setTextColor(getColor(R.color.branco))
        pergunta.text = perguntas[0].pergunta
    }

    private fun fimDeJogo() {
        if (acertos.size + erros.size == perguntas.size) {
            mostrarAlerta()
        }
    }

    private fun mostrarAlerta() {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Fim de jogo")
            alert.setMessage(String.format(getString(R.string.menssagem, acertos.size, perguntas.size)))
            alert.setPositiveButton("Sim") { dialog, which -> reestart() }
            alert.setNegativeButton("Não") { dialog, which -> finish() }
            alert.setCancelable(false)
            alert.create().show()
    }
}
