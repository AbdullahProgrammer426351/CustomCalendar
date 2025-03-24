package com.custom.magic.calendar.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.custom.magic.calendar.data.HeaderFontStyle
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
    accentColor: Color,
    headerFontStyle: HeaderFontStyle
) {
    when (headerStyle) {
        is HeaderStyle.TitleOnStart -> Row(
            modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CalendarTitle(currentMonth, headerFontStyle, accentColor)
            Spacer(modifier = Modifier.weight(1f))
            NavigationButtons(prevIcon, nextIcon, onPrevious, onNext, accentColor)
        }

        is HeaderStyle.TitleOnEnd -> Row(
            modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationButtons(prevIcon, nextIcon, onPrevious, onNext, accentColor)
            Spacer(modifier = Modifier.weight(1f))
            CalendarTitle(currentMonth, headerFontStyle, accentColor)
        }

        is HeaderStyle.TitleInCenter -> Row(
            modifier = Modifier
                .width(LocalConfiguration.current.screenWidthDp.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.clickable{onPrevious()}){ RenderIcon(prevIcon, accentColor) }

            CalendarTitle(currentMonth, headerFontStyle, accentColor)

            Box(modifier = Modifier.clickable{onNext()}){ RenderIcon(nextIcon, accentColor) }
        }

        HeaderStyle.WithoutButtons -> Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
            CalendarTitle(currentMonth, headerFontStyle, accentColor)
        }
    }
}


@Composable
fun CalendarTitle(currentMonth: LocalDate, headerFontStyle: HeaderFontStyle, textColor:Color) {
    Text(
        text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())),
        fontSize = headerFontStyle.size,
        fontFamily = headerFontStyle.fontFamily,
        fontWeight = headerFontStyle.fontWeight,
        modifier = Modifier.padding(8.dp),
        color = textColor
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

