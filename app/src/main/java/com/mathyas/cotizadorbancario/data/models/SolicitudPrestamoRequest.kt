package com.mathyas.cotizadorbancario.data.models

import java.math.BigDecimal

data class SolicitudPrestamoRequest (
    val monto: BigDecimal = BigDecimal.ZERO,
    val plazoAnios: Int = 0,
    val porcentajeCuotaInicial : BigDecimal = BigDecimal.ZERO,
    val clienteId : Long = 0,
)