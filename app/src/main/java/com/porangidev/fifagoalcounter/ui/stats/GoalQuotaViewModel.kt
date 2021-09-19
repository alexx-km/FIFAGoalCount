package com.porangidev.fifagoalcounter.ui.stats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.porangidev.fifagoalcounter.GoalData
import com.porangidev.fifagoalcounter.GoalDatabase
import com.porangidev.fifagoalcounter.GoalRepository

class GoalQuotaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GoalRepository
    private var allEntries: LiveData<List<GoalData>>
    private var allEntriesNonLive: List<GoalData>

    init {
        val goalDao = GoalDatabase.getInstance(application).goalDao()
        repository = GoalRepository(goalDao)
        allEntriesNonLive = repository.getAllSessionsNonLive()
        allEntries = repository.getAllSessions()

    }

    fun getAllSessions(): LiveData<List<GoalData>> {
        return allEntries
    }

    fun getAllSessionsNonLive(): List<GoalData> {
        return allEntriesNonLive
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is stats Fragment"
    }
    val text: LiveData<String> = _text
    var goalsPlayer1 = 0
    var goalsPlayer2 = 0
    var gamesPlayed = 0
    var goalProgress = ""
    var goalProgressTemp = ""
    var player1 = ""
    var player2 = ""
}