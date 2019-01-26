package com.adeuga.develob.ade_uga.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import com.adeuga.develob.ade_uga.fc.Calendar
import java.util.*
import kotlin.collections.ArrayList


class DaysPagerAdapter(fm:FragmentManager, private var initalDate:Date, var initialPosition:Int) : FragmentPagerAdapter(fm) {
    var calendarsBuffer = ArrayList<Calendar>()
    private var currentCalendar:Calendar
    private var currentBufferPosition:Int


    init {
        Log.d(">>>", "initial " + initialPosition)
        this.calendarsBuffer = ArrayList()
        for(i in -5..6) {
            calendarsBuffer.add(com.adeuga.develob.ade_uga.fc.Calendar(addDayToDate(initalDate, i)))
        }
        currentBufferPosition = 5
        currentCalendar = calendarsBuffer[currentBufferPosition]
    }

    private fun addDayToDate(d:Date, i:Int) : Date {
        var c = java.util.Calendar.getInstance()
        c.time = d
        c.add(java.util.Calendar.DATE, i)
        return c.time
    }

    override fun getItem(position: Int): Fragment {
        Log.d(">>>> GET", position.toString())
        val current:Calendar = com.adeuga.develob.ade_uga.fc.Calendar(addDayToDate(initalDate, position - initialPosition))
        return DayFragment.newInstance(current)
    }



    override fun getCount(): Int {
        return Integer.MAX_VALUE
    }

}