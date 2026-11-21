package com.example.moodtune.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Lavender,
    onPrimary = DeepPlum,
    primaryContainer = Lilac,
    onPrimaryContainer = DeepPlum,
    secondary = CloudBlue,
    onSecondary = DeepPlum,
    secondaryContainer = CloudBlue,
    onSecondaryContainer = DeepPlum,
    tertiary = Lilac,
    onTertiary = DeepPlum,
    background = OffWhite,
    onBackground = DeepPlum,
    surface = Color.White,
    onSurface = DeepPlum,
    surfaceVariant = OffWhite,
    onSurfaceVariant = DeepPlum
)

private val DarkColorScheme = darkColorScheme(
    primary = Lavender,
    onPrimary = TextLight,
    primaryContainer = CardDark,
    onPrimaryContainer = TextLight,
    secondary = CloudBlue,
    onSecondary = TextLight,
    secondaryContainer = CardDark,
    onSecondaryContainer = TextLight,
    tertiary = Lilac,
    onTertiary = TextLight,
    background = DeepPlumDark,
    onBackground = TextLight,
    surface = SurfaceDark,
    onSurface = TextLight,
    surfaceVariant = CardDark,
    onSurfaceVariant = TextLight
)

@Composable
fun MoodTuneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}