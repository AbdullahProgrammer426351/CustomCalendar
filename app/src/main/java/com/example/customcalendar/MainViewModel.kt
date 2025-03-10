package com.example.customcalendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Date

class MainViewModel : ViewModel() {
    private val _selectedDate = MutableLiveData(Date())
    val selectedDate: LiveData<Date> = _selectedDate

    fun updateDate(date: Date) {
        _selectedDate.value = date
    }
}
