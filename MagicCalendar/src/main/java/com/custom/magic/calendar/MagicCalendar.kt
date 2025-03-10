package com.custom.magic.calendar

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class MagicCalendar {
    companion object{
        @JvmStatic
        fun init(application: Application) {
            AndroidThreeTen.init(application)
        }
    }
}