package com.custom.magic.calendar.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.custom.magic.calendar.Event
import com.custom.magic.calendar.sealed.DateBoxStyle
import org.threeten.bp.LocalDate
import java.text.DateFormatSymbols
import java.util.Locale

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
    listState: LazyListState, // ✅ Accept LazyListState for smooth scroll,
    verticalSpacing: Dp,
    topSpacing: Dp,
    daysBarColor:Color
) {
    val weeks = remember(selectedDate.value) { getWeeksInMonth(selectedDate.value) }
     val displayWeeks = if (isExpanded) weeks else listOf(weeks[if (selectedRowIndex < 0) 0 else selectedRowIndex])

    LazyColumn(state = listState, modifier = Modifier.padding(top = topSpacing)) { // ✅ Use LazyColumn instead of Column
        item {
            // Day headers
            Row(modifier = Modifier.fillMaxWidth()) {
                getLocalizedWeekDays().forEach { day ->
                    Text(
                        text = day.take(3),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = daysBarColor
                    )
                }
            }
        }
        items(displayWeeks) { week ->
            Row(modifier = Modifier.fillMaxWidth()
                .padding(top = if (week == displayWeeks[0]) 0.dp else verticalSpacing)
            ) {
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

fun getLocalizedWeekDays(): List<String> {
    val locale = Locale.getDefault() // Get user's default locale
    val weekDays = DateFormatSymbols(locale).weekdays // Array with 8 elements (0 is empty)
    return weekDays.toList().subList(1, 8) // Extract Sunday-Saturday
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

