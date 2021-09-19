package com.porangidev.fifagoalcounter.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply { value = "This is home Fragment" }
    val text: LiveData<String> = _text
    var goalsPlayer1 = 0
    var goalsPlayer2 = 0
    var gamesPlayed = 0
    var goalProgress = ""
    var ellapsedGameTime = 0L
    var goalsPlayer1Temp = 0
    var goalsPlayer2Temp = 0
    var player1 = ""
    var player2 = ""
}