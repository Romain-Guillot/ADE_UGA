package com.adeuga.develob.ade_uga.fc

import android.util.Log
import com.adeuga.develob.ade_uga.fc.db.*
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 *
 */
class Calendar(var date:Date, var db:AppDatabase) : Serializable {

    private var events = ArrayList<CalendarEvent>()
    private var tasks = ArrayList<Task>()
    private var ui:UIcalendar? = null
    private var dao:DbDayDataDao = db.dao()

    init {
        Thread{
            readDatabase()
            loadTasks()
        }.start()
    }

    /**
     *
     */
    fun getDateToString() : String {
        val df = SimpleDateFormat("EEEE d MMMM")
        return df.format(date).capitalize()
    }

    /**
     *
     */
    fun getEvents() : ArrayList<CalendarEvent> {
        this.events.sort()
        return this.events
    }

    /**
     *
     */
    fun getTasks() : ArrayList<Task> {
        return this.tasks
    }

    /**
     *
     */
    private fun readDatabase(forceReloadData:Boolean = false) {
        Log.d(">>> DB","DB BEGIN")
        val res:DbDayData? = this.dao.getEvents(Utils.getStandartDate(this.date))
        if(res == null || forceReloadData) {
            val parser = IcsParser(0, this.date)
            parser.execute()
            this.events = parser.get()
            dao.insertEvent(DbDayData(Utils.getStandartDate(this.date), parser.getContent()))
            Log.d(">>>>", "Data inserted")
            this.ui?.notifyDataDownloaded()
        }else {
            val parser = IcsParser(0, this.date, res.content)
            parser.execute()
            this.events = parser.get()
        }
        notifyUI()
    }

    /**
     *
     */
    private fun loadTasks() {
        Log.d(">>>", "DB TASK BEGIN")
        val queryResult:Array<DbTask>? = this.dao.getTasks(Utils.getStandartDate(this.date))
        if(queryResult != null) {
            for(t:DbTask in queryResult) {
                Log.d(">>>>task", t.title)
                val queryTagResult:DbTag? = dao.getTag(name=t.tag)
                val tag:TagManager.Tag = if(queryTagResult == null) TagManager.unknownTag else TagManager.getTag(queryTagResult.name, queryTagResult.color)
                val task = Task(t.title, this.date, tag)
                tag.addTask(task)
                this.tasks.add(task)
            }
        }
        notifyUItasksChanged()
    }

    /**
     *
     */
    fun addUI(ui:UIcalendar) {
        this.ui = ui
    }

    /**
     *
     */
    fun update() {
        Thread {
            Log.d(">>>", "Update")
            readDatabase(forceReloadData = true)
            loadTasks()
        }.start()
    }

    /**
     *
     */
    private fun notifyUI() {
        this.ui?.notifyEventListChanged()
    }

    private fun notifyUItasksChanged() {
        this.ui?.notifyTasksChanged()
    }



}