package com.custom.magic.calendar.ui

import android.content.Context
import androidx.annotation.ColorRes
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.custom.magic.calendar.data.HeaderFontStyle
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
    daysBarColor: Color = Color.Black,
    dateBoxStyle: DateBoxStyle = DateBoxStyle.FilledCircle(color = Color.LightGray),
    selectedDateBoxStyle: DateBoxStyle = DateBoxStyle.FilledCircle(color = Color.Blue),
    selectedDayTextColor: Color = Color.White,
    swipeEnabled: Boolean = true,
    bgColor: Color = Color.White,
    collapseButtonTint : Color = Color.Black,
    headerFontStyle: HeaderFontStyle = HeaderFontStyle(
        size = 20.sp, fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold
    ),
    topSpacing: Dp = 20.dp,
    gridPadding: Dp = 4.dp,
    onDateSelected: (Date) -> Unit
) {
    val verticalSpacing = 5.dp
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
        targetValue = if (visibleRows == 1) (rowHeight+20.dp + topSpacing) * visibleRows else (rowHeight + verticalSpacing) * visibleRows - verticalSpacing + topSpacing,
        label = ""
    )

    var isInitialLoad by remember { mutableStateOf(true) }

    // Prefetch months to avoid lag when scrolling
    LaunchedEffect(pagerState.currentPage) {
        val newMonth = LocalDate.now().plusMonths((pagerState.currentPage - startPage).toLong())
        currentMonth.value = newMonth

        if (!isInitialLoad) {
            onDateSelected(newMonth.withDayOfMonth(1).toDate())
        } else {
            isInitialLoad = false
        }

        // Preload adjacent months
//        preloadMonthData(LocalDate.now().plusMonths((pagerState.currentPage - startPage - 1).toLong()))
//        preloadMonthData(LocalDate.now().plusMonths((pagerState.currentPage - startPage + 1).toLong()))
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CalendarHeader(
            currentMonth = selectedDate.toLocalDate(),
            headerStyle = headerStyle,
            prevIcon = prevIcon,
            nextIcon = nextIcon,
            onPrevious = {
                coroutineScope.launch { pagerState.scrollToPage(pagerState.currentPage - 1) }
            },
            onNext = {
                coroutineScope.launch { pagerState.scrollToPage(pagerState.currentPage + 1) }
            },
            accentColor = headerAccentColor,
            headerFontStyle = headerFontStyle
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(bgColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = gridPadding)
                    .height(targetHeight)
                    ,
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
                        listState = listState,
                        verticalSpacing = verticalSpacing,
                        topSpacing = topSpacing,
                        daysBarColor
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
                    tint = collapseButtonTint
                )
            }
        }
    }
}



// Secondary method overrides
@Composable
fun CalendarView(
    context: Context,
    selectedDate: Date,
    events: List<Event>,
    headerStyle: HeaderStyle = HeaderStyle.TitleInCenter,
    prevIcon: CalendarIcon = CalendarIcon.Vector(Icons.AutoMirrored.Filled.ArrowBack),
    nextIcon: CalendarIcon = CalendarIcon.Vector(Icons.AutoMirrored.Filled.ArrowForward),
    headerAccentColor: Color = Color.Black,
    @ColorRes headerAccentColorRes: Int? = null,
    activeTextColor: Color = Color.Black,
    @ColorRes activeTextColorRes: Int? = null,
    inactiveTextColor: Color = Color.Gray,
    @ColorRes inactiveTextColorRes: Int? = null,
    daysBarColor: Color = Color.Black,
    @ColorRes daysBarColorRes: Int? = null,
    dateBoxStyle: DateBoxStyle = DateBoxStyle.FilledCircle(color = Color.LightGray),
    @ColorRes dateBoxColorRes: Int? = null,
    selectedDateBoxStyle: DateBoxStyle = DateBoxStyle.FilledCircle(color = Color.Blue),
    @ColorRes selectedDateBoxColorRes: Int? = null,
    selectedDayTextColor: Color = Color.White,
    @ColorRes selectedDayTextColorRes: Int? = null,
    swipeEnabled: Boolean = true,
    bgColor: Color = Color.White,
    @ColorRes bgColorRes: Int? = null,
    collapseButtonTint: Color = Color.Black,
    @ColorRes collapseButtonTintRes: Int? = null,
    headerFontStyle: HeaderFontStyle = HeaderFontStyle(
        size = 20.sp, fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold
    ),
    topSpacing: Dp = 20.dp,
    gridPadding: Dp = 4.dp,
    onDateSelected: (Date) -> Unit
) {
    // Convert resource IDs to Colors if provided
    val headerAccentColorFinal = headerAccentColorRes?.let { Color(ContextCompat.getColor(context, it)) } ?: headerAccentColor
    val activeTextColorFinal = activeTextColorRes?.let { Color(ContextCompat.getColor(context, it)) } ?: activeTextColor
    val inactiveTextColorFinal = inactiveTextColorRes?.let { Color(ContextCompat.getColor(context, it)) } ?: inactiveTextColor
    val daysBarColorFinal = daysBarColorRes?.let { Color(ContextCompat.getColor(context, it)) } ?: daysBarColor
    val dateBoxColorFinal = dateBoxColorRes?.let { Color(ContextCompat.getColor(context, it)) } ?: (dateBoxStyle as? DateBoxStyle.FilledCircle)?.color ?: Color.LightGray
    val selectedDateBoxColorFinal = selectedDateBoxColorRes?.let { Color(ContextCompat.getColor(context, it)) } ?: (selectedDateBoxStyle as? DateBoxStyle.FilledCircle)?.color ?: Color.Blue
    val selectedDayTextColorFinal = selectedDayTextColorRes?.let { Color(ContextCompat.getColor(context, it)) } ?: selectedDayTextColor
    val bgColorFinal = bgColorRes?.let { Color(ContextCompat.getColor(context, it)) } ?: bgColor
    val collapseButtonTintFinal = collapseButtonTintRes?.let { Color(ContextCompat.getColor(context, it)) } ?: collapseButtonTint

    // Call the main CalendarView with resolved colors
    CalendarView(
        selectedDate = selectedDate,
        events = events,
        headerStyle = headerStyle,
        prevIcon = prevIcon,
        nextIcon = nextIcon,
        headerAccentColor = headerAccentColorFinal,
        activeTextColor = activeTextColorFinal,
        inactiveTextColor = inactiveTextColorFinal,
        daysBarColor = daysBarColorFinal,
        dateBoxStyle = DateBoxStyle.FilledCircle(color = dateBoxColorFinal),
        selectedDateBoxStyle = DateBoxStyle.FilledCircle(color = selectedDateBoxColorFinal),
        selectedDayTextColor = selectedDayTextColorFinal,
        swipeEnabled = swipeEnabled,
        bgColor = bgColorFinal,
        collapseButtonTint = collapseButtonTintFinal,
        headerFontStyle = headerFontStyle,
        topSpacing = topSpacing,
        gridPadding = gridPadding,
        onDateSelected = onDateSelected
    )
}


