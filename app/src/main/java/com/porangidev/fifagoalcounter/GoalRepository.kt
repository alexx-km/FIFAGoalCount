package com.porangidev.fifagoalcounter

import android.app.Application
import android.os.AsyncTask
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import android.provider.ContactsContract.CommonDataKinds.Note



class GoalRepository(private var goalDao: GoalDao) {
    var allEntries: LiveData<List<GoalData>> = goalDao.getAllSessions()
    var allEntriesNonLive: List<GoalData> = goalDao.getAllSessionsNonLive()

    fun GoalRepository(application: Application){
        var database = GoalDatabase.getInstance(application)
        goalDao = database.goalDao()
        allEntries = goalDao.getAllSessions()
        allEntriesNonLive = goalDao.getAllSessionsNonLive()
    }

    fun insert(goalData: GoalData){
        InsertGoalDataAsyncTask(goalDao).execute(goalData)
    }

    fun update(goalData: GoalData){
        UpdateGoalDataAsyncTask(goalDao).execute(goalData)
    }

    fun delete(goalData: GoalData){
        DeleteGoalDataAsyncTask(goalDao).execute(goalData)
    }

    fun deleteAllEntries(){
        DeleteAllEntriesAsyncTask(goalDao).execute()
    }

    fun getAllSessions():LiveData<List<GoalData>>{
        return allEntries
    }

    fun getAllSessionsNonLive():List<GoalData>{
        return allEntriesNonLive
    }

    private class InsertGoalDataAsyncTask(private val mGoalDao: GoalDao) : AsyncTask<GoalData, Void, Void>() {

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: GoalData): Void? {
            mGoalDao.insert(params[0])
            return null
        }
    }
    private class UpdateGoalDataAsyncTask(private val mGoalDao: GoalDao) : AsyncTask<GoalData, Void, Void>() {

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: GoalData): Void? {
            mGoalDao.update(params[0])
            return null
        }
    }
    private class DeleteGoalDataAsyncTask(private val mGoalDao: GoalDao) : AsyncTask<GoalData, Void, Void>() {

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: GoalData): Void? {
            mGoalDao.delete(params[0])
            return null
        }
    }
    private class DeleteAllEntriesAsyncTask(private val mGoalDao: GoalDao) : AsyncTask<Void, Void, Void>() {

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void): Void? {
            mGoalDao.deleteAllEntries()
            return null
        }
    }
}