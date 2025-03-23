package com.custom.magic.calendar

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.custom.magic.calendar.sealed.EventIcon
import com.custom.magic.calendar.sealed.EventIndicator
import org.threeten.bp.LocalDate
import java.util.Date
import java.util.UUID

data class Event(
    val id: UUID = UUID.randomUUID(),
    val date: LocalDate,
    val eventColor: Color,
    val icon: EventIcon? = null,
    val indicator: EventIndicator? = null
) {
    constructor(
        id: UUID = UUID.randomUUID(),
        date: Date,
        eventColor: Color,
        icon: EventIcon? = null,
        indicator: EventIndicator? = null
    ) : this(
        id = id,
        date = date.toLocalDate(),
        eventColor = eventColor,
        icon = icon,
        indicator = indicator
    )

    constructor(
        context: Context,
        id: UUID = UUID.randomUUID(),
        date: LocalDate,
        @ColorRes eventColorRes: Int,
        icon: EventIcon? = null,
        indicator: EventIndicator? = null
    ) : this(
        id = id,
        date = date,
        eventColor = Color(ContextCompat.getColor(context, eventColorRes)),
        icon = icon,
        indicator = indicator
    )

    constructor(
        context: Context,
        id: UUID = UUID.randomUUID(),
        date: Date,
        @ColorRes eventColorRes: Int,
        icon: EventIcon? = null,
        indicator: EventIndicator? = null
    ) : this(
        context = context,
        id = id,
        date = date.toLocalDate(),
        eventColorRes = eventColorRes,
        icon = icon,
        indicator = indicator
    )
}





