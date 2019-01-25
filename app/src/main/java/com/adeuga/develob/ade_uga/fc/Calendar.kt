package com.adeuga.develob.ade_uga.fc

import java.util.*
import kotlin.collections.ArrayList

class Calendar {
    private var sorted = false
    private val events = ArrayList<CalendarEvent>()

    init {

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

}