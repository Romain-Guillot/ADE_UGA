package com.adeuga.develob.ade_uga.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.adeuga.develob.ade_uga.fc.db.AppDatabase
import com.adeuga.develob.ade_uga.fc.Calendar
import java.util.*


/**
 * Custom FragmentPageAdapter to handle pager (slider) of DayFragment instances
 * This implementations simulate infinite vertical scrolling [0; INT_MAX]
 *
 * @property initialPosition ideally setted to INTMAX/2
 * @property initialDate initial date corresponding to [initialPosition]
 * @property db useful to define Calendar instances
 */
class DaysPagerAdapter(fm: FragmentManager, private var initialDate:Date, private var initialPosition:Int, private val db:AppDatabase) : FragmentPagerAdapter(fm) {


    /**
     * Add # days to a date
     *
     * @param d initial date
     * @param i number of days to add to [d] (negative value accepted)
     */
    private fun addDayToDate(d:Date, i:Int) : Date {
        val c = java.util.Calendar.getInstance()
        c.time = d
        c.add(java.util.Calendar.DATE, i)
        return c.time
    }

    /**
     * Get item at [position]
     * An instance of calendar is created according to its position in the pager
     */
    override fun getItem(position: Int): Fragment {
        val date:Date = addDayToDate(this.initialDate, position - this.initialPosition)
        return DayFragment.newInstance(Calendar(date, this.db))
    }


    override fun getCount(): Int = Integer.MAX_VALUE

    fun setInititalPosition(i: Int) {
        this.initialPosition = i
    }


}