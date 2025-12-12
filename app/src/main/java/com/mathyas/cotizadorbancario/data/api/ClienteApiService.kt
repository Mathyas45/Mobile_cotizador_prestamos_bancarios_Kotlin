package com.mathyas.cotizadorbancario.data.api

import com.mathyas.cotizadorbancario.data.models.ClienteRequest
import com.mathyas.cotizadorbancario.data.models.ClienteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ClienteApiService {
    @POST("clientes/register")
    suspend fun create(
        @Body clienteRequest: ClienteRequest
    ): Response<ClienteResponse>

}