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
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.porangidev.fifagoalcounter.*
import java.text.DecimalFormat

class GoalQuotaFragment : Fragment() {

    var prefs: SharedPreferences? = null

    private var goaldatasize = 0
    private var tempgoalsalex = 0
    private var tempgoalshendrik = 0
    private var lastplaydate = 0F

    private var _visiblefirsttime = false

    private lateinit var quotaChart: LineChart

    private lateinit var repository: GoalRepository
    private lateinit var goalDataAdapter: GoalDataAdapter

    private lateinit var goalQuotaViewModel: GoalQuotaViewModel
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

        var newestGoalData = goalDataAdapter.getGoalDataAt(goaldatasize - 1)
        lastplaydate = newestGoalData.playDate.toFloat()

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
            goaldatasize -= 1
        }

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
        //xAxis.granularity = 1F
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
        quotaChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        quotaChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        quotaChart.isAutoScaleMinMaxEnabled = true
        quotaChart.animateX(1500, Easing.EaseInOutExpo)

        /*quotaChart.notifyDataSetChanged()
        quotaChart.moveViewToX(lastplaydate)
        quotaChart.setVisibleXRangeMaximum(5000000000F)*/

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