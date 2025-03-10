package com.example.customcalendar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.custom.magic.calendar.ui.CalendarView
import com.custom.magic.calendar.Event
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
                            date = Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000), // Add 2 days
                            eventColor = Color.Red,
                            icon = R.drawable.ic_launcher_background
                        ),
                        Event(
                            date = Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000), // Add 5 days
                            eventColor = Color.Green
                        )
                    )
                }



                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CalendarView(selectedDate.value, events) { newDate ->
                        viewModel.updateDate(newDate) // Ensure XML is updated
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Your selected date is ${selectedDate.value}")
                }
            }
        }
    }
}



