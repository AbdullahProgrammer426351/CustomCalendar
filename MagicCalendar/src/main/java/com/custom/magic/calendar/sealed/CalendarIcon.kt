package com.custom.magic.calendar.sealed

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

sealed class CalendarIcon {
    data class Vector(val icon: ImageVector) : CalendarIcon()
    data class Resource(@DrawableRes val resId: Int) : CalendarIcon()
    data class Custom(val content: @Composable () -> Unit) : CalendarIcon()
}
