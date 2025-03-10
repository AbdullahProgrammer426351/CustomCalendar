package com.custom.magic.calendar

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.util.UUID

data class Event(
    val id: UUID = UUID.randomUUID(),
    val date: LocalDate,
    val eventColor: Color,
    @DrawableRes var icon: Int? = null
)
