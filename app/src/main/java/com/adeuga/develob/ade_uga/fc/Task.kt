package com.adeuga.develob.ade_uga.fc

import android.graphics.Color
import com.adeuga.develob.ade_uga.fc.db.AppDatabase
import com.adeuga.develob.ade_uga.fc.db.DbTask
import com.adeuga.develob.ade_uga.fc.db.Utils
import java.util.*

class Task(val title:String, val date:Date, private val tag:TagManager.Tag) {

    fun addToDatabase(db:AppDatabase) {
        val dao = db.dao()
        val DbTask = DbTask(date = Utils.getStandartDate(this.date), title = this.title, tag = this.tag.name)
        dao.insertTask(DbTask)
    }

    fun getBackgroundColor() : Color {
        return this.tag.color
    }

    fun getTagTitle() : String {
        return this.tag.name
    }
}