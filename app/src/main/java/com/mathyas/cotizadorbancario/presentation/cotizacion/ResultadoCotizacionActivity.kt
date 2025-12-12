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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mathyas.cotizadorbancario.data.api.RetrofitClient
import com.mathyas.cotizadorbancario.data.models.Result
import com.mathyas.cotizadorbancario.data.repository.SolicitudRepositoryImpl
import com.mathyas.cotizadorbancario.presentation.clientes.ClientesActivity
import com.mathyas.cotizadorbancario.presentation.theme.UserAppTheme
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

/**
 * RESULTADO COTIZACION ACTIVITY
 * 
 * Pantalla que muestra el resultado de la simulación de crédito hipotecario.
 * Recibe los datos calculados desde CotizacionActivity.
 */
class ResultadoCotizacionActivity : ComponentActivity() {
    // Crear el ViewModel con su repositorio
    private val viewModel by lazy {
        val repository = SolicitudRepositoryImpl(RetrofitClient.SolicitudPrestamoService)
        CotizacionViewModel(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Obtener los datos de la cotización
        val monto = intent.getDoubleExtra("MONTO", 0.0)
        val cuotaMensual = intent.getDoubleExtra("CUOTA_MENSUAL", 0.0)
        val montoFinanciar = intent.getDoubleExtra("MONTO_FINANCIAR", 0.0)
        val tasaInteres = intent.getDoubleExtra("TASA_INTERES", 0.0)
        val tcea = intent.getDoubleExtra("TCEA", 0.0)
        val plazoAnios = intent.getIntExtra("PLAZO_ANIOS", 0)
        val montoCuotaInicial = intent.getDoubleExtra("MONTO_CUOTA_INICIAL", 0.0)
        val clienteId = intent.getLongExtra("CLIENTE_ID", -1L)
        val porcentajeCuotaInicial = intent.getDoubleExtra("PORCENTAJE_CUOTA_INICIAL", 0.0)
        val estado = intent.getIntExtra("ESTADO", 0)
        val motivoRechazo = intent.getStringExtra("MOTIVO_RECHAZO") ?: ""
        
        setContent {
            UserAppTheme {
                ResultadoCotizacionScreen(//esto es para pasarle los parametros a la pantalla
                    monto = monto,
                    cuotaMensual = cuotaMensual,
                    montoFinanciar = montoFinanciar,
                    tasaInteres = tasaInteres,
                    tcea = tcea,
                    plazoAnios = plazoAnios,
                    montoCuotaInicial = montoCuotaInicial,
                    clienteId = clienteId,
                    porcentajeCuotaInicial = porcentajeCuotaInicial,
                    estado = estado,
                    motivoRechazo = motivoRechazo,
                    viewModel = viewModel,
                    onVolverClick = { finish() }
                )
            }
        }
    }
}

@Composable
fun ResultadoCotizacionScreen(
    monto : Double,
    cuotaMensual: Double,
    montoFinanciar: Double,
    tasaInteres: Double,
    tcea: Double,
    plazoAnios: Int,
    montoCuotaInicial: Double,
    clienteId: Long,
    porcentajeCuotaInicial: Double,
    viewModel: CotizacionViewModel,
    estado: Int,
    motivoRechazo: String,
    onVolverClick: () -> Unit
) {
    // Formato de moneda para mostrar los montos
    val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "PE"))
    val context = LocalContext.current
    
    // Estado para mostrar/ocultar el modal de confirmación
    var showSuccessDialog by remember { mutableStateOf(false) }
    
    // Observar el estado del registro (usa createCotizacionState, no simularCotizacionState)
    val createState by viewModel.createCotizacionState.collectAsStateWithLifecycle()
    
    // Modal de confirmación cuando el registro es exitoso
    if (showSuccessDialog) {
        SuccessDialog(// Mostrar el diálogo de éxito
            onDismiss = {// Acción al cerrar el diálogo
                showSuccessDialog = false
                // Navegar a ClientesActivity y limpiar el stack de navegación
                val intent = Intent(context, ClientesActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
            }
        )
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
            if(estado != 1) {
                Text(
                    text = "Lo sentimos, tu cotización no fue aprobada.",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))


                Spacer(modifier = Modifier.height(24.dp))
            }else{
                Text(
                    text = "¡Tu cotización está lista!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Card principal con la cuota mensual destacada
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                if(estado != 1) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Después de analizar tu solicitud, hemos decidido no aprobar tu cotización en esta ocasión.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }else{
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Tu cuota mensual sería de:",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = formatoMoneda.format(cuotaMensual),
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Card con el detalle de la cotización
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
            ) {
                if(estado != 1) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Mostrar solo el motivo de rechazo
                        Text(
                            text = "Motivo de rechazo:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = motivoRechazo,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }else{
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Detalle de tu crédito",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(16.dp))

                        // Monto a financiar
                        DetalleItem(
                            label = "Monto a financiar",
                            valor = formatoMoneda.format(montoFinanciar)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Cuota inicial
                        DetalleItem(
                            label = "Cuota inicial",
                            valor = formatoMoneda.format(montoCuotaInicial)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Plazo
                        DetalleItem(
                            label = "Plazo",
                            valor = "$plazoAnios años"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Tasa de interés
                        DetalleItem(
                            label = "Tasa de interés anual",
                            valor = String.format("%.2f%%", tasaInteres)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // TCEA
                        DetalleItem(
                            label = "TCEA",
                            valor = String.format("%.2f%%", tcea)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        if( estado == 1){
                        // BOTÓN PARA ACEPTAR COTIZACIÓN Y GUARDAR
                            Button(
                                onClick = {
                                    // Calcular el monto total (financiar + cuota inicial)
                                    viewModel.create(
                                        monto = BigDecimal.valueOf(monto),
                                        plazoAnios = plazoAnios,
                                        porcentajeCuotaInicial = BigDecimal.valueOf(porcentajeCuotaInicial),
                                        clienteId = clienteId,
                                        onSuccess = {
                                            // Mostrar el modal de éxito
                                            showSuccessDialog = true
                                        }
                                    )
                                },
                                enabled = createState !is Result.Loading,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                if (createState is Result.Loading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Text(text = "Aceptar Cotización")
                                }
                            }
                        }
                        // Mostrar error si existe
                        if (createState is Result.Error) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = (createState as Result.Error).message,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if(estado != 1){
                Spacer(modifier = Modifier.height(30.dp))
                //volver a la pestaña de clientes
                Button(
                    onClick = {
                        // Navegar a ClientesActivity y limpiar el stack de navegación
                        val intent = Intent(context, ClientesActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Ir a Clientes",
                            tint = MaterialTheme.colorScheme.onTertiary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Volver",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }else{
            // Botón para volver a simular
                Button(
                    onClick = onVolverClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Volver a simular",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Modal de confirmación cuando la cotización se registra exitosamente
 */
@Composable
fun SuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { /* No permitir cerrar tocando fuera */ },
        icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Éxito",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                text = "¡Cotización Registrada!",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Tu cotización ha sido registrada exitosamente. Un asesor se comunicará contigo pronto para continuar con el proceso.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aceptar")
            }
        }
    )
}

/**
 * Componente reutilizable para mostrar un item del detalle
 */
@Composable
fun DetalleItem(label: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )
    }
}
