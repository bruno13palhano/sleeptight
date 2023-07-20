package com.bruno13palhano.sleeptight.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Blue,
    secondary = MiddleBlue,
    tertiary =  Black,
    background = Gray,
    surface = Gray,
    secondaryContainer = MiddleBlue,
    primaryContainer = Blue,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
)

private val LightColorScheme = lightColorScheme(
    primary = MiddleBlue,
    secondary = Blue,
    tertiary = White,
    background = LightGray,
    surface = LightGray,
    secondaryContainer = LightBlue,
    primaryContainer = MiddleBlue,
    onPrimary = Black,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun SleepTightTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}