package com.porangidev.fifagoalcounter.ui.stats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.PieEntry
import com.porangidev.fifagoalcounter.GoalData
import com.porangidev.fifagoalcounter.GoalDatabase
import com.porangidev.fifagoalcounter.GoalRepository

class StatsViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var listtotalgoals: ArrayList<PieEntry>
    var totalgames = 0
    var goalsAlex = 0
    var goalsHendrik = 0
    var gamesPlayed = 0
    var goalProgress = ""
    var goalProgressTemp = ""
    var player1 = ""
    var player2 = ""
}