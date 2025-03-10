package com.example.customcalendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class MainViewModel : ViewModel() {
    private val _selectedDate = MutableLiveData(LocalDate.now())
    val selectedDate: LiveData<LocalDate> = _selectedDate

    fun updateDate(date: LocalDate) {
        _selectedDate.value = date
    }
}
