package com.custom.magic.calendar.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.custom.magic.calendar.Event
import com.custom.magic.calendar.sealed.CircleType
import com.custom.magic.calendar.sealed.EventIcon
import com.custom.magic.calendar.sealed.EventIndicator
import com.custom.magic.calendar.sealed.IconPosition
import com.custom.magic.calendar.sealed.RectangleType
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

        // Render event indicator
        events.firstOrNull()?.indicator?.let { indicator ->
            RenderEventIndicator(
                indicator,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 4.dp)
            )
        }
    }
}


@Composable
fun RenderEventIndicator(indicator: EventIndicator, modifier: Modifier = Modifier) {
    when (indicator) {
        // 🔴 Dot Indicator
        is EventIndicator.Dot -> Box(
            modifier = modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(indicator.color)
        )

        // 🟢 Badge Indicator
        is EventIndicator.Badge -> Box(
            modifier = modifier
                .clip(RoundedCornerShape(50))
                .background(indicator.color)
                .padding(horizontal = 6.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = indicator.count.toString(),
                fontSize = 10.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        // 🟡 Bar Indicator
        is EventIndicator.Bar -> Box(
            modifier = modifier
                .fillMaxWidth(0.5f)
                .height(indicator.thickness)
                .background(indicator.color)
        )

        // 🟠 Ring Indicator
        is EventIndicator.Ring -> Box(
            modifier = modifier
                .size(36.dp)
                .border(width = indicator.thickness, color = indicator.color, shape = CircleShape)
        )

        // 🔵 Circle Indicator
        is EventIndicator.Circle -> {
            Canvas(modifier = modifier.size(20.dp)) {
                when (indicator.type) {
                    CircleType.Filled -> drawCircle(color = indicator.color)
                    CircleType.Stroke -> drawCircle(color = indicator.color, style = Stroke(width = indicator.strokeWidth.toPx()))
                    CircleType.Dotted -> drawCircle(
                        color = indicator.color,
                        style = Stroke(width = indicator.strokeWidth.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f))
                    )
                    CircleType.Transparent -> drawCircle(color = indicator.color.copy(alpha = 0.3f))
                }
            }
        }

        // 🟣 Rectangle Indicator
        is EventIndicator.Rectangle -> {
            Canvas(modifier = modifier.size(20.dp)) {
                when (indicator.type) {
                    RectangleType.Filled -> drawRoundRect(color = indicator.color, cornerRadius = CornerRadius(indicator.cornerRadius.toPx()))
                    RectangleType.Stroke -> drawRoundRect(color = indicator.color, style = Stroke(width = indicator.strokeWidth.toPx()), cornerRadius = CornerRadius(indicator.cornerRadius.toPx()))
                    RectangleType.Dotted -> drawRoundRect(
                        color = indicator.color,
                        style = Stroke(width = indicator.strokeWidth.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)),
                        cornerRadius = CornerRadius(indicator.cornerRadius.toPx())
                    )
                    RectangleType.Transparent -> drawRoundRect(color = indicator.color.copy(alpha = 0.3f), cornerRadius = CornerRadius(indicator.cornerRadius.toPx()))
                }
            }
        }

        // 🛠 Custom Indicator
        is EventIndicator.Custom -> indicator.content()
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
