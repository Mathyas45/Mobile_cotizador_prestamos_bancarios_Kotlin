package com.mathyas.cotizadorbancario.data.utils

import org.json.JSONObject

/**
 * Utilidad para parsear errores de la API
 * Extrae el mensaje de error del response del backend
 */
object ErrorParser {
    /**
     * Extrae el mensaje de error de la respuesta HTTP
     * 
     * @param response Response fallido de Retrofit
     * @return Mensaje de error del backend o mensaje por defecto
     */
    fun parseError(response: retrofit2.Response<*>): String {
        return try {
            val errorBody = response.errorBody()?.string() ?: ""
            val jsonObject = JSONObject(errorBody)
            jsonObject.getString("message")
        } catch (e: Exception) {
            "Error: ${response.code()} ${response.message()}"
        }
    }
}
