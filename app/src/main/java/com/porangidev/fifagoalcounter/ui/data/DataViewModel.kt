package com.porangidev.fifagoalcounter.ui.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.porangidev.fifagoalcounter.GoalData
import com.porangidev.fifagoalcounter.GoalDatabase
import com.porangidev.fifagoalcounter.GoalRepository
import android.provider.ContactsContract.CommonDataKinds.Note



class DataViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GoalRepository
    private var allEntries: LiveData<List<GoalData>>

    init {
        val goalDao = GoalDatabase.getInstance(application).goalDao()
        repository = GoalRepository(goalDao)
        allEntries = repository.getAllSessions()
    }

    fun insert(goalData: GoalData){
        repository.insert(goalData)
    }
    fun update(goalData: GoalData){
        repository.update(goalData)
    }
    fun delete(goalData: GoalData){
        repository.delete(goalData)
    }
    fun deleteAllEntries(){
        repository.deleteAllEntries()
    }
    fun getAllSessions(): LiveData<List<GoalData>> {
        return allEntries
    }

    /*private val _text = MutableLiveData<String>().apply {
        value = "This is data Fragment"
    }
    val text: LiveData<String> = _text*/
}