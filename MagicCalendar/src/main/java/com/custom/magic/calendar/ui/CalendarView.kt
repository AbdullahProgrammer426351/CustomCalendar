package com.custom.magic.calendar.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
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
    selectedDateBoxStyle: DateBoxStyle = DateBoxStyle.FilledCircle(color = Color.Blue),
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

    val selectedLocalDate = remember(selectedDate) { mutableStateOf(selectedDate.toLocalDate()) }
    val currentMonth = remember { mutableStateOf(selectedLocalDate.value.withDayOfMonth(1)) }
    val isExpanded = remember { mutableStateOf(true) }

    // Find weeks in the current month
    val weeks = remember(currentMonth.value) { getWeeksInMonth(currentMonth.value) }
    val totalRows = weeks.size

    // Find the row containing the selected date
    val selectedRowIndex = weeks.indexOfFirst { week -> week.contains(selectedLocalDate.value) }
    val visibleRows = if (isExpanded.value) totalRows else 1

    // Calculate dynamic height (each row has fixed height)
    val rowHeight = (LocalConfiguration.current.screenWidthDp.dp / 7)
    val targetHeight by animateDpAsState(
        targetValue = if (visibleRows == 1) (rowHeight+20.dp) * visibleRows else (rowHeight) * visibleRows,
        label = ""
    )

    // Prefetch months to avoid lag when scrolling
    LaunchedEffect(pagerState.currentPage) {
        val newMonth = LocalDate.now().plusMonths((pagerState.currentPage - startPage).toLong())
        currentMonth.value = newMonth
        onDateSelected(newMonth.withDayOfMonth(1).toDate())

        // Preload adjacent months
//        preloadMonthData(LocalDate.now().plusMonths((pagerState.currentPage - startPage - 1).toLong()))
//        preloadMonthData(LocalDate.now().plusMonths((pagerState.currentPage - startPage + 1).toLong()))
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CalendarHeader(
            currentMonth = currentMonth.value,
            headerStyle = headerStyle,
            prevIcon = prevIcon,
            nextIcon = nextIcon,
            onPrevious = {
                coroutineScope.launch { pagerState.scrollToPage(pagerState.currentPage - 1) }
            },
            onNext = {
                coroutineScope.launch { pagerState.scrollToPage(pagerState.currentPage + 1) }
            },
            accentColor = headerAccentColor
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(targetHeight)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White),
            contentAlignment = Alignment.TopCenter
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                userScrollEnabled = swipeEnabled
            ) { page ->
                val listState = rememberLazyListState() // ✅ Lazy state for smooth scroll

                CalendarGridView(
                    selectedDate = selectedLocalDate,
                    events = events,
                    selectedDayTextColor = selectedDayTextColor,
                    dateBoxStyle = dateBoxStyle,
                    selectedDateBoxStyle = selectedDateBoxStyle,
                    activeTextColor = activeTextColor,
                    inactiveTextColor = inactiveTextColor,
                    onDateSelected = { newDate -> onDateSelected(newDate.toDate()) },
                    isExpanded = isExpanded.value,
                    selectedRowIndex = selectedRowIndex,
                    listState = listState // ✅ Pass lazy state
                )
            }
        }

        IconButton(
            onClick = { isExpanded.value = !isExpanded.value },
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = if (isExpanded.value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand/Collapse",
                tint = headerAccentColor
            )
        }
    }
}


