package com.custom.magic.calendar.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.custom.magic.calendar.Event
import com.custom.magic.calendar.sealed.DateBoxStyle
import org.threeten.bp.LocalDate

@Composable
fun CalendarGridView(
    selectedDate: MutableState<LocalDate>,
    events: List<Event>,
    selectedDayTextColor: Color,
    dateBoxStyle: DateBoxStyle,
    selectedDateBoxStyle: DateBoxStyle,
    activeTextColor: Color,
    inactiveTextColor: Color,
    onDateSelected: (LocalDate) -> Unit,
    isExpanded: Boolean,
    selectedRowIndex: Int,
    listState: LazyListState // ✅ Accept LazyListState for smooth scroll
) {
    val weeks = remember(selectedDate.value) { getWeeksInMonth(selectedDate.value) }
    val displayWeeks = if (isExpanded) weeks else listOf(weeks[selectedRowIndex])

    LazyColumn(state = listState) { // ✅ Use LazyColumn instead of Column
        item {
            // Day headers
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
        }
        items(displayWeeks) { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                    ) {
                        CalendarDayView(
                            date = date,
                            isSelected = selectedDate.value == date,
                            events = events.filter { it.date == date },
                            isInCurrentMonth = date.month == selectedDate.value.month,
                            selectedDayTextColor = selectedDayTextColor,
                            dateBoxStyle = dateBoxStyle,
                            selectedDateBoxStyle = selectedDateBoxStyle,
                            activeTextColor = activeTextColor,
                            inactiveTextColor = inactiveTextColor
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

// Helper function to preload months (simulate caching)
fun preloadMonthData(month: LocalDate) {
    val weeks = getWeeksInMonth(month)
    // Store this data in cache or ViewModel (if applicable)
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

