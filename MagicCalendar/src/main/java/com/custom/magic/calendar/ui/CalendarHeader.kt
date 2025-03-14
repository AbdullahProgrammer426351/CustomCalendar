package com.custom.magic.calendar.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.custom.magic.calendar.sealed.CalendarIcon
import com.custom.magic.calendar.sealed.HeaderStyle
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CalendarHeader(
    currentMonth: LocalDate,
    headerStyle: HeaderStyle,
    prevIcon: CalendarIcon,
    nextIcon: CalendarIcon,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    accentColor: Color
) {
    when (headerStyle) {
        is HeaderStyle.TitleOnStart -> Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CalendarTitle(currentMonth)
            Spacer(modifier = Modifier.weight(1f))
            NavigationButtons(prevIcon, nextIcon, onPrevious, onNext, accentColor)
        }

        is HeaderStyle.TitleOnEnd -> Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationButtons(prevIcon, nextIcon, onPrevious, onNext, accentColor)
            Spacer(modifier = Modifier.weight(1f))
            CalendarTitle(currentMonth)
        }

        is HeaderStyle.TitleInCenter -> Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPrevious) { RenderIcon(prevIcon, accentColor) }

            CalendarTitle(currentMonth)

            IconButton(onClick = onNext) { RenderIcon(nextIcon, accentColor) }
        }

        HeaderStyle.WithoutButtons -> Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
            CalendarTitle(currentMonth)
        }

        is HeaderStyle.Custom -> headerStyle.content()

    }
}


@Composable
fun CalendarTitle(currentMonth: LocalDate) {
    Text(
        text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())),
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun NavigationButtons(
    prevIcon: CalendarIcon,
    nextIcon: CalendarIcon,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    accentColor: Color
) {
    IconButton(onClick = onPrevious) { RenderIcon(prevIcon, accentColor) }
    IconButton(onClick = onNext) { RenderIcon(nextIcon, accentColor) }
}


@Composable
fun RenderIcon(icon: CalendarIcon, tint: Color) {
    when (icon) {
        is CalendarIcon.Vector -> Icon(icon.icon, contentDescription = null, tint = tint)
        is CalendarIcon.Resource -> Image(
            painter = painterResource(id = icon.resId),
            contentDescription = null
        )
        is CalendarIcon.Custom -> icon.content()
    }
}

