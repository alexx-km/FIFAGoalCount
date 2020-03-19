package com.porangidev.fifagoalcounter

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface GoalDao {

    @Insert(onConflict = REPLACE)
    fun insert (goalData: GoalData)

    @Update(onConflict = REPLACE)
    fun update (goalData: GoalData)

    @Delete
    fun delete (goalData: GoalData)

    @Query("DELETE FROM goal_table")
    fun deleteAllEntries()

    @Query("SELECT * FROM goal_table ORDER BY play_date DESC")
    fun getAllSessions(): LiveData<List<GoalData>>

    @Query("SELECT * FROM goal_table ORDER BY play_date DESC")
    fun getAllSessionsNonLive(): List<GoalData>



}