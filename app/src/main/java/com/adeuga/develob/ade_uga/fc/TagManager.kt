package com.adeuga.develob.ade_uga.fc

import java.io.Serializable


/**
 *
 */
class TagManager  {


    /**
     * Inner class to represent a tag entity
     */
    class Tag(val name:String, val color:String) : Serializable {
        private val tasks = ArrayList<Task>()

        @Deprecated("For now not used by other component, so useless...")
        fun addTask(t:Task) {
            tasks.add(t)
        }
    }


    /**
     * TagManager only defined by its companion object (= static in Java)
     * Some Tag are created when companion object is initialized
     *
     * TODO TagManager and tag system more generally can be strongly improved, for example handle tag with the database,
     * possibility to add, delete, modify tag with custom colors, etc.
     */
    companion object {
        val tags: ArrayList<Tag> = ArrayList()
        val unknownTag: Tag = Tag("Unknonwn", "#969696")

        init {
            // default tags
            tags.add(Tag("Info", "#4287DA"))
            tags.add(Tag("Important", "#D0595E"))
            tags.add(Tag("TO DO", "#42DAA5"))
        }


        /**
         * Get a tag from its name, or the unknown tag is returned
         */
        fun getTag(name: String) : Tag {
            for(t:Tag in tags) {
                if(t.name == name) return t
            }
            return unknownTag
        }
    }


}