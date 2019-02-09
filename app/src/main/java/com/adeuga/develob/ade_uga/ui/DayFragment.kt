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
import java.util.*

import kotlin.collections.ArrayList


class DayFragment : Fragment(), UIcalendar {


    private lateinit var eventsView: RecyclerView
    private lateinit var eventsAdapter: EventsViewAdapter
    public var calendar: Calendar? = null
    private var refreshLayout: SwipeRefreshLayout? = null


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
//        val view:View = inflater.inflate(R.layout.day_fragment, container, false)
//        this.refreshLayout = view.findViewById(R.id.refreshLayout)

        return inflater.inflate(R.layout.day_fragment, container, false)


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setRefreshLayout()

        this.calendar = arguments?.getSerializable(DayFragment.DAYFRAGMENT_ARG) as Calendar
        this.calendar?.setFragment(this)
        setEventsList()

    }



    fun setEventsList() {
        refreshLayout?.isRefreshing = false

        activity?.runOnUiThread {

            if(calendar != null) {
                view?.findViewById<TextView>(R.id.dayViewTitle)?.text = calendar?.getDateToString()

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
    }

    fun updateCalendar() {
        Log.d(">>>", "UI update")
//        (activity as MainActivity).daysPagerAdapter.getItem((activity as MainActivity).daysPager.currentItem) as DayFragment
        this.calendar?.update()
    }

    private fun setRefreshLayout() {
        this.refreshLayout = view?.findViewById(R.id.refreshLayout)
        Log.d(">>>", "set refresh layout " + refreshLayout.toString())
        refreshLayout?.setOnRefreshListener {
            Log.d(">>>>", "OKKKKKK")
            updateCalendar()
        }
    }

    override fun notifyEventListChanged() {
        this.setEventsList()
    }

    override fun notifyDataDownloaded() {
        activity?.runOnUiThread{
            Toast.makeText(context, "Mise à jour réussie", Toast.LENGTH_SHORT).show()
        }
    }



}