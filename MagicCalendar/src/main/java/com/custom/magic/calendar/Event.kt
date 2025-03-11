package com.custom.magic.calendar

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.custom.magic.calendar.sealed.EventIcon
import org.threeten.bp.LocalDate
import java.util.Date
import java.util.UUID

data class Event(
    val id: UUID = UUID.randomUUID(),
    val date: LocalDate,
    val eventColor: Color,
    val icon: EventIcon? = null
) {
    constructor(
        id: UUID = UUID.randomUUID(),
        date: Date,
        eventColor: Color,
        icon: EventIcon? = null
    ) : this(
        id = id,
        date = date.toLocalDate(),
        eventColor = eventColor,
        icon = icon
    )
}




