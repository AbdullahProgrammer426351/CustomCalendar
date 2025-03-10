package com.example.customcalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.custom.magic.calendar.Event
import com.example.customcalendar.ui.theme.CustomCalendarTheme
import java.time.LocalDate

class HahaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CustomCalendarTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
                    val events = remember {
                        listOf(
                            com.custom.magic.calendar.Event(
                                date = LocalDate.now().plusDays(2),
                                eventColor = Color.Red,
                                icon = R.drawable.ic_launcher_background
                            ),
                            com.custom.magic.calendar.Event(
                                date = LocalDate.now().plusDays(5),
                                eventColor = Color.Green
                            )
                        )
                    }
Text("jajaja")
//                   Column(modifier = Modifier.padding(innerPadding).padding(20.dp)) {
//                       CalendarView(selectedDate, events, onDateSelected = {})
//                       Text("Your selected date is ${selectedDate.value}")
//                   }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CustomCalendarTheme {
        Greeting("Android")
    }
}