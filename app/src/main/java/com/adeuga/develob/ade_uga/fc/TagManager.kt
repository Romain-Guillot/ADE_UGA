package com.adeuga.develob.ade_uga.fc

import android.graphics.Color


class TagManager {


    class Tag(val name:String, val color:Color) {
        private val tasks = ArrayList<Task>()

        fun addTask(t:Task) {
            tasks.add(t)
        }
    }

    companion object {
        private val tags: ArrayList<Tag> = ArrayList()

        val unknownTag:Tag = Tag("Unknonwn", Color())

        fun getTag(name:String, color:String) : Tag {
            for(t:Tag in tags) {
                if(t.name == name) return t
            }
            val newTag = Tag(name, Color())
            tags.add(newTag)
            return newTag
        }
    }


}