package com.custom.magic.calendar

import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.Instant
import java.util.Date

fun Date.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this.time)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

fun LocalDate.toDate(): Date {
    return Date(this.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000)
}



