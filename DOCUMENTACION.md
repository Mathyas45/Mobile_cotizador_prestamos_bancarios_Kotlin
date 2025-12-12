# 📱 Cotizador Bancario - Documentación del Sistema

## 📋 Descripción General

**Cotizador Bancario** es una aplicación móvil Android desarrollada en Kotlin que permite a los usuarios simular y registrar cotizaciones de créditos hipotecarios. La aplicación se conecta a un backend REST para procesar las solicitudes.

---

## 🏗️ Arquitectura del Sistema

### Patrón de Arquitectura: **MVVM (Model-View-ViewModel)**

La aplicación implementa el patrón **MVVM** (Model-View-ViewModel), que es el patrón recomendado por Google para aplicaciones Android modernas.

```
┌─────────────────────────────────────────────────────────────┐
│                        PRESENTATION                          │
│  ┌─────────────┐    ┌──────────────┐    ┌───────────────┐  │
│  │   Activity  │───▶│  ViewModel   │───▶│   Repository  │  │
│  │  (Compose)  │◀───│  (StateFlow) │◀───│  (Interface)  │  │
│  └─────────────┘    └──────────────┘    └───────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                           DATA                               │
│  ┌──────────────────┐    ┌──────────────┐    ┌───────────┐ │
│  │ RepositoryImpl   │───▶│  ApiService  │───▶│  Backend  │ │
│  │                  │◀───│  (Retrofit)  │◀───│   REST    │ │
│  └──────────────────┘    └──────────────┘    └───────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### Capas de la Arquitectura

| Capa | Descripción | Componentes |
|------|-------------|-------------|
| **Presentation** | UI y lógica de presentación | Activities, Composables, ViewModels |
| **Domain** | Lógica de negocio | Interfaces de Repository |
| **Data** | Acceso a datos | Implementaciones de Repository, API Services, Models |

---

## 📁 Estructura del Proyecto

```
app/src/main/java/com/mathyas/cotizadorbancario/
│
├── 📂 data/                          # Capa de Datos
│   ├── 📂 api/                       # Servicios de red
│   │   ├── ClienteApiService.kt      # Endpoints de clientes
│   │   ├── SolicitudPrestamoApiService.kt  # Endpoints de préstamos
│   │   └── RetrofitClient.kt         # Configuración de Retrofit
│   │
│   ├── 📂 models/                    # Modelos de datos
│   │   ├── ClienteRequest.kt         # Request para crear cliente
│   │   ├── ClienteResponse.kt        # Response del cliente
│   │   ├── SolicitudPrestamoRequest.kt   # Request de cotización
│   │   ├── SolicitudesPrestamoResponse.kt # Response de cotización
│   │   └── Result.kt                 # Sealed class para estados
│   │
│   └── 📂 repository/                # Implementaciones
│       ├── ClienteRepositoryImpl.kt
│       └── SolicitudRepositoryImpl.kt
│
├── 📂 domain/                        # Capa de Dominio
│   └── 📂 repository/                # Interfaces
│       ├── ClienteRepository.kt
│       └── SolicitudPrestamoRepository.kt
│
├── 📂 presentation/                  # Capa de Presentación
│   ├── 📂 splash/                    # Pantalla de inicio
│   │   └── SplashActivity.kt
│   │
│   ├── 📂 clientes/                  # Módulo de clientes
│   │   ├── ClientesActivity.kt       # UI del formulario
│   │   └── ClienteViewModel.kt       # Lógica de presentación
│   │
│   ├── 📂 cotizacion/                # Módulo de cotización
│   │   ├── CotizacionActivity.kt     # UI del simulador
│   │   ├── CotizacionViewModel.kt    # Lógica de simulación
│   │   └── ResultadoCotizacionActivity.kt # Pantalla de resultados
│   │
│   └── 📂 theme/                     # Tema de la app
│       └── Theme.kt                  # Colores y estilos
│
└── MainActivity.kt
```

---

## 🛠️ Tecnologías Utilizadas

### Frontend (Android)

| Tecnología | Versión | Uso |
|------------|---------|-----|
| **Kotlin** | 1.9+ | Lenguaje de programación |
| **Jetpack Compose** | 1.5+ | Framework de UI declarativo |
| **Material Design 3** | - | Sistema de diseño |
| **Retrofit** | 2.9+ | Cliente HTTP para API REST |
| **OkHttp** | 4.11+ | Cliente HTTP con logging |
| **Gson** | - | Serialización JSON |
| **Coroutines** | 1.7+ | Programación asíncrona |
| **StateFlow** | - | Gestión de estado reactivo |

### Backend (Conexión)

| Endpoint | Método | Descripción |
|----------|--------|-------------|
| `/api/clientes/register` | POST | Registrar nuevo cliente |
| `/api/solicitudesPrestamo/simular` | POST | Simular cotización |
| `/api/solicitudesPrestamo/register` | POST | Guardar cotización |

---

## 🔄 Flujo de la Aplicación

```
┌─────────────┐    ┌──────────────┐    ┌─────────────────┐    ┌────────────────┐
│   Splash    │───▶│   Clientes   │───▶│   Cotización    │───▶│   Resultado    │
│   Screen    │    │   (Registro) │    │   (Simulador)   │    │  (Confirmación)│
└─────────────┘    └──────────────┘    └─────────────────┘    └────────────────┘
      │                   │                    │                      │
      │                   │                    │                      │
      ▼                   ▼                    ▼                      ▼
   Logo con          Datos del           Costo inmueble         Cuota mensual
   animación         cliente             Cuota inicial (%)      Detalle crédito
   (2.5 seg)         - DNI               Plazo (años)           Modal éxito
                     - Nombre            Cálculo tiempo real    ────────────
                     - Teléfono          ────────────────       Volver a inicio
                                         Backend: simular
```

---

## 📊 Modelo de Datos

### ClienteRequest
```kotlin
data class ClienteRequest(
    val documentoIdentidad: String,
    val nombre: String,
    val telefono: String
)
```

### SolicitudPrestamoRequest
```kotlin
data class SolicitudPrestamoRequest(
    val monto: BigDecimal,           // Monto total del inmueble
    val plazoAnios: Int,             // Años de financiamiento (4-25)
    val porcentajeCuotaInicial: BigDecimal, // Porcentaje inicial (10-90%)
    val clienteId: Long              // ID del cliente registrado
)
```

### SolicitudesPrestamoResponse
```kotlin
data class SolicitudesPrestamoResponse(
    val monto: BigDecimal,
    val montoCuotaInicial: BigDecimal,
    val porcentajeCuotaInicial: BigDecimal,
    val montoFinanciar: BigDecimal,
    val plazoAnios: Int,
    val tasaInteres: BigDecimal,
    val tcea: BigDecimal,
    val cuotaMensual: BigDecimal,    // Calculado por el backend
    val estado: Int,
    val riesgoCliente: Int
)
```

---

## 🎨 Componentes de UI Destacados

### 1. Splash Screen
- Animación de escala y opacidad
- Logo centrado con gradiente de fondo
- Transición automática después de 2.5 segundos

### 2. Formulario de Cliente
- Validación de campos en tiempo real
- Feedback visual con estados de carga
- Navegación con callback `onSuccess`

### 3. Simulador de Cotización
- **Sliders interactivos** para:
  - Porcentaje de cuota inicial (10% - 90%)
  - Plazo en años (4 - 25 años)
- **Cálculo en tiempo real** de la cuota inicial
- Visualización condicional del monto calculado

### 4. Resultado de Cotización
- Card destacado con cuota mensual
- Detalle completo del crédito
- Modal de confirmación al registrar
- Navegación de retorno limpia

---

## 🔐 Gestión de Estado

### Sealed Class Result
```kotlin
sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}
```

### Patrón de Uso en ViewModel
```kotlin
private val _state = MutableStateFlow<Result<Response>?>(null)
val state: StateFlow<Result<Response>?> = _state

fun action(onSuccess: (Response) -> Unit) {
    viewModelScope.launch {
        _state.value = Result.Loading
        try {
            val response = repository.call()
            _state.value = Result.Success(response)
            onSuccess(response)
        } catch (e: Exception) {
            _state.value = Result.Error(e.message)
        }
    }
}
```

---

## 📡 Configuración de Red

### RetrofitClient (Singleton)
```kotlin
object RetrofitClient {
    // IP especial del emulador para acceder al localhost del PC
    private const val BASE_URL = "http://10.0.2.2:8080/api/"
    
    // Configuración con timeouts y logging
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor())
        .build()
}
```

> ⚠️ **Nota**: En el emulador Android, `localhost` se refiere al propio emulador. Para conectarse al servidor del PC se usa `10.0.2.2`.

---

## 🚀 Características Principales

| Característica | Descripción |
|----------------|-------------|
| ✅ Registro de clientes | Formulario con validación |
| ✅ Simulación en tiempo real | Cálculos automáticos al mover sliders |
| ✅ Cotización de crédito | Conexión con backend para cálculos |
| ✅ Persistencia de datos | Guardado de cotizaciones en BD |
| ✅ UI moderna | Material Design 3 con Compose |
| ✅ Manejo de estados | Loading, Success, Error |
| ✅ Navegación fluida | Paso de datos entre Activities |

---

## 📱 Capturas de Pantalla (Flujo)

1. **Splash** → Logo animado
2. **Clientes** → Formulario de registro
3. **Cotización** → Simulador con sliders
4. **Resultado** → Detalle y confirmación

---

## 👨‍💻 Buenas Prácticas Implementadas

1. **Separación de responsabilidades** - Cada capa tiene su función específica
2. **Inyección de dependencias** - ViewModels reciben sus repositorios
3. **Programación reactiva** - StateFlow para UI reactiva
4. **Código declarativo** - Jetpack Compose para UI
5. **Callbacks para navegación** - Desacoplamiento entre capas
6. **Validación de datos** - En ViewModel antes de enviar al backend
7. **Manejo de errores** - Estados de error visibles al usuario
8. **Singleton para red** - Una única instancia de Retrofit

---

## 📝 Notas Adicionales

- La aplicación requiere conexión a Internet
- El backend debe estar corriendo en `localhost:8080`
- Compatible con Android 7.0 (API 24) en adelante
- Tema claro por defecto, personalizable

---

**Desarrollado con** ❤️ **usando Kotlin y Jetpack Compose**
