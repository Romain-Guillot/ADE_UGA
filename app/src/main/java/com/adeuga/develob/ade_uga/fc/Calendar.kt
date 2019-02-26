package com.adeuga.develob.ade_uga.fc

import com.adeuga.develob.ade_uga.fc.db.*
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * This class represent a calendar of one day with these properties :
 * @property events list of events of this day
 * @property tasks same as events, but it's tasks
 * @property ui the ui that display the calendar data (can be null, of course)
 *
 * WARNING : ui property IS NOT part of the serialized form of the Calendar instances
 * TODO : a single UI instance can be linked, handle a list of UI objects
 */
class Calendar(var date:Date, db:AppDatabase) : Serializable {

    private var events = ArrayList<CalendarEvent>()
    private var tasks = ArrayList<Task>()
    @Transient // Not part of the serialized form
    private var ui:UIcalendar? = null


    init {
        Thread{
            loadEvents(db)
            loadTasks(db)
        }.start()
    }


    /**
     * Return the date format with a the EEEE d MMMM (ex: monday 12 juin) format (french format)
     */
    fun getDateToString() : String {
        val df = SimpleDateFormat("EEEE d MMMM")
        return df.format(date).capitalize()
    }


    /**
     * Getter for the [event] property, events are sorted before being returned
     */
    fun getEvents() : ArrayList<CalendarEvent> {
        this.events.sort()
        return this.events
    }


    /**
     * Getter for tasks
     */
    fun getTasks() : ArrayList<Task> {
        return this.tasks
    }


    /**
     * Load events from IF [forceReloadData]
     *      THEN the database
     *      ELSE the ics parser
     *  Can be slow (depends of internet connection, and database calls)
     */
    private fun loadEvents(db: AppDatabase, forceReloadData:Boolean = false) {
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
     * Load tasks from the database
     * Can be slow (database slow)
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
     * Add an UI object which implements UIcalendar interface
     */
    fun addUI(ui:UIcalendar) {
        this.ui = ui
    }


    /**
     * Update the events and tasks lists
     */
    fun update(db: AppDatabase, events:Boolean = true, tasks:Boolean = true) {
        Thread {
            if(events) loadEvents(db, forceReloadData = true)
            if(tasks) loadTasks(db)
        }.start()
    }


    /*
    * --------------------------------------------------------------------------------------------------------------
    * This function below just called UI implemented method from the UIcalendar interface, see this doc to know more
    * --------------------------------------------------------------------------------------------------------------
    */
    fun notifyUIeventsChanged() { this.ui?.notifyEventListChanged() }

    fun notifyUItasksChanged() { this.ui?.notifyTasksChanged() }

    fun notifyUIerror(msg: String) { this.ui?.notifyError(msg) }
}