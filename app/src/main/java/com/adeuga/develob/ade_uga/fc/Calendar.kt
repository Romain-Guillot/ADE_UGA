package com.adeuga.develob.ade_uga.fc

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class Calendar(var date:Date) : Serializable {
    private var sorted = false
    private var events = ArrayList<CalendarEvent>()

    init {
        val parser = IcsParser(0, this.date)
        parser.execute()
        this.events = parser.get()
    }

    fun addEvent(e: CalendarEvent) {
        this.sorted = false
        this.events.add(e)
    }

    fun getEventsByDate(d:Date) : ArrayList<CalendarEvent> {
        var res = ArrayList<CalendarEvent>()
        for(event in this.events) {
            
        }
        return res
    }

    fun getEvents() : ArrayList<CalendarEvent> {
        if(!sorted) {
            Collections.sort(this.events)
            sorted = true
        }
        return this.events
    }

    fun createNextDayCalendar() : Calendar {
        return Calendar(this.date)
    }

}