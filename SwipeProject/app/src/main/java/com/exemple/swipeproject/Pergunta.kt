package com.exemple.swipeproject

import java.io.Serializable

data class Pergunta(
    val pergunta: String,
    val resposta: String,
    val id: Int,
    var respostaUsuario: String? = null
): Serializable