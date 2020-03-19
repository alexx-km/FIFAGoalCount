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

class GoalQuotaFragment : Fragment() {

    var prefs: SharedPreferences? = null

    private var goaldatasize = 0
    private var tempgoalsalex = 0
    private var tempgoalshendrik = 0

    private var _visiblefirsttime = false

    private lateinit var quotaChart: LineChart
    private lateinit var totalGoalsChart: PieChart

    private lateinit var repository: GoalRepository
    private lateinit var goalDataAdapter: GoalDataAdapter

    private lateinit var goalQuotaViewModel: GoalQuotaViewModel
    private lateinit var text: TextView
    private lateinit var listquotaalex: ArrayList<Entry>
    private lateinit var listquotahendrik: ArrayList<Entry>
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
        val root = inflater.inflate(R.layout.tab_goal_quota, container, false)

        quotaChart = root.findViewById(R.id.quota_chart)
        totalGoalsChart = root.findViewById(R.id.totalGoalChart)
        totalGoalsChart.visibility = View.INVISIBLE
        text = root.findViewById(R.id.textView6)
        text.visibility = View.GONE
        //GoalData
        listquotahendrik = ArrayList<Entry>()
        listquotaalex = ArrayList<Entry>()
        listtotalgoals = ArrayList<PieEntry>()
        prepareList()
        displayemptyQuotaChart()
        return root
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        //super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && !_visiblefirsttime) {
            _visiblefirsttime = true
            displayQuotaChart()
            displayTotalGoalsChart()
        }
    }

    private fun prepareList() {

        var df = DecimalFormat("#.##") //df.format()
        var tempquotaalex = 0F
        var tempquotahendrik = 0F

        var goaldatalist = goalQuotaViewModel.getAllSessionsNonLive()
        goaldatasize = goaldatalist.size

        goalDataAdapter.setGoalData(goaldatalist)

        //text.text = goaldatasize.toString()

        var currentGoalData: GoalData
        //text.text = goaldatasize.toString()

        while (goaldatasize > 0) {
            //get current goal data
            currentGoalData = goalDataAdapter.getGoalDataAt(goaldatasize - 1)
            //get goal quota and add to Entry
            tempquotaalex = currentGoalData.getAlexQuota().toFloat()
                /*BigDecimal(currentGoalData.getAlexQuota()).setScale(2, RoundingMode.HALF_EVEN)
                    .toFloat()*/
            tempquotahendrik = currentGoalData.getHendrikQuota().toFloat()
                /*BigDecimal(currentGoalData.getHendrikQuota()).setScale(2, RoundingMode.HALF_EVEN)
                    .toFloat()*/
            listquotaalex.add(Entry(currentGoalData.playDate.toFloat(), tempquotaalex))
            listquotahendrik.add(Entry(currentGoalData.playDate.toFloat(), tempquotahendrik))
            //
            tempgoalsalex += currentGoalData.goalsAlex
            tempgoalshendrik += currentGoalData.goalsHendrik
            goaldatasize -= 1
        }
        listtotalgoals.add(PieEntry(tempgoalsalex.toFloat(), "Tore ${goalQuotaViewModel.player1}"))
        listtotalgoals.add(PieEntry(tempgoalshendrik.toFloat(), "Tore ${goalQuotaViewModel.player2}"))
        text.text = tempgoalsalex.toString()
    }

    private fun displayTotalGoalsChart() {
        var dataSetTotalGoals = PieDataSet(listtotalgoals, "")
        //dataSetTotalGoals.valueFormatter = IValueFormatter(//)
        var colors = ArrayList<Int>()
        if (tempgoalsalex < tempgoalshendrik) {
            colors.add(ContextCompat.getColor(context!!, R.color.colorPrimary))
            colors.add(ContextCompat.getColor(context!!, R.color.colorLine2))
        } else {
            colors.add(ContextCompat.getColor(context!!, R.color.colorLine2))
            colors.add(ContextCompat.getColor(context!!, R.color.colorPrimary))
        }
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

    private fun displayQuotaChart() {
        var dataSetQuotaHendrik = LineDataSet(listquotahendrik, "Quote ${goalQuotaViewModel.player2}")
        dataSetQuotaHendrik.color = ContextCompat.getColor(context!!, R.color.colorPrimary)
        dataSetQuotaHendrik.lineWidth = 2F
        var dataSetQuotaAlex = LineDataSet(listquotaalex, "Quote ${goalQuotaViewModel.player1}")
        dataSetQuotaAlex.color = ContextCompat.getColor(context!!, R.color.colorLine2)
        dataSetQuotaAlex.lineWidth = 2F

        var xAxis = quotaChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1F
        xAxis.setDrawLabels(true)
        xAxis.valueFormatter = DateAxisFormatter()
        xAxis.setDrawGridLines(true)

        var yAxisRight = quotaChart.axisRight
        yAxisRight.isEnabled = false

        var yAxis = quotaChart.axisLeft
        yAxis.setDrawGridLines(true)

        var dataSetQuota = ArrayList<ILineDataSet>()
        dataSetQuota.add(dataSetQuotaAlex)
        dataSetQuota.add(dataSetQuotaHendrik)

        var data = LineData(dataSetQuota)
        quotaChart.data = data
        quotaChart.description.isEnabled = false
        quotaChart.setPinchZoom(false)
        quotaChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        quotaChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        quotaChart.isAutoScaleMinMaxEnabled = true
        quotaChart.animateX(1500, Easing.EaseInOutExpo)

    }

    private fun displayemptyQuotaChart() {
        var tempemptygoals = ArrayList<Entry>()
        tempemptygoals.add(Entry(0F, 0F))
        var dataSetEmpty = LineDataSet(tempemptygoals, "")
        dataSetEmpty.color = Color.TRANSPARENT
        var xAxis = quotaChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(false)
        var yAxisRight = quotaChart.axisRight
        yAxisRight.isEnabled = false
        yAxisRight.setDrawGridLines(false)
        var yAxis = quotaChart.axisLeft
        yAxis.axisMaximum = (3.0).toFloat()
        yAxis.axisMinimum = (0.0).toFloat()
        yAxis.granularity = 1F
        var dataSet = ArrayList<ILineDataSet>()
        dataSet.add(dataSetEmpty)
        var data = LineData(dataSet)
        quotaChart.setPinchZoom(false)
        quotaChart.isDragEnabled = false
        quotaChart.data = data
        quotaChart.description.isEnabled = false
        quotaChart.legend.isEnabled = false
    }


}