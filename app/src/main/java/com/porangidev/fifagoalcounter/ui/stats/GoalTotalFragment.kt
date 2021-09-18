package com.porangidev.fifagoalcounter.ui.stats

import android.app.Activity
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
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
import kotlin.math.round

class GoalTotalFragment : Fragment() {

    var prefs: SharedPreferences? = null

    private var goaldatasize = 0
    private var tempgoalsalex = 0
    private var tempgoalshendrik = 0
    private var lastplaydate = 0F

    private var _visiblefirsttime = false

    private lateinit var totalGoalsChart: PieChart

    private lateinit var goalQuotaViewModel: StatsViewModel

    //further stats
    private lateinit var row00: TextView
    private lateinit var row01: TextView
    private lateinit var row02: TextView
    private lateinit var row10: TextView
    private lateinit var row11: TextView
    private lateinit var row12: TextView

    private var keyPlayer1 = "key_player_1"
    private var keyPlayer2 = "key_player_2"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //prepare view
        goalQuotaViewModel = ViewModelProviders.of(this).get(StatsViewModel::class.java)
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
        goalQuotaViewModel.player1 = prefs!!.getString(keyPlayer1, "")!!
        goalQuotaViewModel.player2 = prefs!!.getString(keyPlayer2, "")!!
        val root = inflater.inflate(R.layout.tab_goal_total, container, false)
        totalGoalsChart = root.findViewById(R.id.totalGoalChart)
        totalGoalsChart.visibility = View.INVISIBLE
        //Further Stats
        row00 = root.findViewById(R.id.row00)
        row01 = root.findViewById(R.id.row01)
        row02 = root.findViewById(R.id.row02)
        row10 = root.findViewById(R.id.row10)
        row11 = root.findViewById(R.id.row11)
        row12 = root.findViewById(R.id.row12)
        //GoalData
        goalQuotaViewModel.listtotalgoals = ArrayList<PieEntry>()
        goalQuotaViewModel.listtotalgoals = (activity as MainActivity?)!!.getPieData()
        goalQuotaViewModel.totalgames = (activity as MainActivity?)!!.getTotalGames()
        //prepareList()
        displayTotalGoalsChart()
        displayFurtherStats()
        return root
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        //super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && !_visiblefirsttime) {
            _visiblefirsttime = true
            //displayTotalGoalsChart()
        }
    }

    fun displayFurtherStats(){
        row00.text = "Bis jetzt"
        row01.text = goalQuotaViewModel.totalgames.toString()
        row01.typeface = Typeface.DEFAULT_BOLD
        row02.text = "Spiele gespielt"

        row10.text = "Das entspricht ca."
        row11.text = (round((goalQuotaViewModel.totalgames * 15.0)/60)).toString()
        row11.typeface = Typeface.DEFAULT_BOLD
        row12.text = "Stunden"
    }

    private fun displayTotalGoalsChart() {
        var dataSetTotalGoals = PieDataSet(goalQuotaViewModel.listtotalgoals, "")
        //dataSetTotalGoals.valueFormatter = IValueFormatter(//)
        var colors = ArrayList<Int>()
        colors.add(ContextCompat.getColor(requireContext(), R.color.colorLine2))
        colors.add(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        dataSetTotalGoals.colors = colors
        dataSetTotalGoals.valueFormatter
        dataSetTotalGoals.valueTextSize = 15F
        dataSetTotalGoals.valueTextColor = Color.LTGRAY
        //dataSetTotalGoals.valueTextColor
        var data = PieData(dataSetTotalGoals)
        var totalgoals = goalQuotaViewModel.listtotalgoals[0].y + goalQuotaViewModel.listtotalgoals[1].y

        totalGoalsChart.data = data
        totalGoalsChart.setDrawEntryLabels(false)
        totalGoalsChart.centerText = "Insgesamt $totalgoals Tore"
        totalGoalsChart.setCenterTextColor(ContextCompat.getColor(requireContext(), R.color.amoledBlack))
        totalGoalsChart.description.isEnabled = false
        totalGoalsChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        totalGoalsChart.visibility = View.VISIBLE
        totalGoalsChart.setEntryLabelColor(Color.LTGRAY)
        totalGoalsChart.isRotationEnabled = false
        totalGoalsChart.isHighlightPerTapEnabled = false
        totalGoalsChart.animateXY(1000, 1000, Easing.Linear)
    }




}