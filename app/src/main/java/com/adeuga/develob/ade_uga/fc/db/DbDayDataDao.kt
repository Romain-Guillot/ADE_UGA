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
    fun insert(day: DbDayData)

    @Delete
    fun delete(day: DbDayData)

    @Query("SELECT * FROM days WHERE date == :date LIMIT 1")
    fun get(date:String) : DbDayData
}