package com.custom.magic.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CalendarView(
    selectedDate: LocalDate, // Now takes a direct LocalDate value
    events: List<Event>,
    accentColor: Color = Color.Black,
    selectionColor: Color = Color.Blue,
    onDateSelected: (LocalDate) -> Unit
) {
    val startPage = 500
    val pagerState = rememberPagerState(initialPage = startPage)
    val coroutineScope = rememberCoroutineScope()

    val currentMonth = remember { mutableStateOf(selectedDate.withDayOfMonth(1)) }

    // Ensure month updates when swiping
    LaunchedEffect(pagerState.currentPage) {
        val newMonth = LocalDate.now().plusMonths((pagerState.currentPage - startPage).toLong())
        currentMonth.value = newMonth
        onDateSelected(newMonth.withDayOfMonth(1)) // 🔥 Notify ViewModel when month changes
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month", tint = accentColor)
            }

            Text(
                text = currentMonth.value.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(8.dp)
            )

            IconButton(onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next Month", tint = accentColor)
            }
        }

        HorizontalPager(
            count = 1000,
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val monthDate = LocalDate.now().plusMonths((page - startPage).toLong())

            CalendarGridView(
                selectedDate = remember { mutableStateOf(selectedDate) },
                events = events,
                selectionColor = selectionColor,
                onDateSelected = { newDate ->
                    onDateSelected(newDate) // 🔥 Updates ViewModel so XML TextView updates
                }
            )
        }
    }
}





