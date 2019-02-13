package com.adeuga.develob.ade_uga.ui

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import androidx.viewpager.widget.ViewPager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.room.Room
import com.adeuga.develob.ade_uga.R
import com.adeuga.develob.ade_uga.fc.db.AppDatabase
import com.google.android.material.bottomappbar.BottomAppBar
import java.util.*


/**
 * Main android activity that display the FragmentPager (DaysPagerAdapter)
 */
class MainActivity : AppCompatActivity() {

    private lateinit var daysPager: ViewPager
    private lateinit var daysPagerAdapter: DaysPagerAdapter
    private lateinit var settingsBottomSheetLayout: LinearLayout
    private lateinit var settingsBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var floatingAddTask: Button
    private lateinit var toolbar: BottomAppBar
    private lateinit var bg: View
    lateinit var db: AppDatabase
    /**
     * Define views
     * Setting the bottom sheet for settings
     * Setting "add task" button to add task
     * Setting DaysPagerAdapter to display day events
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Init views */
        this.toolbar = findViewById(R.id.bar)
        this.bg = findViewById(R.id.bg)
        this.settingsBottomSheetLayout = findViewById(R.id.settings_bottom_sheet)
        this.settingsBottomSheet = BottomSheetBehavior.from(settingsBottomSheetLayout)
        this.floatingAddTask = findViewById(R.id.fab)
        this.daysPager = findViewById(R.id.daysPager)

        setSupportActionBar(toolbar)

        /* Setting "settings bottom sheet" behavior */
        this.settingsBottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                     bg.visibility = View.GONE
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                bg.visibility = View.VISIBLE
            }
        })

        /* Setting "add task" button behavior */
        this.floatingAddTask.setOnClickListener{
            Log.d(">>>>","Floating")
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.main_activity, AddTaskFragment()).addToBackStack(null).commit() // addToBackStack(null) to provide back navigation
        }

        /* Setting DaysPagerAdapter with current date */
        val currentDate: Date = java.util.Calendar.getInstance().time
        val initPos: Int = Int.MAX_VALUE/2 // initial position (negative position is impossible)
        this.db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "calendar").fallbackToDestructiveMigration().build()
        this.daysPagerAdapter = DaysPagerAdapter(supportFragmentManager, currentDate, initPos, db)
        this.daysPager.adapter = daysPagerAdapter
        this.daysPager.currentItem = initPos
    }

    /**
     * Create menu (bottom right) from menu template file menu/menu.xml
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    /**
     * This function is called when a click is detected on menu
     * Menu items :
     *     - action_settings : display bottom sheet settings
     * @param item Menu item definied in menu/menu.xml, can be indentified with its id
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_settings -> {
                when (this.settingsBottomSheet.state) {
                    BottomSheetBehavior.STATE_EXPANDED  -> this.settingsBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
                    else                                -> this.settingsBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * This function is called when a touch event is detected.
     * This function handle the bottom sheet close behavior when user click outside the sheet
     */
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        // IF user release the screnn AND the bottom sheet is open
        if (event?.action == MotionEvent.ACTION_UP && this.settingsBottomSheet.state == BottomSheetBehavior.STATE_EXPANDED) {
            val outSheet = Rect()
            this.settingsBottomSheetLayout.getGlobalVisibleRect(outSheet)
            if (!outSheet.contains(event.x.toInt(), event.y.toInt())) {
                this.settingsBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        return super.dispatchTouchEvent(event)
    }
}
