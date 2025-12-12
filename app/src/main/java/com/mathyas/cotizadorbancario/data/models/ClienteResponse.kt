package com.mathyas.cotizadorbancario.data.models

data class ClienteResponse(
    val success: Boolean,
    val message: String,
    val data: ClienteData
)

data class ClienteData(
    val id: Long,
    val nombreCompleto: String,
    val documentoIdentidad: String,
    val email: String,
    val telefono: String,
    val ingresoMensual: Double
)