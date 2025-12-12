package com.mathyas.cotizadorbancario.data.models

data class ClienteRequest (
    val documentoIdentidad: String = "",
    val nombreCompleto: String = "",
    val telefono: String = "",
)