package com.adeuga.develob.ade_uga.fc

import java.text.SimpleDateFormat
import java.util.*

class CalendarEvent : Comparable<CalendarEvent> {


    var title:String? = null
    var description:String? = null
    var location:String? = null
    var begin:Date? = null
    var end:Date? = null

    override fun toString(): String {
        return title + " " + description + " " + begin.toString()
    }

    fun getBegin():String {
        return begin.toString()
    }

    fun getBeginHour():List<String> {
        val dateStr = SimpleDateFormat("dd:HH:mm").format(this.begin)
        return dateStr.split(":")
    }


    override
    fun compareTo(other: CalendarEvent): Int {
        if(begin == null || other.begin == null)
            return 0
        return (this.begin?.compareTo(other.begin))?:0
    }


}