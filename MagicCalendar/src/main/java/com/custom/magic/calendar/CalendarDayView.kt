package com.custom.magic.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate

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
        contentAlignment = Alignment.TopEnd
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier.size(48.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray) // Always light gray background
                    .border(
                        width = if (isSelected) 2.dp else 0.dp, // Stroke only when selected
                        color = if (isSelected) selectionColor else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable { onClick() },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    color = if (isInCurrentMonth) Color.Black else Color.Gray
                )
            }

            if (events.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(events.first().eventColor)
                )
            }
        }

        if (events.isNotEmpty()) {
            events.first().icon?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(2.dp)
                        .size(12.dp)
                )
            }
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
