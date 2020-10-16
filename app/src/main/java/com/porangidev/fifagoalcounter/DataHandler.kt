package com.porangidev.fifagoalcounter

import android.media.AsyncPlayer
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry

data class dataPack(var quotaPlayer1 : ArrayList<Entry>, var quotaPlayer2: ArrayList<Entry>, var totalGames: Int, var listTotalGoals: ArrayList<PieEntry>)

class DataHandler {
    fun preparePieData(repository : GoalRepository, goalDataAdapter : GoalDataAdapter, player1 : String, player2 : String) : dataPack{
        //Quota Lists
        var listquotaplayer1 = ArrayList<Entry>()
        var listquotaplayer2 = ArrayList<Entry>()
        //Total Games
        var totalgames = 0
        //Total Goals
        var listtotalgoals = ArrayList<PieEntry>()
        //Get GoalData List
        val goaldatalist = repository.getAllSessionsNonLive()
        //Get Size of GoalData List
        var goaldatasize = goaldatalist.size
        //Set GoalDataAdapter to GoalDataList
        goalDataAdapter.setGoalData(goaldatalist)

        if (goaldatasize > 0) {
            var currentGoalData: GoalData
            var newestGoalData = goalDataAdapter.getGoalDataAt(goaldatasize - 1)
            var lastplaydate = newestGoalData.playDate.toFloat()

            var tempgoalsalex = 0
            var tempgoalshendrik = 0

            while (goaldatasize > 0) {
                //get current goal data
                currentGoalData = goalDataAdapter.getGoalDataAt(goaldatasize - 1)
                tempgoalsalex += currentGoalData.goalsAlex
                tempgoalshendrik += currentGoalData.goalsHendrik
                listquotaplayer1.add(Entry(currentGoalData.playDate.toFloat(), currentGoalData.getAlexQuota().toFloat()))
                listquotaplayer2.add(Entry(currentGoalData.playDate.toFloat(), currentGoalData.getHendrikQuota().toFloat()))
                totalgames += currentGoalData.playedGames
                goaldatasize -= 1
            }
            listtotalgoals.add(PieEntry(tempgoalsalex.toFloat(), "Tore ${player1}"))
            listtotalgoals.add(PieEntry(tempgoalshendrik.toFloat(), "Tore ${player2}"))

            //goals current matchday missing
        }
        else {
            listquotaplayer1.add(Entry(0.0F, 0.0F))
            listquotaplayer2.add(Entry(0.0F, 0.0F))
            listtotalgoals.add(PieEntry(0.0F, "Tore ${player1}"))
            listtotalgoals.add(PieEntry(0.0F, "Tore ${player2}"))
        }
        return dataPack(listquotaplayer1, listquotaplayer2, totalgames, listtotalgoals)
    }
}