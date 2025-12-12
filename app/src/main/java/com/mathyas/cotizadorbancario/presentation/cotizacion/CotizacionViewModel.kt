package com.mathyas.cotizadorbancario.presentation.cotizacion
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathyas.cotizadorbancario.data.models.Result
import com.mathyas.cotizadorbancario.data.models.SolicitudPrestamoRequest
import com.mathyas.cotizadorbancario.data.models.SolicitudesPrestamoResponse
import com.mathyas.cotizadorbancario.domain.repository.SolicitudPrestamoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CotizacionViewModel (
    private val repository: SolicitudPrestamoRepository) : ViewModel() {
        private val _simularCotizacionState = MutableStateFlow<Result <SolicitudesPrestamoResponse>?> (null)
        val simularCotizacionState : StateFlow<Result<SolicitudesPrestamoResponse>?> = _simularCotizacionState

        // Estado separado para el registro (create) - evita conflictos con simular
        private val _createCotizacionState = MutableStateFlow<Result<SolicitudesPrestamoResponse>?>(null)
        val createCotizacionState: StateFlow<Result<SolicitudesPrestamoResponse>?> = _createCotizacionState

        /**
         * Simula una cotización de crédito hipotecario
         * @param onSuccess callback que se ejecuta cuando la simulación es exitosa
         */
        fun simular(
            monto: BigDecimal,
            plazoAnios: Int,
            porcentajeCuotaInicial : BigDecimal,
            clienteId : Long,
            onSuccess: (SolicitudesPrestamoResponse) -> Unit = {} // Callback para navegación
        ){
            if(!validarCampos(monto, plazoAnios, porcentajeCuotaInicial)){
                return
            }
            // Ejecutar el registro en una coroutine
            viewModelScope.launch {
                _simularCotizacionState.value = Result.Loading
                try {
                    val solicitudPrestamoRequest = SolicitudPrestamoRequest(
                        monto = monto,
                        plazoAnios = plazoAnios,
                        porcentajeCuotaInicial = porcentajeCuotaInicial,
                        clienteId = clienteId
                    )
                    val response = repository.simular(solicitudPrestamoRequest)
                    if (response != null) {
                        _simularCotizacionState.value = Result.Success(response)
                        // Llamar al callback con la respuesta
                        onSuccess(response)
                    } else {
                        _simularCotizacionState.value = Result.Error("Error al simular la cotización")

                    }
                }catch (e: Exception) {
                    _simularCotizacionState.value = Result.Error(e.message ?: "Error al simular la cotización")

                }
            }
        }
    fun create(
        monto: BigDecimal,
        plazoAnios: Int,
        porcentajeCuotaInicial : BigDecimal,
        clienteId : Long,
        onSuccess: () -> Unit = {} // Callback sin parámetros, solo indica éxito
    ){
        // Ejecutar el registro en una coroutine
        viewModelScope.launch {
            _createCotizacionState.value = Result.Loading
            try {
                val solicitudPrestamoRequest = SolicitudPrestamoRequest(
                    monto = monto,
                    plazoAnios = plazoAnios,
                    porcentajeCuotaInicial = porcentajeCuotaInicial,
                    clienteId = clienteId
                )
                val response = repository.register(solicitudPrestamoRequest)
                if (response != null) {
                    _createCotizacionState.value = Result.Success(response)
                    // Llamar al callback
                    onSuccess()
                } else {
                    _createCotizacionState.value = Result.Error("Error al registrar la cotización")
                }
            }catch (e: Exception) {
                _createCotizacionState.value = Result.Error(e.message ?: "Error al registrar la cotización")
            }
        }
    }

    private fun validarCampos(
        monto: BigDecimal,
        plazoAnios: Int,
        porcentajeCuotaInicial : BigDecimal,
    ): Boolean {
        if (monto <= BigDecimal.ZERO) {
            _simularCotizacionState.value = Result.Error("El monto debe ser mayor a cero")
            return false
        }
        if (plazoAnios <= 0) {
            _simularCotizacionState.value = Result.Error("El plazo en años debe ser mayor a cero")
            return false
        }
        if (porcentajeCuotaInicial < BigDecimal.ZERO || porcentajeCuotaInicial > BigDecimal(100)) {
            _simularCotizacionState.value = Result.Error("El porcentaje de cuota inicial debe estar entre 0 y 100")
            return false
        }
        return true
    }
}