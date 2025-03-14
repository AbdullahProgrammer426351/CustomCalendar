package com.custom.magic.calendar.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Brush
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
import com.custom.magic.calendar.sealed.DateBoxStyle
import org.threeten.bp.LocalDate

@Composable
fun CalendarDayView(
    date: LocalDate,
    isSelected: Boolean,
    events: List<Event>,
    isInCurrentMonth: Boolean,
    selectedDayTextColor: Color,
    dateBoxStyle: DateBoxStyle,
    selectedDateBoxStyle: DateBoxStyle,
    activeTextColor: Color,
    inactiveTextColor: Color,
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
                .then(applySimpleDayStyle(if (isSelected) selectedDateBoxStyle else dateBoxStyle)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                color = if (isSelected) selectedDayTextColor else{ if (isInCurrentMonth) activeTextColor else inactiveTextColor}
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

fun applySimpleDayStyle(style: DateBoxStyle): Modifier {
    return when (style) {
        is DateBoxStyle.FilledCircle -> Modifier
            .clip(CircleShape)
            .background(style.color)

        is DateBoxStyle.FilledRectangle -> Modifier
            .clip(RoundedCornerShape(style.cornerRadius))
            .background(style.color)

        is DateBoxStyle.BorderedCircle -> Modifier
            .clip(CircleShape)
            .border(style.borderWidth, style.borderColor, CircleShape)

        is DateBoxStyle.BorderedRectangle -> Modifier
            .clip(RoundedCornerShape(style.cornerRadius))
            .border(style.borderWidth, style.borderColor, RoundedCornerShape(style.cornerRadius))

        is DateBoxStyle.DottedCircle -> Modifier
            .clip(CircleShape)
            .border(
                width = style.dotSize,
                brush = Brush.radialGradient(listOf(style.dotColor, Color.Transparent)),
                shape = CircleShape
            )

        is DateBoxStyle.DottedRectangle -> Modifier
            .clip(RoundedCornerShape(style.cornerRadius))
            .border(
                width = style.dotSize,
                brush = Brush.radialGradient(listOf(style.dotColor, Color.Transparent)),
                shape = RoundedCornerShape(style.cornerRadius)
            )

        is DateBoxStyle.Custom -> Modifier // ⚠️ Do nothing here (handled separately)
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
            selectedDayTextColor = Color.White,
            dateBoxStyle = DateBoxStyle.FilledCircle(color = Color.LightGray),
            selectedDateBoxStyle = DateBoxStyle.FilledCircle(color = Color.Black),
            activeTextColor = Color.Black,
            inactiveTextColor = Color.Gray
        ) { }
    }
}
