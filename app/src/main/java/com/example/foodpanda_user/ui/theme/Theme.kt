package com.example.foodpanda_user.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.foodpanda_user.R

val FoodpandaPink = Color(0xFFD60265)
val FoodpandaPinkDark = Color(0xFFC63065)
val FoodpandaBackground = Color(0xFFF6F6F6)
val FoodpandaSurface = Color(0xFFFFFFFF)
val FoodpandaTextPrimary = Color(0xFF1E1E1E)
val FoodpandaTextSecondary = Color(0xFF707070)
val FoodpandaTitleText = Color(0xFF323232)
val FoodpandaBorder = Color(0xFFE5E5E5)
val FoodpandaError = Color(0xFFE74C3C)
val FoodpandaAmber = Color(0xFFFFB300)

val PoppinsFontFamily = FontFamily(
    Font(R.font.poppins, FontWeight.Normal),
    Font(R.font.poppins, FontWeight.Medium),
    Font(R.font.poppins, FontWeight.SemiBold),
    Font(R.font.poppins, FontWeight.Bold)
)

private val LightColorScheme = lightColorScheme(
    primary = FoodpandaPink,
    onPrimary = Color.White,
    primaryContainer = FoodpandaPinkDark,
    onPrimaryContainer = Color.White,
    secondary = FoodpandaTextPrimary,
    onSecondary = Color.White,
    background = FoodpandaBackground,
    onBackground = FoodpandaTextPrimary,
    surface = FoodpandaSurface,
    onSurface = FoodpandaTextPrimary,
    surfaceVariant = Color(0xFFF2F2F2),
    onSurfaceVariant = FoodpandaTextSecondary,
    error = FoodpandaError,
    onError = Color.White
)

val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleLarge = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    titleMedium = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 21.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp
    ),
    labelLarge = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),
    labelSmall = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 15.sp
    )
)

@Composable
fun FoodpandaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        content = content
    )
}
