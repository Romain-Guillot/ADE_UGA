package com.adeuga.develob.ade_uga.fc

import android.graphics.Color

class Task(val title:String, tag:String, val tagManager:TagManager) {

    private val tag:TagManager.Tag = tagManager.getTag(tag)

    fun getBackgroundColor() : Color {
        return this.tag.color
    }

    fun getTagTitle() : String {
        return this.tag.name
    }
}