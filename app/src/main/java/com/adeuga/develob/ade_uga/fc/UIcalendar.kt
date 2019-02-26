package com.adeuga.develob.ade_uga.fc

interface UIcalendar {
    fun notifyEventListChanged()

    fun notifyDataDownloaded()

    fun notifyTasksChanged()

    fun notifyError(msg:String)
}