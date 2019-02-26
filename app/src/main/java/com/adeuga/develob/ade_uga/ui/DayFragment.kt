package com.adeuga.develob.ade_uga.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.adeuga.develob.ade_uga.R
import com.adeuga.develob.ade_uga.fc.Calendar
import com.adeuga.develob.ade_uga.fc.CalendarEvent
import com.adeuga.develob.ade_uga.fc.Task
import com.adeuga.develob.ade_uga.fc.UIcalendar
import com.adeuga.develob.ade_uga.fc.db.AppDatabase
import kotlin.collections.ArrayList


/**
 *  Custom fragment to display calendar events (notably used in DaysPager)
 *  Implement UIcalendar interface to handle notifications
 */
class DayFragment : Fragment(), UIcalendar {

    private var eventsView: RecyclerView? = null
    private var eventsAdapter: EventsViewAdapter? = null

    private var tasksView: RecyclerView? = null
    private var tasksAdapter: TasksViewAdapter? = null

    private var titleView: TextView? = null
    private var calendar: Calendar? = null
    private var refreshLayout: SwipeRefreshLayout? = null

    /**
     * Create a new instance of the fragment (if not exist) with argument (calendar) bundled
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
        this.tasksView = view?.findViewById(R.id.dayTasksListView)
        this.eventsView = view?.findViewById(R.id.dayViewListView)
        this.refreshLayout = view?.findViewById(R.id.refreshLayout)

        this.calendar = arguments?.getSerializable(DayFragment.DAYFRAGMENT_ARG) as Calendar //deserialize calendar from args
        this.calendar?.addUI(this)

        setRefreshLayout()
        setEventsList()
        setTasksList()
    }

    /**
     * Set events from calendar attached to the fragment in the recycler list
     */
    private fun setEventsList() {
        if (calendar != null) {
            this.titleView?.text = calendar?.getDateToString()
            val events : ArrayList<CalendarEvent>? = this.calendar?.getEvents() // getEvents events

            if (events != null) { // set recycler list with events
                val view:View? = view
                if (view != null) {
                    this.eventsAdapter = EventsViewAdapter(events)
                    this.eventsView?.apply {
                            setHasFixedSize(true)
                            layoutManager = LinearLayoutManager(this.context)
                            adapter = eventsAdapter
                        }
                }
            } else {
            }
        }else {
        }
    }

    /**
     * Set tasks from calendar attached to the fragment in the recycler list
     */
    private fun setTasksList() {
        val tasks:ArrayList<Task>? = this.calendar?.getTasks()
        if(tasks != null) {
            this.tasksAdapter = TasksViewAdapter(tasks, (activity as MainActivity).db)
            this.tasksView?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this.context)
                adapter = tasksAdapter
            }
        }
    }

    /**
     *  Update calendar attached to the fragment
     */
    fun updateCalendar(db: AppDatabase, events:Boolean = true, tasks:Boolean = true) {
        this.calendar?.update(db, events=events, tasks=tasks)
    }

    /**
     *  Setting refresh layout behavior (update calender on scroll)
     */
    private fun setRefreshLayout() {
        val db = (activity as MainActivity).db
        this.refreshLayout?.setOnRefreshListener {
            updateCalendar(db)
        }
    }

    /**
     *  UIcalendar interface function
     *  Event occur when calendar notify that events list changed
     */
    override fun notifyEventListChanged() {
        Log.d(">>>", "YES")
        activity?.runOnUiThread {
            Log.d(">>>", "YES2")
            setEventsList()
            this.refreshLayout?.isRefreshing = false
            Toast.makeText(context, "Succès.", Toast.LENGTH_SHORT).show()
            Log.d(">>>", "YES3")
        }
    }

    /**
     *  UIcalendar interface function
     *  Events occur when calendar notify that new data are downloaded
     */
    override fun notifyDataDownloaded() {
        activity?.runOnUiThread {
            setEventsList()
            this.refreshLayout?.isRefreshing = false
            Toast.makeText(context, "Mise à jour réussie", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * UIcalendar interface function
     * Events occur when calender notify that tasks list changed (and so load)
     */
    override fun notifyTasksChanged() {
        activity?.runOnUiThread {
            setTasksList()
            this.refreshLayout?.isRefreshing = false
        }
    }

    /**
     * UIcalendar interface function
     * Events occur when calendar notify that an error occurred
     */
    override fun notifyError(msg: String) {
        activity?.runOnUiThread {
            this.refreshLayout?.isRefreshing = false
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
}