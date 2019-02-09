package com.adeuga.develob.ade_uga.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.adeuga.develob.ade_uga.R
import com.adeuga.develob.ade_uga.fc.Calendar
import com.adeuga.develob.ade_uga.fc.CalendarEvent
import com.adeuga.develob.ade_uga.fc.UIcalendar
import kotlin.collections.ArrayList


/**
 *  Custom fragment to display calendar events (notably used in DaysPager)
 *  Implement UIcalendar interface to handle notifications
 */
class DayFragment : Fragment(), UIcalendar {

    private lateinit var eventsView: RecyclerView
    private lateinit var eventsAdapter: EventsViewAdapter
    private var titleView: TextView? = null
    private var calendar: Calendar? = null
    private var refreshLayout: SwipeRefreshLayout? = null

    /**
     *
     */
    companion object {
        const val DAYFRAGMENT_ARG = "DAYFRAGMENTARGS"

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

    /**
     * (re)define calendar when activity is created from serialized arguments (constructors not allowed)
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.titleView = view?.findViewById(R.id.dayViewTitle)
        this.calendar = arguments?.getSerializable(DayFragment.DAYFRAGMENT_ARG) as Calendar //deserialize calendar from args
        this.calendar?.addUI(this)
        setRefreshLayout()
        setEventsList()
    }

    /**
     * Set events from calendar attached to the fragment in the recycler list
     */
    private fun setEventsList() {
        if (calendar != null) {
            this.titleView?.text = calendar?.getDateToString()
            val events : ArrayList<CalendarEvent>? = this.calendar?.getEvents() // get events

            if (events != null) { // set recycler list with events
                val view:View? = view
                if (view != null) {
                    this.eventsAdapter = EventsViewAdapter(events)
                    this.eventsView = view.findViewById<RecyclerView>(R.id.dayViewListView).apply {
                            setHasFixedSize(true)
                            layoutManager = LinearLayoutManager(this.context)
                            adapter = eventsAdapter
                        }
                }
            } else {
                Log.d(">>>", "No events...")
            }
        }
    }

    /**
     *  Update calendar attached to the fragment
     */
    private fun updateCalendar() {
        this.calendar?.update()
    }

    /**
     *  Setting refresh layout behavior (update calender on scroll)
     */
    private fun setRefreshLayout() {
        this.refreshLayout = view?.findViewById(R.id.refreshLayout)
        this.refreshLayout?.setOnRefreshListener {
            updateCalendar()
        }
    }

    /**
     *  UIcalendar interface function
     *  Event occured when calendar notify that events list changed
     */
    override fun notifyEventListChanged() {
        activity?.runOnUiThread {
            setEventsList()
            this.refreshLayout?.isRefreshing = false
        }
    }

    /**
     *  UIcalendar interface function
     *  Events occured when calendar notify that new data are downloaded
     */
    override fun notifyDataDownloaded() {
        activity?.runOnUiThread {
            Toast.makeText(context, "Mise à jour réussie", Toast.LENGTH_SHORT).show()
        }
    }



}