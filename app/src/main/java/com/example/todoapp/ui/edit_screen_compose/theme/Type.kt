package com.example.todoapp.ui.edit_screen_compose.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.todoapp.R

val Typography = Typography(

    h1 = TextStyle(
        fontFamily = FontFamily(Font( R.font.regular,)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    h2 = TextStyle(
        fontFamily = FontFamily(Font( R.font.regular,)),
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
    ),

    body1 = TextStyle(
        fontFamily = FontFamily(Font( R.font.regular,)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
    ),

    h3 = TextStyle(
        fontFamily = FontFamily(Font( R.font.regular,)),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
    )
)