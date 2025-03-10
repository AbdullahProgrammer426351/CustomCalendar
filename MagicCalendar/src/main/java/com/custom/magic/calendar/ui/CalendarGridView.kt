package com.custom.magic.calendar.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.custom.magic.calendar.Event
import org.threeten.bp.LocalDate

@Composable
fun CalendarGridView(
    selectedDate: MutableState<LocalDate>,
    events: List<Event>,
    selectionColor: Color,
    onDateSelected: (LocalDate) -> Unit
) {
    val weeks = remember(selectedDate.value) { getWeeksInMonth(selectedDate.value) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Day headers (already working fine)
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Date grid with evenly distributed width
        weeks.forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    Box(
                        modifier = Modifier
                            .weight(1f) // Ensures equal width for all 7 days
                            .aspectRatio(1f) // Ensures the box remains a square
                    ) {
                        CalendarDayView(
                            date = date,
                            isSelected = selectedDate.value == date,
                            events = events.filter { it.date == date },
                            isInCurrentMonth = date.month == selectedDate.value.month,
                            selectionColor = selectionColor
                        ) {
                            selectedDate.value = date
                            onDateSelected(date)
                        }
                    }
                }
            }
        }

    }
}


fun getWeeksInMonth(selectedDate: LocalDate): List<List<LocalDate>> {
    val firstDayOfMonth = selectedDate.withDayOfMonth(1)
    val lastDayOfMonth = selectedDate.withDayOfMonth(selectedDate.lengthOfMonth())

    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // Adjust for Sunday start
    val lastDayOfWeek = lastDayOfMonth.dayOfWeek.value % 7

    val startDate = firstDayOfMonth.minusDays(firstDayOfWeek.toLong())
    val endDate = lastDayOfMonth.plusDays((6 - lastDayOfWeek).toLong())

    val dates = generateSequence(startDate) { it.plusDays(1) }.takeWhile { it <= endDate }.toList()
    return dates.chunked(7)
}

