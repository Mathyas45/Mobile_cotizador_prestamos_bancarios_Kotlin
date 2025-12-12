package com.mathyas.cotizadorbancario.presentation.cotizacion

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mathyas.cotizadorbancario.data.api.RetrofitClient
import com.mathyas.cotizadorbancario.data.models.Result
import com.mathyas.cotizadorbancario.data.repository.SolicitudRepositoryImpl
import com.mathyas.cotizadorbancario.presentation.theme.UserAppTheme
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

/**
 * COTIZACION ACTIVITY
 * 
 * Pantalla donde el cliente puede realizar cotizaciones de crédito hipotecario.
 * Recibe el ID del cliente desde ClientesActivity.
 */
class CotizacionActivity : ComponentActivity() {
    
    // Crear el ViewModel con su repositorio
    private val viewModel by lazy {
        val repository = SolicitudRepositoryImpl(RetrofitClient.SolicitudPrestamoService)
        CotizacionViewModel(repository)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Obtener el ID del cliente que viene desde ClientesActivity
        val clienteId = intent.getLongExtra("CLIENTE_ID", -1L)
        val nombre = intent.getStringExtra("NOMBRE")
        
        setContent {
            UserAppTheme {
                CotizacionScreen(
                    clienteId = clienteId, 
                    nombre = nombre,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun CotizacionScreen(clienteId: Long, nombre: String?, viewModel: CotizacionViewModel) {
    // Estados para los campos del formulario
    var costoInmueble by remember { mutableStateOf("") }
    var porcentajeCuotaInicial by remember { mutableFloatStateOf(10f) } // Slider: 10% - 90%
    var plazoAnios by remember { mutableFloatStateOf(4f) } // Slider: 4 - 25 años
    
    // Observar el estado de la cotización
    val simularState by viewModel.simularCotizacionState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    // Calcular la cuota inicial en tiempo real
    // Solo se calcula si el costo del inmueble es válido
    val montoCuotaInicial = remember(costoInmueble, porcentajeCuotaInicial) {
        val costo = costoInmueble.toDoubleOrNull() ?: 0.0
        costo * (porcentajeCuotaInicial / 100.0)
    }
    
    // Formato de moneda para mostrar el monto
    val formatoMoneda = remember { 
        NumberFormat.getCurrencyInstance(Locale("es", "PE")) 
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = buildAnnotatedString {
                    append("Ahora ")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                        append(nombre ?: "tu nombre")
                    }
                    append(" cuéntanos un poco sobre el préstamo que deseas")
                },
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // ========== COSTO DEL INMUEBLE ==========
                    Text(
                        text = "¿Cuál es el costo del inmueble?",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = costoInmueble,
                        onValueChange = { costoInmueble = it },
                        label = { Text("Costo del Inmueble S/") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // ========== CUOTA INICIAL (SLIDER) ==========
                    Text(
                        text = "Cuota inicial",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Mostrar el porcentaje seleccionado
                    Text(
                        text = "${porcentajeCuotaInicial.toInt()}%",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    // Slider de porcentaje (10% - 90%)
                    Slider(
                        value = porcentajeCuotaInicial,
                        onValueChange = { porcentajeCuotaInicial = it },
                        valueRange = 10f..90f,
                        steps = 79, // Para que vaya de 1 en 1
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Etiquetas min/max del slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "10%", style = MaterialTheme.typography.bodySmall)
                        Text(text = "90%", style = MaterialTheme.typography.bodySmall)
                    }
                    
                    // ========== MOSTRAR CUOTA INICIAL CALCULADA ==========
                    // Solo se muestra si hay un costo válido
                    if (costoInmueble.isNotBlank() && costoInmueble.toDoubleOrNull() != null && costoInmueble.toDouble() > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Tu cuota inicial sería de:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    text = formatoMoneda.format(montoCuotaInicial),
                                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // ========== PLAZO EN AÑOS (SLIDER) ==========
                    Text(
                        text = "Plazo del crédito (en años)",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Mostrar los años seleccionados
                    Text(
                        text = "${plazoAnios.toInt()} años",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    // Slider de años (4 - 25)
                    Slider(
                        value = plazoAnios,
                        onValueChange = { plazoAnios = it },
                        valueRange = 4f..25f,
                        steps = 20, // Para que vaya de 1 en 1
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Etiquetas min/max del slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "4 años", style = MaterialTheme.typography.bodySmall)
                        Text(text = "25 años", style = MaterialTheme.typography.bodySmall)
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // ========== BOTÓN COTIZAR ==========
                    Button(
                        onClick = {
                            val monto = costoInmueble.toDoubleOrNull() ?: 0.0
                            viewModel.simular(
                                monto = BigDecimal.valueOf(monto),
                                plazoAnios = plazoAnios.toInt(),
                                porcentajeCuotaInicial = BigDecimal.valueOf(porcentajeCuotaInicial.toDouble()),
                                clienteId = clienteId,
                                onSuccess = { response ->
                                    // Navegar a ResultadoCotizacionActivity con los datos
                                    val intent = Intent(context, ResultadoCotizacionActivity::class.java).apply {
                                        putExtra("MONTO" ,response.monto.toDouble())
                                        putExtra("CUOTA_MENSUAL", response.cuotaMensual.toDouble())
                                        putExtra("MONTO_FINANCIAR", response.montoFinanciar.toDouble())
                                        putExtra("TASA_INTERES", response.tasaInteres.toDouble())
                                        putExtra("TCEA", response.tcea.toDouble())
                                        putExtra("PLAZO_ANIOS", response.plazoAnios)
                                        putExtra("MONTO_CUOTA_INICIAL", response.montoCuotaInicial.toDouble())
                                        putExtra("CLIENTE_ID", clienteId)
                                        putExtra("PORCENTAJE_CUOTA_INICIAL", porcentajeCuotaInicial.toDouble())
                                        putExtra("ESTADO" ,response.estado)
                                        putExtra("MOTIVO_RECHAZO" ,response.motivoRechazo)
                                    }
                                    context.startActivity(intent)
                                }
                            )
                        },
                        enabled = simularState !is Result.Loading && costoInmueble.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        if (simularState is Result.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(text = "Cotizar")
                        }
                    }
                    
                    // Mostrar error si existe
                    if (simularState is Result.Error) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = (simularState as Result.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}