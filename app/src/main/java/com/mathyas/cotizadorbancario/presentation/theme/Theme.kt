package com.mathyas.cotizadorbancario.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * TEMA DE LA APP con Material Design 3
 *
 * Define los colores para modo claro y oscuro
 */

// Colores personalizados (puedes cambiarlos)
private val Purple = Color(0xFF7365FF)
private val PurpleGrey80 = Color(0xFFCCC2DC)
private val Pink80 = Color(0xFFEFB8C8)
private val blanco = Color(0xFFFFFFFF)

private val Purple40 = Color(0xFF6650a4)
private val gray = Color(0xFF4D4D55)
private val Pink40 = Color(0xFF7D5260)

// Esquema de colores para modo claro
private val LightColorScheme = lightColorScheme(
    primary = Purple,
    secondary = gray,
    tertiary = Pink40,
)

// Esquema de colores para modo oscuro
private val DarkColorScheme = darkColorScheme(
    primary = Purple,
    secondary = gray,
    tertiary = Pink80
)

/**
 * TEMA COMPOSABLE
 *
 * Aplica el tema Material 3 a toda la app
 * Puedes personalizar colores, tipografía, formas, etc.
 */
@Composable
fun UserAppTheme(
    darkTheme: Boolean = false, // Cambiar a true para modo oscuro por defecto
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
