package com.mathyas.cotizadorbancario.data.api

import com.mathyas.cotizadorbancario.data.models.SolicitudPrestamoRequest
import com.mathyas.cotizadorbancario.data.models.SolicitudesPrestamoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SolicitudPrestamoApiService {
    @POST("solicitudesPrestamo/simular")
    suspend fun simular(
        @Body solicitudesPrestamoRequest: SolicitudPrestamoRequest): Response<SolicitudesPrestamoResponse>

    @POST("solicitudesPrestamo/register")
    suspend fun create(
        @Body solicitudesPrestamoRequest: SolicitudPrestamoRequest): Response<SolicitudesPrestamoResponse>
}