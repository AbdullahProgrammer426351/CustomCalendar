package com.custom.magic.calendar.sealed

import android.content.Context
import androidx.annotation.ColorRes
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

sealed class DateBoxStyle {

    data class FilledCircle(val color: Color) : DateBoxStyle() {
        constructor(context: Context, @ColorRes colorRes: Int) :
                this(Color(ContextCompat.getColor(context, colorRes)))
    }

    data class FilledRectangle(val color: Color, val cornerRadius: Dp = 2.dp) : DateBoxStyle() {
        constructor(context: Context, @ColorRes colorRes: Int, cornerRadius: Dp = 2.dp) :
                this(Color(ContextCompat.getColor(context, colorRes)), cornerRadius)
    }

    data class BorderedCircle(val borderColor: Color, val borderWidth: Dp = 2.dp) : DateBoxStyle() {
        constructor(context: Context, @ColorRes borderColorRes: Int, borderWidth: Dp = 2.dp) :
                this(Color(ContextCompat.getColor(context, borderColorRes)), borderWidth)
    }

    data class BorderedRectangle(
        val borderColor: Color, val borderWidth: Dp = 2.dp, val cornerRadius: Dp = 2.dp
    ) : DateBoxStyle() {
        constructor(context: Context, @ColorRes borderColorRes: Int, borderWidth: Dp = 2.dp, cornerRadius: Dp = 2.dp) :
                this(Color(ContextCompat.getColor(context, borderColorRes)), borderWidth, cornerRadius)
    }

    data class DottedCircle(val dotColor: Color, val dotSize: Dp = 4.dp) : DateBoxStyle() {
        constructor(context: Context, @ColorRes dotColorRes: Int, dotSize: Dp = 4.dp) :
                this(Color(ContextCompat.getColor(context, dotColorRes)), dotSize)
    }

    data class DottedRectangle(
        val dotColor: Color, val dotSize: Dp = 4.dp, val cornerRadius: Dp = 2.dp
    ) : DateBoxStyle() {
        constructor(context: Context, @ColorRes dotColorRes: Int, dotSize: Dp = 4.dp, cornerRadius: Dp = 2.dp) :
                this(Color(ContextCompat.getColor(context, dotColorRes)), dotSize, cornerRadius)
    }

    data class Custom(val content: @Composable BoxScope.() -> Unit) : DateBoxStyle()
}
