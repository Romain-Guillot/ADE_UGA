package com.adeuga.develob.ade_uga.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.adeuga.develob.ade_uga.R
import com.adeuga.develob.ade_uga.fc.Calendar
import com.adeuga.develob.ade_uga.fc.CalendarEvent
import com.adeuga.develob.ade_uga.fc.IcsParser
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : FragmentActivity() {

    private lateinit var daysPager:ViewPager
    private lateinit var daysPagerAdapter:DaysPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
        val d: Date = sdf.parse("2019/01/24")
        val initPos:Int = Int.MAX_VALUE/2

        daysPager = findViewById(R.id.daysPager)
        daysPagerAdapter = DaysPagerAdapter(supportFragmentManager, d, initPos)
        daysPager.adapter = daysPagerAdapter
        daysPager.currentItem = initPos
    }
}
