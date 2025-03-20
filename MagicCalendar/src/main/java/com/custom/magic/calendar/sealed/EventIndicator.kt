package com.custom.magic.calendar.sealed

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

sealed class EventIndicator {
    data class Dot(val color: Color) : EventIndicator()
    data class Badge(val color: Color, val count: Int) : EventIndicator()
    data class Bar(val color: Color, val thickness: Dp = 2.dp, val radius: Dp = 8.dp) : EventIndicator()
    data class Ring(val color: Color, val thickness: Dp = 2.dp) : EventIndicator()

    // 🟢 Circle Variations
    data class Circle(
        val color: Color,
        val strokeWidth: Dp = 2.dp,
        val type: CircleType = CircleType.Filled
    ) : EventIndicator()

    // 🔵 Rectangle Variations
    data class Rectangle(
        val color: Color,
        val cornerRadius: Dp = 4.dp,
        val strokeWidth: Dp = 2.dp,
        val type: RectangleType = RectangleType.Filled
    ) : EventIndicator()

    // 🛠 Custom Implementation
    data class Custom(val content: @Composable () -> Unit) : EventIndicator()
}

// Circle Type Enum
enum class CircleType { Filled, Stroke, Dotted, Transparent }

// Rectangle Type Enum
enum class RectangleType { Filled, Stroke, Dotted, Transparent }

