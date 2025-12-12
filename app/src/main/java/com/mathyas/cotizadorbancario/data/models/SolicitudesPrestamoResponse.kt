package com.mathyas.cotizadorbancario.data.models

import java.math.BigDecimal

data class SolicitudesPrestamoResponse (
    val id: Long,
    val monto: BigDecimal,
    val montoCuotaInicial: BigDecimal,
    val porcentajeCuotaInicial: BigDecimal,
    val montoFinanciar: BigDecimal,
    val plazoAnios: Int,
    val tasaInteres: BigDecimal,
    val tcea: BigDecimal,
    val cuotaMensual: BigDecimal,
    val motivoRechazo: String?,  // Nullable porque puede venir null
    val riesgoCliente: Int,
    val estado: Int,
    val createdAt: String,       // String en lugar de LocalDateTime (Gson no lo parsea automáticamente)
    val cliente: Cliente?        // Nullable por si no viene
)