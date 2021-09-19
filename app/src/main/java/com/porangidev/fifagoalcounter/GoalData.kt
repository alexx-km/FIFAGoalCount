package com.porangidev.fifagoalcounter

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Entity(tableName = "goal_table")
data class GoalData(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "goals_player1") var goalsPlayer1: Int, //Player1
    @ColumnInfo(name = "goals_player2") var goalsPlayer2: Int, //Player2
    @ColumnInfo(name = "played_games") var playedGames: Int,
    @ColumnInfo(name = "goals_progress") var goalsProgress: String,
    @ColumnInfo(name = "play_date") var playDate: Long,
    @ColumnInfo(name = "fifa_version", defaultValue = "FIFA 20") var fifa_version: String
)

{
    fun getGoals(): Int {
        return goalsPlayer1 + goalsPlayer2
    }

    fun getPlayer1Quota() : Double {
        return goalsPlayer1/playedGames.toDouble()
    }

    fun getPlayer2Quota() : Double {
        return goalsPlayer2/playedGames.toDouble()
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
                    goals_player1 INTEGER,
                    goals_player2 INTEGER,
                    played_games INTEGER,
                    goals_progress TEXT,
                    play_date INTEGER,
                    fifa_version TEXT DEFAULT 'FIFA 21'
                )
                """)
        database.execSQL("""
                INSERT INTO new_goal_table (id, goals_player1, goals_player2, played_games, goals_progress, play_date, fifa_version)
                SELECT id, goals_player1, goals_player2, played_games, goals_progress, play_date FROM goal_table
                """)
        database.execSQL("DROP TABLE goal_table")
        database.execSQL("ALTER TABLE new_goal_table RENAME TO goal_table")*/
    }
}
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        //database.execSQL("ALTER TABLE goal_table RENAME COLUMN goals_alex TO goals_player1")
        //database.execSQL("ALTER TABLE goal_table RENAME COLUMN goals_hendrik TO goals_player2")
        database.execSQL("""
                CREATE TABLE new_goal_table (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    goals_player1 INTEGER NOT NULL,
                    goals_player2 INTEGER NOT NULL,
                    played_games INTEGER NOT NULL,
                    goals_progress TEXT NOT NULL, 
                    play_date INTEGER NOT NULL,
                    fifa_version TEXT NOT NULL DEFAULT 'FIFA 20'
                )
                """)
        database.execSQL("""
                INSERT INTO new_goal_table (id, goals_player1, goals_player2, played_games, goals_progress, play_date, fifa_version)
                SELECT id, goals_alex, goals_hendrik, played_games, goals_progress, play_date, fifa_version FROM goal_table
                """)
        database.execSQL("DROP TABLE goal_table")
        database.execSQL("ALTER TABLE new_goal_table RENAME TO goal_table")
    }
}

