package com.mathyas.cotizadorbancario.data.repository

import com.mathyas.cotizadorbancario.data.api.ClienteApiService
import com.mathyas.cotizadorbancario.data.models.ClienteRequest
import com.mathyas.cotizadorbancario.data.models.ClienteResponse
import com.mathyas.cotizadorbancario.domain.repository.ClienteRepository

class ClienteRepositoryImpl (private val clienteApiService: ClienteApiService ) :
    ClienteRepository {
        override suspend fun create(clienteRequest: ClienteRequest): ClienteResponse?{
            return try {
                val response = clienteApiService.create(clienteRequest)
                if (response.isSuccessful) {
                    response.body() ?: return null
                } else {
                    throw Exception("Error al crear cliente: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                throw e
            }
        }
}


