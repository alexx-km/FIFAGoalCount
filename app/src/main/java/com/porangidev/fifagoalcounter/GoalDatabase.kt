package com.porangidev.fifagoalcounter

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.provider.ContactsContract.CommonDataKinds.Note
import android.os.AsyncTask
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.annotation.NonNull



@Database(entities = [GoalData::class], version = 4)
abstract class GoalDatabase: RoomDatabase() {

    abstract fun goalDao(): GoalDao

    companion object{
        @Volatile
        var INSTANCE: GoalDatabase? = null

        fun getInstance(context: Context): GoalDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, GoalDatabase::class.java, "goal_database")
                    .addMigrations(MIGRATION_2_3, MIGRATION_3_4).allowMainThreadQueries().addCallback(roomCallback).build()
                INSTANCE = instance
                return instance
            }
        }
        fun destroyInstance() {
            INSTANCE = null
        }
        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(INSTANCE!!).execute()
            }
        }

        private class PopulateDbAsyncTask internal constructor(db: GoalDatabase) : AsyncTask<Void, Void, Void>() {

            // private var goalDao: GoalDao = db.goalDao()

            override fun doInBackground(vararg voids: Void): Void? {
                // place for manual test entries
                //goalDao.insert(GoalData(1, 12, 7, 9, "hhhhaaaa", 397510577L, "FIFA 21"))
                return null
            }
        }
    }



}