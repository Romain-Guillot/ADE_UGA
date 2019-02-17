package com.adeuga.develob.ade_uga.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adeuga.develob.ade_uga.fc.Task
import com.adeuga.develob.ade_uga.R

/**
 *
 */
class TasksViewAdapter(val tasks: ArrayList<Task>) : RecyclerView.Adapter<TasksViewAdapter.MyViewHolder>() {

    /**
     *
     */
    class MyViewHolder constructor(v:View) : RecyclerView.ViewHolder(v) {
        var title: TextView = v.findViewById(R.id.task_title)
        var tagName: TextView = v.findViewById(R.id.task_tag_name)
    }

    /**
     *
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task: Task = this.tasks[position]
        holder.title.text = task.title
        holder.tagName.text = task.getTagTitle()

        holder.itemView.setBackgroundColor(task.getBackgroundColor())
    }

    /**
     *
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val eventView: View = inflater.inflate(R.layout.calendar_task, parent, false)
        return TasksViewAdapter.MyViewHolder(eventView)
    }


    /**
     *
     */
    override fun getItemCount(): Int = this.tasks.size


}