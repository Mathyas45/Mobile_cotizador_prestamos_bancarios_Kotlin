package com.mathyas.cotizadorbancario.presentation.clientes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
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
import com.mathyas.cotizadorbancario.data.repository.ClienteRepositoryImpl
import com.mathyas.cotizadorbancario.presentation.cotizacion.CotizacionActivity
import com.mathyas.cotizadorbancario.presentation.theme.UserAppTheme
import com.mathyas.cotizadorbancario.data.models.Result


class ClientesActivity : ComponentActivity() {
    private val viewModel by lazy {
        val repository = ClienteRepositoryImpl(RetrofitClient.clienteService)
        ClienteViewModel(repository)
    }

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserAppTheme {
                ClientesScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ClientesScreen(viewModel: ClienteViewModel) {

    // Estado para mostrar/ocultar el modal de confirmación
    val showModal = remember { mutableStateOf(false) }
    if(showModal.value){
        showPrivacyPolicyModal(showModal)
    }

    var documentoIdentidad by remember { mutableStateOf("") }
    var nombreCompleto by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    // Observar el estado del registro
    val registerClienteState by viewModel.registerClienteState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Surface(// Superficie de fondo esta parte define una superficie de fondo para la pantalla de registro en Jetpack Compose. La superficie ocupa todo el tamaño disponible y utiliza el color de fondo definido en el tema de Material Design 3.
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // Column con scroll para formularios largos
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Título
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Simula tu Crédito",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
                )
                Text(
                    text = "Hipotecario",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Ingresa los siguientes datos para poder conocerte mejor",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Agrega sombra
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)) // Agrega borde
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), // Agrega un padding interno para que el contenido no esté pegado a los bordes
                    horizontalAlignment = Alignment.CenterHorizontally // Centra el contenido dentro del Card
                ) {

                // Campo de Documento de Identidad
                OutlinedTextField(
                    value = documentoIdentidad,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { documentoIdentidad = it },
                    label = { Text("Documento de Identidad") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp) )
                // Campo de Nombre
                OutlinedTextField(
                    value = nombreCompleto,
                    onValueChange = { nombreCompleto = it },
                    label = { Text("Nombres") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp) )
                // Campo de Teléfono
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("celular") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = buildAnnotatedString {
                            append("Declaro conocer que esta información será utilizada únicamente para los fines de este proceso, y será tratada según lo indicado en nuestra ")
                            pushStringAnnotation(tag = "POLITICA", annotation = "POLITICA")
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                append("Política de Privacidad")
                            }
                            pop()
                            append(".")
                        },
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.clickable {
                            showModal.value = true
                        }
                    )


                Spacer(modifier = Modifier.height(16.dp))
                // Botón de Registro
                Button(
                    onClick = {
                        viewModel.create(
                            documentoIdentidad = documentoIdentidad,
                            nombreCompleto = nombreCompleto,
                            telefono = telefono,
                            onSuccess = { clienteId , nombreCompleto->
                                // Navegar a CotizacionActivity pasando el ID del cliente
                                val intent = Intent(context, CotizacionActivity::class.java).apply {
                                    putExtra("CLIENTE_ID", clienteId)
                                    putExtra("NOMBRE", nombreCompleto)
                                }
                                context.startActivity(intent)
                            }
                        )
                    },
                    enabled = registerClienteState !is Result.Loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    if (registerClienteState is Result.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }else{
                        Text(text = "Continuar")
                    }
                }
                }
            }
            // Mostrar error si existe
            if (registerClienteState is Result.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = (registerClienteState as Result.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

//modal
@Composable
fun showPrivacyPolicyModal(showModal: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = { showModal.value = false },
        title = { Text(text = "Política de Privacidad") },
        text = { Text(text =
                "Fecha de entrada en vigor: 11 de Diciembre de 2025\n" +
                "1. Información Recopilada:\n" +
                "Recopilamos los datos que usted ingresa en el cotizador (ej. monto, plazo, edad) con el único fin de realizar la simulación hipotecaria solicitada y mejorar la precisión de nuestra herramienta.\n" +
                "2. Uso de Datos:\n" +
                "Utilizamos su información exclusivamente para mostrarle resultados de cotización. No recopilamos datos personales identificables (como su nombre completo o DNI) a menos que usted decida proporcionarlos voluntariamente para recibir seguimiento.\n" +
                "3. Confidencialidad:\n" +
                "Su información de simulación es confidencial. No vendemos, alquilamos ni compartimos sus datos con terceros, salvo obligación legal.\n" +
                "4. Aceptación:\n" +
                "Al utilizar este cotizador, usted acepta el manejo de la información descrita en estas políticas.") },
        confirmButton = {
            Button(
                onClick = { showModal.value = false }
            ) {
                Text("Aceptar")
            }
        }
    )
}


