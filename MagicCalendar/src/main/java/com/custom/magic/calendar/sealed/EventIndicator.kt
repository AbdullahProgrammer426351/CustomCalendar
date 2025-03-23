package com.custom.magic.calendar.sealed

import android.content.Context
import androidx.annotation.ColorRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

sealed class EventIndicator {

    data class Dot(val color: Color) : EventIndicator() {
        constructor(context: Context, @ColorRes colorRes: Int) :
                this(Color(ContextCompat.getColor(context, colorRes)))
    }

    data class Badge(val color: Color, val count: Int) : EventIndicator() {
        constructor(context: Context, @ColorRes colorRes: Int, count: Int) :
                this(Color(ContextCompat.getColor(context, colorRes)), count)
    }

    data class Bar(val color: Color, val thickness: Dp = 2.dp, val radius: Dp = 8.dp) : EventIndicator() {
        constructor(context: Context, @ColorRes colorRes: Int, thickness: Dp = 2.dp, radius: Dp = 8.dp) :
                this(Color(ContextCompat.getColor(context, colorRes)), thickness, radius)
    }

    data class Ring(val color: Color, val thickness: Dp = 2.dp) : EventIndicator() {
        constructor(context: Context, @ColorRes colorRes: Int, thickness: Dp = 2.dp) :
                this(Color(ContextCompat.getColor(context, colorRes)), thickness)
    }

    // 🟢 Circle Variations
    data class Circle(
        val color: Color,
        val strokeWidth: Dp = 2.dp,
        val type: CircleType = CircleType.Filled
    ) : EventIndicator() {
        constructor(context: Context, @ColorRes colorRes: Int, strokeWidth: Dp = 2.dp, type: CircleType = CircleType.Filled) :
                this(Color(ContextCompat.getColor(context, colorRes)), strokeWidth, type)
    }

    // 🔵 Rectangle Variations
    data class Rectangle(
        val color: Color,
        val cornerRadius: Dp = 4.dp,
        val strokeWidth: Dp = 2.dp,
        val type: RectangleType = RectangleType.Filled
    ) : EventIndicator() {
        constructor(context: Context, @ColorRes colorRes: Int, cornerRadius: Dp = 4.dp, strokeWidth: Dp = 2.dp, type: RectangleType = RectangleType.Filled) :
                this(Color(ContextCompat.getColor(context, colorRes)), cornerRadius, strokeWidth, type)
    }

    // 🛠 Custom Implementation
    data class Custom(val content: @Composable () -> Unit) : EventIndicator()
}

// Circle Type Enum
enum class CircleType { Filled, Stroke, Dotted, Transparent }

// Rectangle Type Enum
enum class RectangleType { Filled, Stroke, Dotted, Transparent }

