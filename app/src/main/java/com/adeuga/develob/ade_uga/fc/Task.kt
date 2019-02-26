package com.adeuga.develob.ade_uga.fc

import android.graphics.Color
import com.adeuga.develob.ade_uga.fc.db.AppDatabase
import com.adeuga.develob.ade_uga.fc.db.DbTask
import com.adeuga.develob.ade_uga.fc.db.Utils
import java.io.Serializable
import java.util.*


/**
 * This class represents a single task with a [title], a [date] and a [tag].
 * A task can have its [calendar] reference (nullable object)
 * The [calendar] can be null typically when the task is created (calendar not created // it's the calendar that uploads its task list)
 */
class Task(title:String?, val date:Date, private val tag:TagManager.Tag, private val calendar:Calendar?) : Serializable {

    var id: Int? = null
    val title = title?: "Unknonwn"


    /**
     * Constructor used if the task already exists in the database (so an id, the primary key, is associated)
     */
    constructor(title:String?, date:Date, tag:TagManager.Tag, calendar:Calendar, id:Int) : this(title, date, tag, calendar){
        this.id = id
    }


    /**
     * Add the task to the database [db]
     */
    fun addToDatabase(db:AppDatabase) {
        val dao = db.dao()
        val DbTask = DbTask(date = Utils.getStandartDate(this.date), title = this.title, tag = this.tag.name)
        dao.insertTask(DbTask)
        this.id = DbTask.id
        calendar?.update(db, events = false, tasks = true)
        this.calendar?.notifyUItasksChanged()
    }


    /**
     * Delete the task from the database [db]
     */
    fun deleteToDatabase(db:AppDatabase) {
        val idFinal:Int? = id
        if(idFinal != null) {
            val dao = db.dao()
            dao.deleteTask(idFinal)
        }else {
            this.calendar?.notifyUIerror("Suppression impossible.")
        }
        calendar?.update(db, events = false, tasks = true)
        calendar?.notifyUItasksChanged()

    }


    fun getBackgroundColor() : Int = Color.parseColor(this.tag.color)


    fun getTagTitle() : String = this.tag.name.toUpperCase()
}