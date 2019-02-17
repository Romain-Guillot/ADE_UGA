package com.adeuga.develob.ade_uga.fc.db

import androidx.room.*

@Dao
interface DbDayDataDao {
//    @Query("SELECT * FROM days")
//    fun getAll(): List<DbDayData>

//    @Query("SELECT * FROM days WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<DbDayData>

//    @Query("SELECT * FROM days WHERE first_name LIKE :first AND last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): DbDayData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(day: DbDayData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(event: DbTask)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertTag(event: DbTag)


    @Query("SELECT * FROM days WHERE date == :date LIMIT 1")
    fun getEvents(date:String) : DbDayData

    @Query("SELECT * FROM tasks WHERE date == :date")
    fun getTasks(date:String) : Array<DbTask>

//    @Query("SELECT * FROM tags")
//    fun getTags() : Array<DbTag>
//
//    @Query("SELECT * FROM tags WHERE name == :name")
//    fun getTag(name:String) : DbTag


    @Delete
    fun delete(day: DbDayData)
}