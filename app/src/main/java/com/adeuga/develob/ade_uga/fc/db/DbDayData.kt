package com.adeuga.develob.ade_uga.fc.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adeuga.develob.ade_uga.fc.CalendarEvent
import java.sql.Date
import java.util.*

@Entity(tableName = "days", primaryKeys = arrayOf("date"))
data class DbDayData(
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "content") var content: String
)

@Entity(tableName = "tasks")
data class DbTask(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name="tag") var tag: String
)

@Entity(tableName = "tags", primaryKeys = arrayOf("name"))
data class DbTag(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "color") var color: String
)

