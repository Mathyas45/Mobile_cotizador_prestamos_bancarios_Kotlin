package com.mathyas.cotizadorbancario.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * OBJETO SINGLETON para crear y configurar Retrofit
 * 
 * En Kotlin, "object" crea un singleton (una única instancia global)
 * Este objeto se encarga de:
 * 1. Configurar OkHttp (cliente HTTP)
 * 2. Configurar Retrofit (convierte la interface en implementación real)
 * 3. Proveer la instancia de UserApiService
 */
object RetrofitClient {

    // URL base de la API
    // 10.0.2.2 es la IP especial del emulador Android para acceder a localhost de tu PC
    // IMPORTANTE: En el emulador, "localhost" se refiere al propio emulador, NO a tu PC
    // Por eso usamos 10.0.2.2 que es la IP especial para acceder al host (tu PC)
    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    /**
     * Configuración de OkHttpClient
     * 
     * OkHttpClient es el cliente HTTP real que hace las peticiones
     * Aquí configuramos:
     * - Timeouts (tiempo máximo de espera)
     * - Interceptores (logging para ver las peticiones en el log)
     */
    private val okHttpClient: OkHttpClient by lazy {
        // "by lazy" = se crea solo cuando se usa por primera vez
        
        // Interceptor de logging para ver las peticiones HTTP en el Logcat
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // BODY = muestra todo (URL, headers, body de request y response)
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Construir el cliente HTTP
        OkHttpClient.Builder()
            // Tiempo máximo para establecer conexión (30 segundos)
            .connectTimeout(30, TimeUnit.SECONDS)
            
            // Tiempo máximo para leer la respuesta (30 segundos)
            .readTimeout(30, TimeUnit.SECONDS)
            
            // Tiempo máximo para escribir la petición (30 segundos)
            .writeTimeout(30, TimeUnit.SECONDS)
            
            // Agregar el interceptor de logging
            .addInterceptor(loggingInterceptor)
            
            // Construir el cliente
            .build()
    }

    /**
     * Instancia de Retrofit configurada
     * 
     * Retrofit convierte la interface UserApiService en una implementación real
     * que hace las peticiones HTTP
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            // URL base para todas las peticiones
            .baseUrl(BASE_URL)
            
            // Cliente HTTP a usar (nuestro OkHttpClient configurado)
            .client(okHttpClient)
            
            // Convertidor para transformar JSON a objetos Kotlin y viceversa
            .addConverterFactory(GsonConverterFactory.create())
            
            // Construir Retrofit
            .build()
    }

    /**
     * Instancia de la API
     * 
     * Esta es la implementación real de UserApiService
     * Retrofit genera automáticamente el código basándose en la interface
     * 
     * USO:
     * val response = RetrofitClient.apiService.login(loginRequest)
     */
    val clienteService: ClienteApiService by lazy {
        // create() genera la implementación de la interface
        retrofit.create(ClienteApiService::class.java)
    }
    val SolicitudPrestamoService: SolicitudPrestamoApiService by lazy {
        retrofit.create(SolicitudPrestamoApiService::class.java)
    }
}

/**
 * CÓMO FUNCIONA:
 * 
 * 1. Defines una interface (UserApiService) con anotaciones
 * 2. Retrofit.create() genera automáticamente la implementación
 * 3. Cuando llamas a apiService.login(), Retrofit:
 *    - Construye la URL completa (BASE_URL + "users/login")
 *    - Serializa el objeto LoginRequest a JSON
 *    - Hace la petición HTTP POST
 *    - Deserializa la respuesta JSON a LoginResponse
 *    - Devuelve Response<LoginResponse>
 * 
 * TODO ESTO ES AUTOMÁTICO - solo defines la interface y Retrofit hace el resto
 */
