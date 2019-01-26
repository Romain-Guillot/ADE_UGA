package com.adeuga.develob.ade_uga.ui

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.adeuga.develob.ade_uga.R
import com.adeuga.develob.ade_uga.fc.CalendarEvent

class EventsViewAdapter(private val eventsList:ArrayList<CalendarEvent>) : RecyclerView.Adapter<EventsViewAdapter.MyViewHolder>() {

    class MyViewHolder constructor (v:View) : RecyclerView.ViewHolder(v)  {
        var title:TextView = v.findViewById(R.id.events_title)
        var description:TextView = v.findViewById(R.id.events_description)
        var begin:TextView = v.findViewById(R.id.events_begin)
        var end:TextView = v.findViewById(R.id.events_end)
        var location:TextView = v.findViewById(R.id.events_location)
    }

    override
    fun onBindViewHolder(viewHolder: MyViewHolder, position : Int) {
        val event: CalendarEvent = eventsList[position]
        viewHolder.title.text = event.title
        viewHolder.description.text = event.description?.replace("\\n", "\n")?.trim()
        viewHolder.begin.text = event.getBeginHour()
        viewHolder.end.text = event.getEndHour()
        viewHolder.location.text = event.location
    }

    override
    fun getItemCount(): Int {
        return eventsList.size
    }

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater:LayoutInflater = LayoutInflater.from(parent.context)
        val eventView:View = inflater.inflate(R.layout.calendar_event, parent, false)
        return MyViewHolder(eventView)
    }


}