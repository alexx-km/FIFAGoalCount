package com.porangidev.fifagoalcounter.ui.stats

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.porangidev.fifagoalcounter.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.sql.Date
import java.text.DateFormat
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class GoalTotalFragment : Fragment() {

    var prefs: SharedPreferences? = null

    private var goaldatasize = 0
    private var tempgoalsalex = 0
    private var tempgoalshendrik = 0
    private var lastplaydate = 0F

    private var _visiblefirsttime = false

    private lateinit var totalGoalsChart: PieChart

    private lateinit var repository: GoalRepository
    private lateinit var goalDataAdapter: GoalDataAdapter

    private lateinit var goalQuotaViewModel: GoalQuotaViewModel
    private lateinit var listtotalgoals: ArrayList<PieEntry>

    private var keyPlayer1 = "key_player_1"
    private var keyPlayer2 = "key_player_2"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //prepare view
        goalQuotaViewModel = ViewModelProviders.of(this).get(GoalQuotaViewModel::class.java)
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
        goalQuotaViewModel.player1 = prefs!!.getString(keyPlayer1, "")
        goalQuotaViewModel.player2 = prefs!!.getString(keyPlayer2, "")
        repository = GoalRepository(GoalDatabase.INSTANCE!!.goalDao())
        goalDataAdapter = GoalDataAdapter()
        val root = inflater.inflate(R.layout.tab_goal_total, container, false)
        totalGoalsChart = root.findViewById(R.id.totalGoalChart)
        totalGoalsChart.visibility = View.INVISIBLE
        //GoalData
        listtotalgoals = ArrayList<PieEntry>()
        prepareList()
        return root
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        //super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && !_visiblefirsttime) {
            _visiblefirsttime = true
            displayTotalGoalsChart()
        }
    }

    private fun prepareList() {

        var goaldatalist = goalQuotaViewModel.getAllSessionsNonLive()
        goaldatasize = goaldatalist.size

        goalDataAdapter.setGoalData(goaldatalist)

        //text.text = goaldatasize.toString()

        var currentGoalData: GoalData
        //text.text = goaldatasize.toString()

        var newestGoalData = goalDataAdapter.getGoalDataAt(goaldatasize - 1)
        lastplaydate = newestGoalData.playDate.toFloat()

        while (goaldatasize > 0) {
            //get current goal data
            currentGoalData = goalDataAdapter.getGoalDataAt(goaldatasize - 1)
            tempgoalsalex += currentGoalData.goalsAlex
            tempgoalshendrik += currentGoalData.goalsHendrik
            goaldatasize -= 1
        }
    }

    private fun displayTotalGoalsChart() {
        var dataSetTotalGoals = PieDataSet(listtotalgoals, "")
        //dataSetTotalGoals.valueFormatter = IValueFormatter(//)
        var colors = ArrayList<Int>()
        colors.add(ContextCompat.getColor(context!!, R.color.colorLine2))
        colors.add(ContextCompat.getColor(context!!, R.color.colorPrimary))
        dataSetTotalGoals.colors = colors
        dataSetTotalGoals.valueTextSize = 15F
        dataSetTotalGoals.valueTextColor = Color.LTGRAY
        //dataSetTotalGoals.valueTextColor
        var data = PieData(dataSetTotalGoals)
        var totalgoals = tempgoalsalex + tempgoalshendrik

        totalGoalsChart.data = data
        totalGoalsChart.setDrawEntryLabels(false)
        totalGoalsChart.centerText = "Insgesamt $totalgoals Tore"
        totalGoalsChart.setCenterTextColor(ContextCompat.getColor(context!!, R.color.amoledBlack))
        totalGoalsChart.description.isEnabled = false
        totalGoalsChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        totalGoalsChart.visibility = View.VISIBLE
        totalGoalsChart.setEntryLabelColor(Color.LTGRAY)
        totalGoalsChart.isRotationEnabled = false
        totalGoalsChart.isHighlightPerTapEnabled = false
        totalGoalsChart.animateXY(1000, 1000, Easing.Linear)
    }




}