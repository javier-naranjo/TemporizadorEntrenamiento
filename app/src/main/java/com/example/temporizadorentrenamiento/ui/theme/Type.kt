package com.example.temporizadorentrenamiento.ui.theme

import android.text.Layout.Alignment
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.temporizadorentrenamiento.R

val Michigan = FontFamily(
    Font(R.font.michigan_3)
)

////////////////////////////////////////////////////////////////
@OptIn(ExperimentalTextApi::class)
val Typography = Typography(
    bodyMedium = TextStyle(
        fontFamily = Michigan,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center
    ),
    bodySmall = TextStyle(
        fontFamily = Michigan,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center
    ),
    titleLarge = TextStyle(
        fontFamily = Michigan,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp,
        lineHeight = 42.sp,
        letterSpacing = 0.sp,
        drawStyle = Stroke(
            miter = 10f,
            width = 5f,
            join = StrokeJoin.Round
        )
    ),
    titleMedium = TextStyle(
        fontFamily = Michigan,
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    )
)