package com.mathyas.cotizadorbancario.domain.repository

import com.mathyas.cotizadorbancario.data.models.SolicitudPrestamoRequest
import com.mathyas.cotizadorbancario.data.models.SolicitudesPrestamoResponse

interface SolicitudPrestamoRepository {//esto nos sirve para definir las funciones que vamos a usar en el repositorio, solo definimos las funciones sin implementarlas
    suspend fun simular (solicitudPrestamoRequest: SolicitudPrestamoRequest): SolicitudesPrestamoResponse?
    suspend fun register (solicitudPrestamoRequest: SolicitudPrestamoRequest): SolicitudesPrestamoResponse?
}