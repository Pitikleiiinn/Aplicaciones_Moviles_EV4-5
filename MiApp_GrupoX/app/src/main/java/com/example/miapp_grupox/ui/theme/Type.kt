package com.example.miapp_grupox.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AppTypography = Typography(

    headlineLarge = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
    ),

    headlineMedium = TextStyle(
        fontSize = 23.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
    ),

    titleLarge = TextStyle(
        fontSize = 19.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
    ),

    titleMedium = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary
    ),

    bodyLarge = TextStyle(
        fontSize = 15.sp,
        color = TextPrimary
    ),

    bodyMedium = TextStyle(
        fontSize = 13.sp,
        color = TextSecondary
    )
)