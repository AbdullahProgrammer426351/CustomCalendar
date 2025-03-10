package com.custom.magic.calendar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import java.time.LocalDate

@Composable
fun CalendarPagerView(
    events: List<Event>,
    selectionColor: Color,
    onDateSelected: (LocalDate) -> Unit
) {
    val startPage = 500 // Middle point to represent current month
    val pagerState = rememberPagerState(initialPage = startPage)

    HorizontalPager(
        count = 1000, // Large enough for multiple years
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        val currentMonth = LocalDate.now().plusMonths((page - startPage).toLong()) // Adjust range
        CalendarGridView(
            selectedDate = remember { mutableStateOf(currentMonth) },
            events = events,
            selectionColor = selectionColor,
            onDateSelected = onDateSelected
        )
    }
}

