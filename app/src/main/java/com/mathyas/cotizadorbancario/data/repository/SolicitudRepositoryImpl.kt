package com.mathyas.cotizadorbancario.data.repository

import com.mathyas.cotizadorbancario.data.api.SolicitudPrestamoApiService
import com.mathyas.cotizadorbancario.data.models.SolicitudPrestamoRequest
import com.mathyas.cotizadorbancario.data.models.SolicitudesPrestamoResponse
import com.mathyas.cotizadorbancario.domain.repository.SolicitudPrestamoRepository

class SolicitudRepositoryImpl (private val solicitudPrestamoApiService: SolicitudPrestamoApiService) :
    SolicitudPrestamoRepository {
        override suspend fun simular(solicitudPrestamoRequest: SolicitudPrestamoRequest): SolicitudesPrestamoResponse? {
            return try {
                val response = solicitudPrestamoApiService.simular(solicitudPrestamoRequest)
                if (response.isSuccessful) {
                    response.body() ?: return null
                } else {
                    throw Exception("Error al simular solicitud de préstamo: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                throw e
            }
        }
        override suspend fun register(solicitudPrestamoRequest: SolicitudPrestamoRequest): SolicitudesPrestamoResponse? {
            return try {
                val response = solicitudPrestamoApiService.create(solicitudPrestamoRequest)
                if (response.isSuccessful) {
                    response.body() ?: return null
                } else {
                    throw Exception("Error al registrar solicitud de préstamo: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }
