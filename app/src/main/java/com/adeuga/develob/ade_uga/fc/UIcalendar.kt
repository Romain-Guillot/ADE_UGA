package com.adeuga.develob.ade_uga.fc


/**
 * Interface that UI component which want to display calendar events should implements
 * It's to handle notification from the Calendar
 */
interface UIcalendar {

    /**
     * Called if events list changed
     */
    fun notifyEventListChanged()


    /**
     * Called if data downloading succeed
     */
    fun notifyDataDownloaded()


    /**
     * Called if tasks list changed
     */
    fun notifyTasksChanged()


    /**
     * Called with a [msg] if an error occurred
     */
    fun notifyError(msg:String)
}