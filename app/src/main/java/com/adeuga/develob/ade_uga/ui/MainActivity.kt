package com.adeuga.develob.ade_uga.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.adeuga.develob.ade_uga.R
import com.adeuga.develob.ade_uga.fc.CalendarEvent
import com.adeuga.develob.ade_uga.fc.IcsParser
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var eventsView:RecyclerView
    private lateinit var eventsAdapter: EventsViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(">>>>>>>>>>>>>>>>>>DEBUG","Appel du parser")

        val sdf:SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
        val d:Date = sdf.parse("2019/01/24")
        val parser = IcsParser(0, d)

        parser.execute()
        var events:ArrayList<CalendarEvent> = parser.get().getEvents()
        for (event in events) {
            Log.d(">>>DEBUG", event.toString())
        }

        eventsAdapter = EventsViewAdapter(events)
        eventsView = findViewById<RecyclerView>(R.id.dayViewListView).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
            adapter = eventsAdapter
        }

    }
}
