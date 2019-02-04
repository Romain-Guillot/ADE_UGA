package com.adeuga.develob.ade_uga.fc.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adeuga.develob.ade_uga.fc.CalendarEvent
import java.sql.Date
import java.util.*

@Entity(tableName = "days")
data class DbDayData(
    @PrimaryKey var uid: Int,
    @ColumnInfo(name = "date") var date: String?,
    @ColumnInfo(name = "content") var lastName: String?
)