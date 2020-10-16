package com.porangidev.fifagoalcounter

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Entity(tableName = "goal_table")
data class GoalData(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "goals_alex") var goalsAlex: Int,
    @ColumnInfo(name = "goals_hendrik") var goalsHendrik: Int,
    @ColumnInfo(name = "played_games") var playedGames: Int,
    @ColumnInfo(name = "goals_progress") var goalsProgress: String,
    @ColumnInfo(name = "play_date") var playDate: Long,
    @ColumnInfo(name = "fifa_version", defaultValue = "FIFA 20") var fifa_version: String
)

{
    fun getGoals(): Int {
        return goalsAlex + goalsHendrik
    }

    fun getAlexQuota() : Double {
        return goalsAlex/playedGames.toDouble()
    }

    fun getHendrikQuota() : Double {
        return goalsHendrik/playedGames.toDouble()
    }


    constructor() : this(null, 0, 0,0, "", 0L, "")
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE goal_table ADD COLUMN fifa_version TEXT NOT NULL DEFAULT 'FIFA 20'")
        /* process to migrate to new room version as seen here
        https://developer.android.com/training/data-storage/room/migrating-db-versions#kotlin
        database.execSQL("""
                CREATE TABLE new_goal_table (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    goals_alex INTEGER,
                    goals_hendrik INTEGER,
                    played_games INTEGER,
                    goals_progress TEXT,
                    play_date INTEGER,
                    fifa_version TEXT DEFAULT 'FIFA 21'
                )
                """)
        database.execSQL("""
                INSERT INTO new_goal_table (id, goals_alex, goals_hendrik, played_games, goals_progress, play_date, fifa_version)
                SELECT id, goals_alex, goals_hendrik, played_games, goals_progress, play_date FROM goal_table
                """)
        database.execSQL("DROP TABLE goal_table")
        database.execSQL("ALTER TABLE new_goal_table RENAME TO goal_table")*/
    }
}

