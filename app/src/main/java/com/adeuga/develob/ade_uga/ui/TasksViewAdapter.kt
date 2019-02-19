package com.adeuga.develob.ade_uga.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adeuga.develob.ade_uga.fc.Task
import com.adeuga.develob.ade_uga.R
import com.adeuga.develob.ade_uga.fc.db.AppDatabase


/**
 * Custom RecyclerView Adapter to handle calendar task list
 *
 * @property tasks tasks to display
 * @property db database
 */
class TasksViewAdapter(val tasks: ArrayList<Task>, val db:AppDatabase) : RecyclerView.Adapter<TasksViewAdapter.MyViewHolder>() {

    /**
     *  Define a graphical representation of a task
     */
    class MyViewHolder constructor(v:View) : RecyclerView.ViewHolder(v) {
        var title: TextView = v.findViewById(R.id.task_title)
        var tagName: TextView = v.findViewById(R.id.task_tag_name)
    }


    /**
     * Bind graphical item task with real task object
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task: Task = this.tasks[position]
        holder.title.text = task.title
        holder.tagName.text = task.getTagTitle()
        holder.itemView.setBackgroundColor(task.getBackgroundColor())
        holder.itemView.findViewById<ImageButton>(R.id.task_delete).setOnClickListener{
            Thread {
                tasks[position].deleteToDatabase(this.db)
            }.start()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val eventView: View = inflater.inflate(R.layout.calendar_task, parent, false)
        return TasksViewAdapter.MyViewHolder(eventView)
    }


    override fun getItemCount(): Int = this.tasks.size


}