package com.custom.magic.calendar.sealed


sealed class HeaderStyle {
    data object TitleOnStart : HeaderStyle()
    data object TitleOnEnd : HeaderStyle()
    data object TitleInCenter : HeaderStyle()
    data object WithoutButtons : HeaderStyle()
}
