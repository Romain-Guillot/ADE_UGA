package com.adeuga.develob.ade_uga.fc

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 */
class CalendarEvent : Comparable<CalendarEvent>, Serializable {

    var title:String? = null
        set(value) { field = value?.trim()} // trim(): cleaning data
    var description:String? = null
        set(value) { field = value?.trim()} // trim(): cleaning data
    var location:String? = null
        set(value) { field = value?.trim()} // trim(): cleaning data
    var begin:Date? = null
    var end:Date? = null

    /**
     *
     */
    fun getBegin() : String {
        return begin.toString()
    }

    /**
     *
     */
    fun getBeginHour() : String {
        val dateStr = SimpleDateFormat("HH:mm").format(this.begin)
        return dateStr
    }

    /**
     *
     */
    fun getEndHour() : String {
        val dateStr = SimpleDateFormat("HH:mm").format(this.end)
        return dateStr
    }

    /**
     *
     */
    override fun compareTo(other: CalendarEvent): Int {
        if(begin == null || other.begin == null)
            return 0
        return (this.begin?.compareTo(other.begin))?:0
    }

    /**
     *
     */
    override fun toString(): String {
        return title + " " + description + " " + begin.toString()
    }


}