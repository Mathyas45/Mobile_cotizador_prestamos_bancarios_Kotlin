package com.mathyas.cotizadorbancario.presentation.clientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathyas.cotizadorbancario.data.models.ClienteRequest
import com.mathyas.cotizadorbancario.data.models.ClienteResponse
import com.mathyas.cotizadorbancario.data.models.Result
import com.mathyas.cotizadorbancario.domain.repository.ClienteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClienteViewModel (
    private val repository: ClienteRepository):ViewModel(){
        private val _registerClienteState = MutableStateFlow<Result<ClienteResponse>?>(null)//estado mutable que puede cambiar, nos sirve para manejar el estado de registro del cliente
        val registerClienteState: StateFlow<Result<ClienteResponse>?> = _registerClienteState//estado inmutable que expone el estado de registro del cliente. nos sirve para que la UI observe los cambios

    /**
     * Crea un nuevo cliente en el backend
     * @param onSuccess callback que se ejecuta cuando el registro es exitoso, recibe el ID del cliente
     */
    fun create(
        documentoIdentidad : String,
        nombreCompleto: String,
        telefono: String,
        onSuccess: (Long, String) -> Unit = { _, _ -> } // Callback que recibe el ID del cliente y el nombre
    ){
        if(!validarCampos(nombreCompleto, telefono, documentoIdentidad)){
            return
        }
        // Ejecutar el registro en una coroutine
            viewModelScope.launch {
                _registerClienteState.value = Result.Loading
                try {
                    val clienteRequest = ClienteRequest(
                        documentoIdentidad = documentoIdentidad.trim(),
                        nombreCompleto = nombreCompleto.trim(),
                        telefono = telefono.trim()
                    )
                    val response = repository.create(clienteRequest)
                    if (response != null) {
                        _registerClienteState.value = Result.Success(response)
                        // Llamar al callback con el ID del cliente
                        onSuccess(response.data.id, response.data.nombreCompleto)//estamos pasando el id y nombre del cliente registrado hacia el callback onSuccess
                    } else {
                        _registerClienteState.value = Result.Error("Error al registrar el cliente")
                    }
                }catch (e: Exception) {
                    _registerClienteState.value = Result.Error(e.message ?: "Error al registrar el cliente")
            }
        }
    }

    private fun validarCampos(
        nombreCompleto: String,
        telefono: String,
        documentoIdentidad : String
    ): Boolean {
        if (nombreCompleto.isBlank()) {
            _registerClienteState.value = Result.Error("El nombre no puede estar vacío")
            return false
        }
        if (telefono.isBlank()) {
            _registerClienteState.value = Result.Error("El teléfono no puede estar vacío")
            return false
        }
        if (documentoIdentidad.isBlank()) {
            _registerClienteState.value = Result.Error("El documento de identidad no puede estar vacío")
            return false
        }
        if(telefono.length < 9)
        {
            _registerClienteState.value = Result.Error("El teléfono debe tener al menos 9 dígitos")
            return false
        }
        if(documentoIdentidad.length != 8){
            _registerClienteState.value = Result.Error("El documento de identidad debe tener 8 dígitos")
            return false
        }
        return true
    }
}