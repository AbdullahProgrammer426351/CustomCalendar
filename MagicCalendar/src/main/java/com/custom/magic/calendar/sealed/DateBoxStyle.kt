package com.custom.magic.calendar.sealed

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

sealed class DateBoxStyle {
    data class FilledCircle(val color: Color) : DateBoxStyle()
    data class FilledRectangle(val color: Color, val cornerRadius: Dp = 2.dp) : DateBoxStyle()
    data class BorderedCircle(val borderColor: Color, val borderWidth: Dp = 2.dp) : DateBoxStyle()
    data class BorderedRectangle(val borderColor: Color, val borderWidth: Dp = 2.dp, val cornerRadius: Dp = 2.dp) : DateBoxStyle()
    data class DottedCircle(val dotColor: Color, val dotSize: Dp = 4.dp) : DateBoxStyle()
    data class DottedRectangle(val dotColor: Color, val dotSize: Dp = 4.dp, val cornerRadius: Dp = 2.dp) : DateBoxStyle()
    data class Custom(val content: @Composable BoxScope.() -> Unit) : DateBoxStyle()
}
