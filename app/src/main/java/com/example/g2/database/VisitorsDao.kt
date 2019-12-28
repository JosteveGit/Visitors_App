package com.example.g2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VisitorsDao {

    @Query("SELECT * FROM visitors ORDER BY id DESC")
    fun getAllWords():LiveData<List<Visitors>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vistior: Visitors)

    @Query("DELETE FROM visitors")
    suspend fun deleteAll()
}