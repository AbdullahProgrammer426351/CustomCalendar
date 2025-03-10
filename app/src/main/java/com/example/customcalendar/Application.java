package com.example.customcalendar;

import com.custom.magic.calendar.MagicCalendar;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MagicCalendar.init(this);
    }
}
