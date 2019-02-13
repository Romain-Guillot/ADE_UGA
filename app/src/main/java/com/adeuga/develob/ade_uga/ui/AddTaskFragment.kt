package com.adeuga.develob.ade_uga.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.adeuga.develob.ade_uga.R
import com.adeuga.develob.ade_uga.fc.TagManager
import com.adeuga.develob.ade_uga.fc.Task
import com.adeuga.develob.ade_uga.fc.db.AppDatabase
import java.util.*
import kotlin.concurrent.thread

class AddTaskFragment : Fragment() {

    private var addTaskButton:Button? = null
    private lateinit var db:AppDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_task, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(">>>", "Add task fragment")

        this.db = (activity as MainActivity).db

        addTaskButton = view?.findViewById(R.id.add_task_action)

        addTaskButton?.setOnClickListener {
            val task = Task("Test", Calendar.getInstance().time, TagManager.unknownTag)
            thread {
                task.addToDatabase(this.db)
            }
        }
    }
}