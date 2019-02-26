package com.adeuga.develob.ade_uga.fc

import com.adeuga.develob.ade_uga.fc.db.*
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 *
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
        var parser:IcsParser?
        if(res == null || forceReloadData) {
            parser = IcsParser(0, this.date)
            db.dao().insertEvent(DbDayData(Utils.getStandartDate(this.date), parser.getContent()))
        }else {
            parser = IcsParser(0, this.date, res.content)
        }
        parser.execute()
        val parserRes : ArrayList<CalendarEvent>? = parser.get()
        if(parserRes != null) {
            this.events = parserRes
            notifyUIeventsChanged()
        }else {
            notifyUIerror("Erreur téléchargement.")
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


    /**
     *
     */
    fun notifyUItasksChanged() {
        this.ui?.notifyTasksChanged()
    }


    fun notifyUIerror(msg: String) {
        this.ui?.notifyError(msg)
    }



}