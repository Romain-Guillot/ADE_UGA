package com.adeuga.develob.ade_uga.fc

import android.os.AsyncTask
import java.io.BufferedInputStream
import java.net.URL
import java.util.*
import java.lang.Exception
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList


class IcsParser(val idResource:Int, val date:Date) : AsyncTask<Void, Void, ArrayList<CalendarEvent>>()  {

    constructor(idResource: Int, date : Date, content:String) : this(idResource, date) {
        this.content = content
    }

    private var content : String? = null


    override fun doInBackground(vararg params: Void?): ArrayList<CalendarEvent> {
        val c : String? = this.content
        if(c == null ) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val dateChoosed:String = dateFormat.format(date)
            val urlStr = "https://ade6-ujf-ro.grenet.fr/jsp/custom/modules/plannings/anonymous_cal.jsp?resources=11608&projectId=7&calType=ical&firstDate=$dateChoosed&lastDate=$dateChoosed"
            val url = URL(urlStr)
            val conection = url.openConnection()
            conection.connect()
            val lenghtOfFile:Int = conection.contentLength

            val inBuff = BufferedInputStream(url.openStream())
            var dataBuff = ByteArray(1024)

            var byteReaded = inBuff.read(dataBuff, 0, 1024)
            if(byteReaded == -1) {
                //error
                throw Exception()
            }
            var content = String(dataBuff,0, byteReaded)
            var tmp = 0
            while(byteReaded < lenghtOfFile) {
                tmp = inBuff.read(dataBuff, 0, 1024)
                if(tmp == -1) break
                byteReaded += tmp
                content  += String(dataBuff, 0, tmp)
            }
            this.content = content
            return this.buildCalendar(content)
        } else {
            return this.buildCalendar(c)

        }


    }


    fun buildCalendar(ics:String) :ArrayList<CalendarEvent> {
        val lines:Array<String> = ics.split("\n").toTypedArray()
        var i = 0
        val size = lines.size
        var events = ArrayList<CalendarEvent>()

        while(i<size) {

            while(i<size) {
                i+=1
                if(lines[i-1].trim() == "BEGIN:VEVENT")
                    break
            }
            if(i>=size)
                break

            var currentEvent = CalendarEvent()
            var type = ""
            var content = ""
            while(i<size && lines[i].trim() != "END:VEVENT") { // Loop over event attributes

                val l: String = lines[i]
                val lToArray: Array<String> = l.split(":", limit = 2).toTypedArray()

                when (lToArray[0].trim()) {
                    "DTSTART" ->     {type="start"
                                    content = lToArray[1].trim()}
                    "DTEND" ->      {type="end"
                                    content = lToArray[1].trim()}
                    "SUMMARY" ->    {type="title"
                                    content = lToArray[1].trim()}
                    "LOCATION" ->   {type="location"
                                    content = lToArray[1].trim()}
                    "DESCRIPTION" -> {type="description"
                                     content = lToArray[1].trim()}
                    "STATUS", "CATEGORIES", "TRANSP", "SEQUENCE", "UID", "CREATED", "LAST-MODIFIED", "DTSTAMP" -> type="unknown"

                    else -> {
                        when(type) {
                            "title", "description", "location" -> content += l
                        }
                    }
                }
                when(type) {
                    "start" -> currentEvent.begin = toDate(content)
                    "end" -> currentEvent.end = toDate(content)
                    "title" -> currentEvent.title = content
                    "description" -> currentEvent.description = content
                    "location" -> currentEvent.location = content
                }
                i+=1
            }
            events.add(currentEvent)
            i+=1

        }

        return events
    }

    fun toDate(str:String) : Date {
        val dataFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
        dataFormat.timeZone = TimeZone.getTimeZone("GMT")
        val date:Date = dataFormat.parse(str.trim())
        return date
    }

    fun getContent() : String {
        val c = this.content?:""
        return c
    }


}