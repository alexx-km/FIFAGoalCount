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

    private class InsertGoalDataAsyncTask internal constructor(private val mGoalDao: GoalDao) : AsyncTask<GoalData, Void, Void>() {

        override fun doInBackground(vararg params: GoalData): Void? {
            mGoalDao.insert(params[0])
            return null
        }
    }
    private class UpdateGoalDataAsyncTask internal constructor(private val mGoalDao: GoalDao) : AsyncTask<GoalData, Void, Void>() {

        override fun doInBackground(vararg params: GoalData): Void? {
            mGoalDao.update(params[0])
            return null
        }
    }
    private class DeleteGoalDataAsyncTask internal constructor(private val mGoalDao: GoalDao) : AsyncTask<GoalData, Void, Void>() {

        override fun doInBackground(vararg params: GoalData): Void? {
            mGoalDao.delete(params[0])
            return null
        }
    }
    private class DeleteAllEntriesAsyncTask internal constructor(private val mGoalDao: GoalDao) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            mGoalDao.deleteAllEntries()
            return null
        }
    }
}