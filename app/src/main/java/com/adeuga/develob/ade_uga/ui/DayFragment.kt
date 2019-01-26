package com.adeuga.develob.ade_uga.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.adeuga.develob.ade_uga.R
import com.adeuga.develob.ade_uga.fc.Calendar
import com.adeuga.develob.ade_uga.fc.CalendarEvent

import kotlin.collections.ArrayList


class DayFragment : Fragment() {

    private lateinit var eventsView: RecyclerView
    private lateinit var eventsAdapter: EventsViewAdapter
    public var calendar: Calendar? = null

    companion object {
        val DAYFRAGMENT_ARG = "DAYFRAGMENTARGS"

        fun newInstance(c:Calendar) : DayFragment {
            val args = Bundle()
            args.putSerializable(DAYFRAGMENT_ARG, c)
            val frag = DayFragment()
            frag.arguments = args
            return frag

        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        return inflater.inflate(R.layout.day_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.calendar = arguments?.getSerializable(DayFragment.DAYFRAGMENT_ARG) as Calendar
        if(calendar != null) {
            view?.findViewById<TextView>(R.id.dayViewTitle)?.text = calendar?.date.toString()

            var events : ArrayList<CalendarEvent>? = this.calendar?.getEvents()
            if(events != null) {
//            for (event in events) {
//                Log.d(">>>DEBUG", event.toString())
//            }

                val view:View? = view

                if(view != null) {
                    eventsAdapter = EventsViewAdapter(events)
                    eventsView = view.findViewById<RecyclerView>(R.id.dayViewListView)
                        .apply {
                            setHasFixedSize(true)
                            layoutManager = LinearLayoutManager(this.context)
                            adapter = eventsAdapter
                        }
                }
            }else {
                Log.d(">>>", "No events...")
            }
        }




    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}