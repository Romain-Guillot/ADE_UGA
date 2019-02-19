package com.adeuga.develob.ade_uga.fc

import java.io.Serializable


class TagManager  {


    class Tag(val name:String, val color:String) : Serializable {
        private val tasks = ArrayList<Task>()

        fun addTask(t:Task) {
            tasks.add(t)
        }
    }

    companion object {
        val tags: ArrayList<Tag> = ArrayList()
        val unknownTag: Tag = Tag("Unknonwn", "#969696")

        init {
            tags.add(Tag("Info", "#4287DA"))
            tags.add(Tag("Important", "#D0595E"))
            tags.add(Tag("TO DO", "#42DAA5"))
        }


        fun getTag(name: String) : Tag {
            for(t:Tag in tags) {
                if(t.name == name) return t
            }
            return unknownTag
        }
    }


}