package com.adeuga.develob.ade_uga.fc

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.adeuga.develob.ade_uga.fc.db.AppDatabase
import com.adeuga.develob.ade_uga.fc.db.Utils
import java.io.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Calendar(var date:Date, var db:AppDatabase) : Serializable {
    private var sorted = false
    private var events = ArrayList<CalendarEvent>()


    init {
//        val parser = IcsParser(0, this.date)
//        parser.execute()
//        this.events = parser.get()

        val dao = db.dao()
        val res = dao.get(Utils.getStandartDate(this.date))
        Log.d(">>>", res.toString())


    }

    fun getDateToString() : String {
        val df = SimpleDateFormat("EEEE d MMMM")
        return df.format(date).capitalize()
    }



    fun getEvents() : ArrayList<CalendarEvent> {
        if(!sorted) {
            Collections.sort(this.events)
            sorted = true
        }
        return this.events
    }

//    fun createNextDayCalendar() : Calendar {
//        return Calendar(this.date, this.ctx)
//    }

}