package com.mathyas.cotizadorbancario.presentation.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mathyas.cotizadorbancario.R
import com.mathyas.cotizadorbancario.presentation.clientes.ClientesActivity
import com.mathyas.cotizadorbancario.presentation.theme.UserAppTheme
import kotlinx.coroutines.delay

/**
 * SPLASH ACTIVITY
 * 
 * Pantalla de inicio que muestra el logo de la aplicación con una animación elegante
 * Después de unos segundos, navega automáticamente a ClientesActivity
 */
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserAppTheme {
                SplashScreen {
                    // Navegar a ClientesActivity cuando termine la animación
                    startActivity(Intent(this@SplashActivity, ClientesActivity::class.java))
                    finish() // Cerrar SplashActivity para que no se pueda volver atrás
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    // Animación de escala (el logo crece suavemente)
    val scale = remember { Animatable(0.5f) }
    // Animación de opacidad (el logo aparece gradualmente)
    val alpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) } // Animación para el texto

    // Colores para el gradiente de fondo
    val primaryColor = MaterialTheme.colorScheme.primary
    val backgroundColor = MaterialTheme.colorScheme.background
    val secondaryColor = MaterialTheme.colorScheme.secondary

    // Efecto que se ejecuta cuando se compone la pantalla
    LaunchedEffect(key1 = true) {
        // Animación de escala: de 0.5 a 1.1 en 800ms
        scale.animateTo(
            targetValue = 1.1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            )
        )
    }

    LaunchedEffect(key1 = true) {
        // Animación de opacidad: de 0 a 1 en 600ms
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 600,
                easing = FastOutSlowInEasing
            )
        )

        // Animación de opacidad para el texto
        textAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            )
        )

        // Esperar 2 segundos después de que termine la animación
        delay(2000L)

        // Llamar al callback para navegar a la siguiente pantalla
        onSplashFinished()
    }

    // UI del Splash
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = 0.1f),
                        backgroundColor,
                        primaryColor.copy(alpha = 0.05f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Logo con animaciones
            Image(
                painter = painterResource(id = R.drawable.logo1),
                contentDescription = "Logo de la aplicación",
                modifier = Modifier
                    .size(220.dp) // Incremento del tamaño del logo
                    .scale(scale.value)
                    .alpha(alpha.value)
            )

            // Espaciado para mover el texto más abajo
            Spacer(modifier = Modifier.size(40.dp))

            // Texto debajo del logo
            Text(
                text = "Rápido y sencillo",
                color = secondaryColor,
                style = MaterialTheme.typography.headlineMedium, // Texto más grande
                modifier = Modifier.alpha(textAlpha.value)
            )
        }
    }
}
