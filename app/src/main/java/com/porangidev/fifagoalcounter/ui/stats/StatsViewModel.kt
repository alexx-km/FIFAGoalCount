package com.porangidev.fifagoalcounter.ui.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StatsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is stats Fragment"
    }
    val text: LiveData<String> = _text
    var goalsAlex = 0
    var goalsHendrik = 0
    var gamesPlayed = 0
    var goalProgress = ""
    var goalProgressTemp = ""
    var player1 = ""
    var player2 = ""
}