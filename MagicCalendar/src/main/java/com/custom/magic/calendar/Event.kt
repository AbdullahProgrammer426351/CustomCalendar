package com.custom.magic.calendar

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import org.threeten.bp.LocalDate
import java.util.Date
import java.util.UUID

data class Event(
    val id: UUID = UUID.randomUUID(),
    val date: LocalDate,
    val eventColor: Color,
    @DrawableRes var icon: Int? = null
) {
    // Secondary constructor to accept java.util.Date
    constructor(id: UUID = UUID.randomUUID(), date: Date, eventColor: Color, @DrawableRes icon: Int? = null) :
            this(
                id = id,
                date = date.toLocalDate(), // Convert Date to LocalDate
                eventColor = eventColor,
                icon = icon
            )
}



