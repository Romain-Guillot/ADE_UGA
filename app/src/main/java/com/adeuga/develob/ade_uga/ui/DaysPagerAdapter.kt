package com.adeuga.develob.ade_uga.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.util.Log
import com.adeuga.develob.ade_uga.fc.Calendar
import com.adeuga.develob.ade_uga.fc.db.AppDatabase
import java.util.*
import kotlin.collections.ArrayList


class DaysPagerAdapter(fm: FragmentManager, private var initalDate:Date, var initialPosition:Int, val db:AppDatabase) : FragmentPagerAdapter(fm) {
    var calendarsBuffer = ArrayList<Calendar>()
    private var currentCalendar:Calendar


    init {
        Log.d(">>>", "initial " + initialPosition)
        currentCalendar = com.adeuga.develob.ade_uga.fc.Calendar(initalDate, db)
    }

    private fun addDayToDate(d:Date, i:Int) : Date {
        var c = java.util.Calendar.getInstance()
        c.time = d
        c.add(java.util.Calendar.DATE, i)
        return c.time
    }

    override fun getItem(position: Int): Fragment {
        Log.d(">>>> GET", position.toString())
        val current:Calendar = com.adeuga.develob.ade_uga.fc.Calendar(addDayToDate(initalDate, position - initialPosition), db)
        return DayFragment.newInstance(current)
    }



    override fun getCount(): Int {
        return Integer.MAX_VALUE
    }

}