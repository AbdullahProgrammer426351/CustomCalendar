package com.custom.magic.calendar.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import com.custom.magic.calendar.Event
import com.custom.magic.calendar.toDate
import com.custom.magic.calendar.toLocalDate
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun CalendarView(
    selectedDate: Date, // Takes java.util.Date as input
    events: List<Event>,
    accentColor: Color = Color.Black,
    selectionColor: Color = Color.Blue,
    onDateSelected: (Date) -> Unit // Returns java.util.Date
) {
    val startPage = 500
    val pagerState = rememberPagerState(
        initialPage = startPage,
        initialPageOffsetFraction = 0f, // Default to no offset
        pageCount = { 1000 } // Use a lambda to provide the page count dynamically
    )
    val coroutineScope = rememberCoroutineScope()

    // Convert Date to LocalDate
    val selectedLocalDate = remember(selectedDate) { selectedDate.toLocalDate() }
    val currentMonth = remember { mutableStateOf(selectedLocalDate.withDayOfMonth(1)) }

    // Ensure month updates when swiping
    LaunchedEffect(pagerState.currentPage) {
        val newMonth = LocalDate.now().plusMonths((pagerState.currentPage - startPage).toLong())
        currentMonth.value = newMonth
        onDateSelected(newMonth.withDayOfMonth(1).toDate()) // Convert back to Date when month changes
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
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month", tint = accentColor)
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
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month", tint = accentColor)
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) {
            CalendarGridView(
                selectedDate = remember { mutableStateOf(selectedLocalDate) },
                events = events,
                selectionColor = selectionColor,
                onDateSelected = { newDate ->
                    onDateSelected(newDate.toDate()) // Convert LocalDate to Date before returning
                }
            )
        }
    }
}






