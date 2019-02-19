package com.adeuga.develob.ade_uga.fc

import com.adeuga.develob.ade_uga.fc.db.*
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * One calendar = 1 day
 */
class Calendar(var date:Date, db:AppDatabase) : Serializable {

    private var events = ArrayList<CalendarEvent>()
    private var tasks = ArrayList<Task>()
    @Transient
    private var ui:UIcalendar? = null

    init {
        Thread{
            readDatabase(db)
            loadTasks(db)
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
    private fun readDatabase(db: AppDatabase, forceReloadData:Boolean = false) {
        val res:DbDayData? = db.dao().getEvents(Utils.getStandartDate(this.date))
        if(res == null || forceReloadData) {
            val parser = IcsParser(0, this.date)
            parser.execute()
            this.events = parser.get()
            db.dao().insertEvent(DbDayData(Utils.getStandartDate(this.date), parser.getContent()))
            this.ui?.notifyDataDownloaded()
        }else {
            val parser = IcsParser(0, this.date, res.content)
            parser.execute()
            this.events = parser.get()
            notifyUIeventsChanged()
        }
    }

    /**
     *
     */
    private fun loadTasks(db: AppDatabase) {
        this.tasks = ArrayList()
        val queryResult:Array<DbTask>? = db.dao().getTasks(Utils.getStandartDate(this.date))
        if(queryResult != null) {
            for(t:DbTask in queryResult) {
                val tag: TagManager.Tag = TagManager.getTag(t.tag)
                val task = Task(t.title, this.date, tag, this, t.id)
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
    fun update(db: AppDatabase, events:Boolean = true, tasks:Boolean = true) {
        Thread {
            if(events) readDatabase(db, forceReloadData = true)
            if(tasks) loadTasks(db)
        }.start()
    }

    /**
     *
     */
    fun notifyUIeventsChanged() {
        this.ui?.notifyEventListChanged()
    }

    fun notifyUItasksChanged() {
        this.ui?.notifyTasksChanged()
    }



}