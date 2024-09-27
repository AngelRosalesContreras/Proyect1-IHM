package com.example.clasificadorobjetos.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.clasificadorobjetos.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fontName = GoogleFont("Pacifico")
val foutFamily = FontFamily(
    Font(
        googleFont = fontName,
        fontProvider = provider
    )
)

val fontName1 = GoogleFont("Sofadi One")
val fontFamily1 = FontFamily(
    Font(
        googleFont = fontName1,
        fontProvider = provider
    )
)

val fontName2 = GoogleFont("Caveat")
val fontFamily2 = FontFamily(
    Font(
        googleFont = fontName2,
        fontProvider = provider
    )
)

val fontName3 = GoogleFont("Permanent Marker")
val fontFamily3 = FontFamily(
    Font(
        googleFont = fontName3,
        fontProvider = provider
    )
)

val fontName4 = GoogleFont("Lilita One")
val fontFamily4 = FontFamily(
    Font(
        googleFont = fontName4,
        fontProvider = provider
    )
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)