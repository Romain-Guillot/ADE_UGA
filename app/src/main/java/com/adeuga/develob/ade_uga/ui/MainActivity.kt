package com.adeuga.develob.ade_uga.ui

import android.graphics.Rect
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.room.Room
import com.adeuga.develob.ade_uga.R
import com.adeuga.develob.ade_uga.fc.Calendar
import com.adeuga.develob.ade_uga.fc.CalendarEvent
import com.adeuga.develob.ade_uga.fc.IcsParser
import com.adeuga.develob.ade_uga.fc.db.AppDatabase
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var daysPager: ViewPager
    private lateinit var daysPagerAdapter:DaysPagerAdapter
    private lateinit var settingsBottomSheetLayout:LinearLayout
    private lateinit var settingsBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var floatingAddTask:Button
    private lateinit var toolbar:BottomAppBar
    private lateinit var bg:View
    private lateinit var db:AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.bar)
        setSupportActionBar(toolbar)

        bg = findViewById(R.id.bg)

        settingsBottomSheetLayout = findViewById(R.id.settings_bottom_sheet)
        settingsBottomSheet = BottomSheetBehavior.from(settingsBottomSheetLayout)
        settingsBottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> bg.visibility = View.GONE
//                    else -> bg.visibility = View.GONE
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                bg.visibility = View.VISIBLE
            }
        })



        floatingAddTask = findViewById(R.id.fab)
        floatingAddTask.setOnClickListener{
            Log.d(">>>>","Floating")
        }

        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val d: Date = sdf.parse("2019/01/24")
        val initPos:Int = Int.MAX_VALUE/2
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "calendar").build()


        daysPager = findViewById(R.id.daysPager)
        daysPagerAdapter = DaysPagerAdapter(supportFragmentManager, d, initPos, db)
        daysPager.adapter = daysPagerAdapter
        daysPager.currentItem = initPos



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.action_settings -> {
                when(settingsBottomSheet.state) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        settingsBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
                    }

                    else -> {
                        settingsBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.d(">>>", "TOUCHE")
        if(event?.action == MotionEvent.ACTION_DOWN && settingsBottomSheet.state == BottomSheetBehavior.STATE_EXPANDED) {
            Log.d(">>>>", "TES")
            var outSheet = Rect()
            settingsBottomSheetLayout.getGlobalVisibleRect(outSheet)
            if(!outSheet.contains(event.x.toInt(), event.y.toInt())) {
                settingsBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        return super.dispatchTouchEvent(event)
    }
}
