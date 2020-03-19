package com.porangidev.fifagoalcounter

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "goal_table")
data class GoalData(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "goals_alex") var goalsAlex: Int,
    @ColumnInfo(name = "goals_hendrik") var goalsHendrik: Int,
    @ColumnInfo(name = "played_games") var playedGames: Int,
    @ColumnInfo(name = "goals_progress") var goalsProgress: String,
    @ColumnInfo(name = "play_date") var playDate: Long
) {

    fun getGoals(): Int {
        return goalsAlex + goalsHendrik
    }

    fun getAlexQuota() : Double {
        return goalsAlex/playedGames.toDouble()
    }

    fun getHendrikQuota() : Double {
        return goalsHendrik/playedGames.toDouble()
    }


    constructor() : this(null, 0, 0,0, "", 0L)
}

