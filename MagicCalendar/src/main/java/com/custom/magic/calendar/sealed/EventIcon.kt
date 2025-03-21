package com.custom.magic.calendar.sealed

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

sealed class EventIcon(val position: IconPosition) {
    data class Resource(@DrawableRes val resId: Int, val pos: IconPosition = IconPosition.TopEnd) : EventIcon(pos)
    data class Vector(val icon: ImageVector, val pos: IconPosition = IconPosition.TopEnd) : EventIcon(pos)
    data class Custom(val content: @Composable () -> Unit, val pos: IconPosition = IconPosition.TopEnd) : EventIcon(pos)
}
enum class IconPosition {
    TopStart, TopEnd, BottomStart, BottomEnd, Center
}