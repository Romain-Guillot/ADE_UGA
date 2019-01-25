package com.adeuga.develob.ade_uga.fc

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedInputStream
import java.net.URL
import java.util.*
import java.lang.Exception
import java.text.SimpleDateFormat


class IcsParser constructor (val idResource:Int, val date:Date) : AsyncTask<Void, Void, Calendar>()  {

    init {
//        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//        StrictMode.setThreadPolicy(policy)

    }

    override fun doInBackground(vararg params: Void?): Calendar {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateChoosed:String = dateFormat.format(date)
        val urlStr = "https://ade6-ujf-ro.grenet.fr/jsp/custom/modules/plannings/anonymous_cal.jsp?resources=11608&projectId=7&calType=ical&firstDate=$dateChoosed&lastDate=$dateChoosed"
        Log.d(">>>Debug", urlStr)
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
        var content = String(dataBuff)
        var tmp = 0
        while(byteReaded < lenghtOfFile) {
            tmp = inBuff.read(dataBuff, 0, 1024)
            if(tmp == -1) break
            byteReaded += tmp
            content  += String(dataBuff, 0, tmp)
        }
        Log.d(">>>DEBUG", content)
        return this.buildCalendar(content)
    }


    fun buildCalendar(ics:String) : Calendar {
        Log.d(">>> DEBUG" , "BEGIN TO BUILD CALENDAR")
        val lines:Array<String> = ics.split("\n").toTypedArray()
        var i = 0
        val size = lines.size
        var calendar = Calendar()


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
            calendar.addEvent(currentEvent)
            i+=1

        }

        Log.d(">>> DEBUG", "Calendare built")
        return calendar
    }

    fun toDate(str:String) : Date {
        val dataFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
        dataFormat.timeZone = TimeZone.getTimeZone("GMT")
        val date:Date = dataFormat.parse(str.trim())
        return date
    }


}