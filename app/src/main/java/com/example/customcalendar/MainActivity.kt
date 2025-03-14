package com.example.customcalendar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.custom.magic.calendar.ui.CalendarView
import com.custom.magic.calendar.Event
import com.custom.magic.calendar.sealed.CalendarIcon
import com.custom.magic.calendar.sealed.CircleType
import com.custom.magic.calendar.sealed.EventIcon
import com.custom.magic.calendar.sealed.EventIndicator
import com.custom.magic.calendar.sealed.IconPosition
import com.custom.magic.calendar.sealed.RectangleType
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val textView = findViewById<TextView>(R.id.xmlTextView)
        val composeView = findViewById<ComposeView>(R.id.composeView)

        // Observe selectedDate and update the XML TextView
        viewModel.selectedDate.observe(this) { date ->
            textView.text = "XML Date: $date"
        }

        // Set Compose content
        composeView.setContent {
            MaterialTheme {
                val selectedDate = viewModel.selectedDate.observeAsState(Date())

                val events = remember {
                    listOf(
                        Event(
                            date = Date(),
                            eventColor = Color.Green,
                            icon = EventIcon.Custom(
                                content = {
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .background(Color.Yellow, shape = CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("!", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                },
                                pos = IconPosition.Center
                            ),
                            indicator = EventIndicator.Rectangle(Color.Magenta, strokeWidth = 2.dp, type = RectangleType.Dotted)
                        ),
                        Event(
                            date = Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000),
                            eventColor = Color.Red,
                            icon = EventIcon.Resource(R.drawable.ic_launcher_background, IconPosition.BottomEnd)
                        )
                        ,
                        Event(
                            date = Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000),
                            eventColor = Color.Blue,
                            icon = EventIcon.Vector(Icons.Default.Star, IconPosition.TopStart)
                        )
                    )
                }



                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CalendarView(selectedDate.value, events, prevIcon = CalendarIcon.Custom {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color.Red, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "<", color = Color.White)
                        }
                    },
                        nextIcon = CalendarIcon.Vector(Icons.AutoMirrored.Filled.ArrowBack),
                        activeTextColor = Color.Red,
                        inactiveTextColor = Color.Yellow,
                        selectedDayTextColor = Color.Green) { newDate ->
                        viewModel.updateDate(newDate) // Ensure XML is updated
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Your selected date is ${selectedDate.value}")
                }
            }
        }
    }
}



