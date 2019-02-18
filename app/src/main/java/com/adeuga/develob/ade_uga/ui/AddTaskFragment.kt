package com.adeuga.develob.ade_uga.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.adeuga.develob.ade_uga.R
import com.adeuga.develob.ade_uga.fc.Task
import com.adeuga.develob.ade_uga.fc.db.AppDatabase
import com.google.android.material.chip.ChipGroup
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


class AddTaskFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var addTaskButton:Button? = null
    private var editTextTitle: EditText? = null
    private var chipTagGroup: ChipGroup? = null
    private var tagChipsAdapter: TagChipsAdapter? = null
    private var selectDate: Button? = null
    private lateinit var selectedDate: Calendar

    private lateinit var datePicker: DatePickerDialog
    private lateinit var db:AppDatabase


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_task, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.selectedDate = Calendar.getInstance()

        this.db = (activity as MainActivity).db
        this.datePicker = DatePickerDialog(this.context, this, this.selectedDate.get(Calendar.YEAR), this.selectedDate.get(Calendar.MONTH), this.selectedDate.get(Calendar.DAY_OF_MONTH))

        this.addTaskButton = view?.findViewById(R.id.add_task_action)
        this.editTextTitle = view?.findViewById(R.id.addtask_title)
        this.chipTagGroup = view?.findViewById(R.id.addtask_tagchipgroup)
        this.selectDate = view?.findViewById(R.id.addtask_date)

        addTaskButton?.setOnClickListener {
            processAddTask()
        }

        selectDate?.setOnClickListener {
            this.datePicker.show()
        }

        setTagList()
        setSelectedDate(this.selectedDate.get(Calendar.YEAR), this.selectedDate.get(Calendar.MONTH), this.selectedDate.get(Calendar.DAY_OF_MONTH))
    }

    private fun setTagList() {
        val ctx : Context? = this.context
        val chipsGroup : ChipGroup? = this.chipTagGroup
        if(ctx != null && chipsGroup != null) {
            this.tagChipsAdapter = TagChipsAdapter(chipsGroup, ctx)
        }
    }

    private fun processAddTask() {
        val title:String? = editTextTitle?.text?.toString()
        if(title == null || title.isEmpty()) {
            Toast.makeText(this.context, "Veuillez saisir un titre.", Toast.LENGTH_SHORT).show()
        }else {
            val tagChipsAdapterFinal = this.tagChipsAdapter
            if (tagChipsAdapterFinal != null) {
                val task = Task(title, this.selectedDate.time, tagChipsAdapterFinal.getCheckedTag(), null)
                thread {
                    task.addToDatabase(this.db)
                }
                Toast.makeText(this.context, "Tache ajoutée !", Toast.LENGTH_LONG).show()
                (activity as MainActivity).notifyTasksChanged()
                this.fragmentManager?.popBackStack()
            }else {
                Toast.makeText(this.context, "Erreur interne", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        setSelectedDate(year, month, dayOfMonth)
    }

    private fun setSelectedDate(year: Int, month: Int, dayOfMonth: Int) {
        val df = SimpleDateFormat("EEEE d MMMM")
        this.selectedDate.set(year, month, dayOfMonth)
        this.selectDate?.text = df.format(this.selectedDate.time)
    }
}