package com.mathyas.cotizadorbancario.data.models

/**
 * SEALED CLASS para manejar los diferentes ESTADOS de las operaciones
 * 
 * En MVVM, usamos esta clase para comunicar estados desde el ViewModel a la View
 * Es una "sealed class" lo que significa que solo puede tener subclases definidas aquí
 * 
 * Ventajas:
 * - Tipo seguro (el compilador sabe todos los casos posibles)
 * - Fácil de usar con "when" (similar a switch en otros lenguajes)
 * - Permite pasar datos diferentes en cada estado
 * 
 * @param T Tipo genérico para el dato de éxito
 */
sealed class Result<out T> {
    /**
     * Estado LOADING - La operación está en progreso
     * Úsalo para mostrar un ProgressBar o indicador de carga
     */
    object Loading : Result<Nothing>()
    
    /**
     * Estado SUCCESS - La operación fue exitosa
     * Contiene los datos resultantes
     * 
     * @param data Los datos obtenidos de la operación
     */
    data class Success<T>(val data: T) : Result<T>()
    
    /**
     * Estado ERROR - La operación falló
     * Contiene el mensaje de error para mostrar al usuario
     * 
     * @param message Mensaje descriptivo del error
     */
    data class Error(val message: String) : Result<Nothing>()
}

/**
 * EJEMPLO DE USO en un ViewModel:
 * 
 * when (resultado) {
 *     is Result.Loading -> {
 *         // Mostrar loading
 *         mostrarCargando(true)
 *     }
 *     is Result.Success -> {
 *         // Operación exitosa, usar resultado.data
 *         val usuario = resultado.data
 *         mostrarUsuario(usuario)
 *     }
 *     is Result.Error -> {
 *         // Mostrar error al usuario
 *         mostrarError(resultado.message)
 *     }
 * }
 */
