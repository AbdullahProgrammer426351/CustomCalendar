package com.custom.magic.calendar.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.custom.magic.calendar.Event
import com.custom.magic.calendar.toDate
import com.custom.magic.calendar.toLocalDate
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import com.custom.magic.calendar.sealed.CalendarIcon
import com.custom.magic.calendar.sealed.HeaderStyle
import com.custom.magic.calendar.sealed.DateBoxStyle
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.util.Date

@Composable
fun CalendarView(
    selectedDate: Date,
    events: List<Event>,
    headerStyle: HeaderStyle = HeaderStyle.TitleInCenter,
    prevIcon: CalendarIcon = CalendarIcon.Vector(Icons.AutoMirrored.Filled.ArrowBack),
    nextIcon: CalendarIcon = CalendarIcon.Vector(Icons.AutoMirrored.Filled.ArrowForward),
    headerAccentColor: Color = Color.Black,
    activeTextColor: Color = Color.Black,
    inactiveTextColor: Color = Color.Gray,
    dateBoxStyle: DateBoxStyle = DateBoxStyle.FilledCircle(color = Color.LightGray),
    selectedDateBoxStyle: DateBoxStyle = DateBoxStyle.FilledRectangle(color = Color.Black),
    selectedDayTextColor: Color = Color.White,
    swipeEnabled: Boolean = true,
    onDateSelected: (Date) -> Unit
) {
    val startPage = 500
    val pagerState = rememberPagerState(
        initialPage = startPage,
        initialPageOffsetFraction = 0f,
        pageCount = { 1000 }
    )
    val coroutineScope = rememberCoroutineScope()

    val selectedLocalDate = remember(selectedDate) { selectedDate.toLocalDate() }
    val currentMonth = remember { mutableStateOf(selectedLocalDate.withDayOfMonth(1)) }

    LaunchedEffect(pagerState.currentPage) {
        val newMonth = LocalDate.now().plusMonths((pagerState.currentPage - startPage).toLong())
        currentMonth.value = newMonth
        onDateSelected(newMonth.withDayOfMonth(1).toDate())
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CalendarHeader(
            currentMonth = currentMonth.value,
            headerStyle = headerStyle,
            prevIcon = prevIcon,
            nextIcon = nextIcon,
            onPrevious = {
                coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
            },
            onNext = {
                coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
            },
            accentColor = headerAccentColor
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            userScrollEnabled = swipeEnabled
        ) {
            CalendarGridView(
                selectedDate = remember { mutableStateOf(selectedLocalDate) },
                events = events,
                selectedDayTextColor = selectedDayTextColor,
                dateBoxStyle = dateBoxStyle,
                selectedDateBoxStyle = selectedDateBoxStyle,
                activeTextColor = activeTextColor,
                inactiveTextColor = inactiveTextColor,
                onDateSelected = { newDate -> onDateSelected(newDate.toDate()) }
            )
        }
    }
}
