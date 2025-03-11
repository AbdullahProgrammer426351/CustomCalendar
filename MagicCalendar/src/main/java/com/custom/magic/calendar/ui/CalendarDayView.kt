package com.custom.magic.calendar.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.custom.magic.calendar.Event
import com.custom.magic.calendar.sealed.EventIcon
import com.custom.magic.calendar.sealed.IconPosition
import org.threeten.bp.LocalDate

@Composable
fun CalendarDayView(
    date: LocalDate,
    isSelected: Boolean,
    events: List<Event>,
    isInCurrentMonth: Boolean,
    selectionColor: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Background Circle
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) selectionColor else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                color = if (isInCurrentMonth) Color.Black else Color.Gray
            )
        }

        // Render first event's icon if available
        events.firstOrNull()?.icon?.let { icon ->
            val iconModifier = when (icon.position) {
                IconPosition.TopStart -> Modifier.align(Alignment.TopStart).padding(4.dp)
                IconPosition.TopEnd -> Modifier.align(Alignment.TopEnd).padding(4.dp)
                IconPosition.BottomStart -> Modifier.align(Alignment.BottomStart).padding(4.dp)
                IconPosition.BottomEnd -> Modifier.align(Alignment.BottomEnd).padding(4.dp)
                IconPosition.Center -> Modifier.align(Alignment.Center)
            }

            RenderEventIcon(icon, iconModifier)
        }
    }
}


@Composable
fun RenderEventIcon(eventIcon: EventIcon, modifier: Modifier) {
    when (eventIcon) {
        is EventIcon.Vector -> Icon(
            eventIcon.icon, contentDescription = null, modifier = modifier.size(14.dp)
        )

        is EventIcon.Resource -> Image(
            painter = painterResource(id = eventIcon.resId),
            contentDescription = null,
            modifier = modifier.size(14.dp)
        )

        is EventIcon.Custom -> Box(modifier = modifier) {
            eventIcon.content()
        }
    }
}






@Preview
@Composable
private fun DayPrev() {
    Box(modifier = Modifier
        .background(Color.White)
        .padding(20.dp)){
        CalendarDayView(
            date = LocalDate.now(),
            isSelected = true,
            events = listOf(
                Event(date = LocalDate.now(), eventColor = Color.Red)
            ),
            isInCurrentMonth = true,
            selectionColor = Color.Blue
        ) { }
    }
}
