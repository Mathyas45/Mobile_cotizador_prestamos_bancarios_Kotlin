package com.mathyas.cotizadorbancario.domain.repository

import com.mathyas.cotizadorbancario.data.models.ClienteRequest
import com.mathyas.cotizadorbancario.data.models.ClienteResponse

interface ClienteRepository {
    suspend fun create(clienteRequest: ClienteRequest): ClienteResponse?
}