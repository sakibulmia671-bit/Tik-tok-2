package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TikTokColorScheme = darkColorScheme(
    primary = TikTokRed,
    onPrimary = Color.White,
    secondary = TikTokCyan,
    onSecondary = Color.Black,
    tertiary = TikTokYellow,
    background = TikTokBlack,
    onBackground = TikTokTextPrimary,
    surface = TikTokDarkSurface,
    onSurface = TikTokTextPrimary,
    surfaceVariant = TikTokCardDark,
    onSurfaceVariant = TikTokTextSecondary,
    outline = TikTokBorder
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TikTokColorScheme,
        typography = Typography,
        content = content
    )
}
