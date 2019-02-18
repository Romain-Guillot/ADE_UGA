package com.adeuga.develob.ade_uga.fc

import android.graphics.Color
import android.util.Log
import com.adeuga.develob.ade_uga.fc.db.AppDatabase
import com.adeuga.develob.ade_uga.fc.db.DbTask
import com.adeuga.develob.ade_uga.fc.db.Utils
import java.util.*


class Task(title:String?, val date:Date, private val tag:TagManager.Tag, private val calendar:Calendar?) {

    constructor(title:String?, date:Date, tag:TagManager.Tag, calendar:Calendar, id:Int) : this(title, date, tag, calendar){
        this.id = id
    }

    var id: Int? = null

    val title = title?: "Unknonwn"

    fun addToDatabase(db:AppDatabase) {
        val dao = db.dao()
        val DbTask = DbTask(date = Utils.getStandartDate(this.date), title = this.title, tag = this.tag.name)
        dao.insertTask(DbTask)
        this.id = DbTask.id
        this.calendar?.notifyUItasksChanged()
    }

    fun deleteToDatabase(db:AppDatabase) {
        val idFinal:Int? = id
        if(idFinal != null) {
            val dao = db.dao()
            dao.deleteTask(idFinal)
        }else {
            Log.d(">>>>", "Impossible de delete")
        }
        calendar?.update(events = false, tasks = true)
        calendar?.notifyUItasksChanged()

    }

    fun getBackgroundColor() : Int {
        return Color.parseColor(this.tag.color)
    }

    fun getTagTitle() : String {
        return this.tag.name.toUpperCase()
    }
}