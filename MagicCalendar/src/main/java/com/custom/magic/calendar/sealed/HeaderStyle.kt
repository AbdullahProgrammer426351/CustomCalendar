package com.custom.magic.calendar.sealed

import androidx.compose.runtime.Composable

sealed class HeaderStyle {
    object TitleOnStart : HeaderStyle()
    object TitleOnEnd : HeaderStyle()
    object TitleInCenter : HeaderStyle()
    object WithoutButtons : HeaderStyle()
    data class Custom(val content: @Composable () -> Unit) : HeaderStyle()
}
