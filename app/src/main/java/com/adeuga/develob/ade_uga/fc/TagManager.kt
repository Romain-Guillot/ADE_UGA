package com.adeuga.develob.ade_uga.fc

import android.graphics.Color
import com.adeuga.develob.ade_uga.fc.db.AppDatabase


class TagManager (val db:AppDatabase) {

    val tags: ArrayList<Tag> = ArrayList()

    class Tag(val name:String, val color:Color)

    init {
        Thread {
            getAllTagFromDatabase()
        }
    }

    fun getTag(tagName : String) : Tag {
        return tags[0]
    }

    fun getAllTagFromDatabase() {
        
    }

}