package com.porangidev.fifagoalcounter.ui.stats

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.porangidev.fifagoalcounter.*
import java.text.DecimalFormat


class GoalQuotaFragment : Fragment() {

    var prefs: SharedPreferences? = null

    private var goaldatasize = 0
    private var tempgoalsplayer1 = 0
    private var tempgoalsplayer2 = 0
    private var lastplaydate = 0F

    private var _visiblefirsttime = false

    private lateinit var quotaChart: LineChart

    private lateinit var repository: GoalRepository
    private lateinit var goalDataAdapter: GoalDataAdapter

    private lateinit var goalQuotaViewModel: GoalQuotaViewModel
    private lateinit var listquotaplayer1: ArrayList<Entry>
    private lateinit var listquotaplayer2: ArrayList<Entry>
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
        goalQuotaViewModel.player1 = prefs!!.getString(keyPlayer1, "")!!
        goalQuotaViewModel.player2 = prefs!!.getString(keyPlayer2, "")!!
        repository = GoalRepository(GoalDatabase.INSTANCE!!.goalDao())
        goalDataAdapter = GoalDataAdapter()
        val root = inflater.inflate(R.layout.tab_goal_quota, container, false)

        quotaChart = root.findViewById(R.id.quota_chart)
        //GoalData
        listquotaplayer2 = ArrayList<Entry>()
        listquotaplayer1 = ArrayList<Entry>()
        listtotalgoals = ArrayList<PieEntry>()
        prepareList()
        displayQuotaChart()
        return root
    }

    private fun prepareList() {

        var df = DecimalFormat("#.##") //df.format()
        var tempquotaplayer1 = 0F
        var tempquotaplayer2 = 0F

        var goaldatalist = goalQuotaViewModel.getAllSessionsNonLive()
        goaldatasize = goaldatalist.size

        if (goaldatasize > 0){
            goalDataAdapter.setGoalData(goaldatalist)

            //text.text = goaldatasize.toString()

            var currentGoalData: GoalData
            //text.text = goaldatasize.toString()

            var newestGoalData = goalDataAdapter.getGoalDataAt(goaldatasize - 1)
            lastplaydate = newestGoalData.playDate.toFloat()

            while (goaldatasize > 0) {
                //get current goal data
                currentGoalData = goalDataAdapter.getGoalDataAt(goaldatasize - 1)
                //get goal quota and add to Entry
                tempquotaplayer1 = currentGoalData.getPlayer1Quota().toFloat()
                /*BigDecimal(currentGoalData.getPlayer1Quota()).setScale(2, RoundingMode.HALF_EVEN)
                    .toFloat()*/
                tempquotaplayer2 = currentGoalData.getPlayer2Quota().toFloat()
                /*BigDecimal(currentGoalData.getPlayer2Quota()).setScale(2, RoundingMode.HALF_EVEN)
                    .toFloat()*/
                listquotaplayer1.add(Entry(currentGoalData.playDate.toFloat(), tempquotaplayer1))
                listquotaplayer2.add(Entry(currentGoalData.playDate.toFloat(), tempquotaplayer2))
                goaldatasize -= 1
            }
        }
    }

    private fun displayQuotaChart() {
        var dataSetQuotaPlayer2 = LineDataSet(
            listquotaplayer2,
            "Quote ${goalQuotaViewModel.player2}"
        )
        dataSetQuotaPlayer2.color = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        dataSetQuotaPlayer2.lineWidth = 2F
        var dataSetQuotaPlayer1 = LineDataSet(listquotaplayer1, "Quote ${goalQuotaViewModel.player1}")
        dataSetQuotaPlayer1.color = ContextCompat.getColor(requireContext(), R.color.colorLine2)
        dataSetQuotaPlayer1.lineWidth = 2F

        var xAxis = quotaChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //xAxis.granularity = 1F
        xAxis.setDrawLabels(true)
        xAxis.valueFormatter = DateAxisFormatter()
        xAxis.setDrawGridLines(true)

        var yAxisRight = quotaChart.axisRight
        yAxisRight.isEnabled = false

        var yAxis = quotaChart.axisLeft
        yAxis.setDrawGridLines(true)

        var dataSetQuota = ArrayList<ILineDataSet>()
        dataSetQuota.add(dataSetQuotaPlayer1)
        dataSetQuota.add(dataSetQuotaPlayer2)

        var data = LineData(dataSetQuota)
        quotaChart.data = data
        quotaChart.description.isEnabled = false
        quotaChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        quotaChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        quotaChart.isAutoScaleMinMaxEnabled = true
        quotaChart.animateX(1500, Easing.EaseInOutExpo)

        /*quotaChart.notifyDataSetChanged()
        quotaChart.moveViewToX(lastplaydate)
        quotaChart.setVisibleXRangeMaximum(5000000000F)*/

    }
}