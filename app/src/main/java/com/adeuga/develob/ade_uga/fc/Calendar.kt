package com.adeuga.develob.ade_uga.fc

import android.util.Log
import com.adeuga.develob.ade_uga.fc.db.AppDatabase
import com.adeuga.develob.ade_uga.fc.db.DbDayData
import com.adeuga.develob.ade_uga.fc.db.DbDayDataDao
import com.adeuga.develob.ade_uga.fc.db.Utils
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Calendar(var date:Date, var db:AppDatabase) : Serializable {
    private var sorted = false
    private var events = ArrayList<CalendarEvent>()
    private var ui:UIcalendar? = null
    private var dao:DbDayDataDao? = null


    init {
        Thread{
            this.readDatabase()
        }.start()
    }

    fun getDateToString() : String {
        val df = SimpleDateFormat("EEEE d MMMM")
        return df.format(date).capitalize()
    }



    fun getEvents() : ArrayList<CalendarEvent> {
        Collections.sort(this.events)
        return this.events
    }

    fun readDatabase() {
        Log.d(">>> DB","DB BEGIN")
        this.dao = db.dao()
        val res:DbDayData? = this.dao?.get(Utils.getStandartDate(this.date))
        if(res == null) {
            val parser = IcsParser(0, this.date)
            parser.execute()
            this.events = parser.get()
            dao?.insert(DbDayData(Utils.getStandartDate(this.date), parser.getContent()))
            Log.d(">>>>", "Data inserted")
            this.ui?.notifyDataDownloaded()
        }else {
            val parser = IcsParser(0, this.date, res.content)
            parser.execute()
            this.events = parser.get()
        }
        notifyUI()
    }

    fun addUI(ui:UIcalendar) {
        this.ui = ui
    }

    fun update() {
        Thread {
            Log.d(">>>", "Update")
            val parser = IcsParser(0, this.date)
            parser.execute()
            this.events = parser.get()
            notifyUI()
            this.ui?.notifyDataDownloaded()
        }.start()
    }

    fun notifyUI() {
        this.ui?.notifyEventListChanged()
    }



}