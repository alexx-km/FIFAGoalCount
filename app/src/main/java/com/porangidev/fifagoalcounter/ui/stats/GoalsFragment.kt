package com.porangidev.fifagoalcounter.ui.stats

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.porangidev.fifagoalcounter.GoalDataAdapter
import com.porangidev.fifagoalcounter.R

class GoalsFragment : Fragment(), OnItemSelectedListener {

    var prefs: SharedPreferences? = null
    var keyGoalProgress = "KEY_GOAL_PROGRESS_PREFS"
    var keyGoalsAlex = "KEY_GOALS_ALEX_PREFS"
    var keyGoalsHendrik = "KEY_GOALS_HENDRIK_PREFS"
    var goalprogress: String = ""
    var goalprogresstemp: String = ""

    private lateinit var goalQuotaViewModel: GoalQuotaViewModel
    private lateinit var graph: LineChart
    private lateinit var goaldataSpinner: Spinner
    private lateinit var listgoalsalex: ArrayList<Entry>
    private lateinit var listgoalshendrik: ArrayList<Entry>
    private lateinit var dataSet: ArrayList<ILineDataSet>
    private lateinit var data: LineData

    private var keyPlayer1 = "key_player_1"
    private var keyPlayer2 = "key_player_2"

    private var _visiblefirsttime = false
    private var _viewcreated = false
    private var entries = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //prepare view
        goalQuotaViewModel = ViewModelProviders.of(this).get(GoalQuotaViewModel::class.java)
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
        goalprogress = prefs!!.getString(keyGoalProgress, "")!!
        goalQuotaViewModel.player1 = prefs!!.getString(keyPlayer1, "")!!
        goalQuotaViewModel.player2 = prefs!!.getString(keyPlayer2, "")!!
        val root = inflater.inflate(R.layout.tab_goals, container, false)
        //views by id
        val textview = root.findViewById<TextView>(R.id.textView8)
        textview.visibility = View.INVISIBLE
        graph = root.findViewById(R.id.quota_chart)
        goaldataSpinner = root.findViewById(R.id.goaldataSpinner)
        //init Lists
        listgoalshendrik = ArrayList<Entry>()
        listgoalsalex = ArrayList<Entry>()
        dataSet = ArrayList()
        data = LineData()
        //setUp graph
        //graph.data = makeEntries(prefs!!.getString(keyGoalProgress, ""))
        setUpGraph()
        displayEmptyChart()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewcreated = true
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && !_visiblefirsttime && _viewcreated) {
            setUpSpinner()
            displayChart()
            _visiblefirsttime = true
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        return
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (_visiblefirsttime) {
            var goaldatalist = goalQuotaViewModel.getAllSessionsNonLive()
            var tempentries = goaldatalist.size
            var goalDataAdapter = GoalDataAdapter()
            goalDataAdapter.setGoalData(goaldatalist)
            //graph.data.removeDataSet(graph.data.getDataSetByLabel())
            if (position == tempentries) {
                //Toast.makeText(context, "position==tempentries", Toast.LENGTH_LONG).show()
                graph.data.clearValues()
                graph.data = makeEntries(prefs!!.getString(keyGoalProgress, "")!!)
                graph.animateX(1500, Easing.EaseInOutQuad)
            } else {
                var currentgoaldata = goalDataAdapter.getGoalDataAt(tempentries - position - 1)
                graph.data.clearValues()
                graph.data = makeEntries(currentgoaldata.goalsProgress)
                graph.animateX(1500, Easing.EaseInOutQuad)
            }
            //Toast.makeText(context, "$position", Toast.LENGTH_LONG).show()
        }
    }

    private fun setUpSpinner() {
        entries = goalQuotaViewModel.getAllSessionsNonLive().size
        var GoalDataAdapter = GoalDataAdapter()
        GoalDataAdapter.setGoalData(goalQuotaViewModel.getAllSessionsNonLive())
        var datelist = ArrayList<String>()
        while (entries > 0) {
            var currentGoalData = GoalDataAdapter.getGoalDataAt(entries - 1)
            var date = DateFormat.format("dd/MM/yyyy", currentGoalData.playDate).toString()
            datelist.add(date)
            entries -= 1
        }
        datelist.add(DateFormat.format("dd/MM/yyyy", System.currentTimeMillis()).toString())
        var spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, datelist)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        goaldataSpinner.adapter = spinnerAdapter
        goaldataSpinner.onItemSelectedListener = this
        goaldataSpinner.setSelection(datelist.size - 1)
    }

    private fun setUpGraph() {
        var goalsalex = prefs!!.getInt(keyGoalsAlex, 0)
        var goalshendrik = prefs!!.getInt(keyGoalsHendrik, 0)
        var goalstotal = goalsalex + goalshendrik
        var xAxis = graph.xAxis
        var goalmax = goalsalex
        if (goalsalex<goalshendrik) goalmax = goalshendrik //setzt den maximalen wert der y-achse
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisMaximum = (goalstotal - 1).toFloat()
        xAxis.granularity = 1F

        var yAxis = graph.axisLeft
        yAxis.axisMaximum = goalmax.toFloat()
        yAxis.granularity = 1F

    }

    private fun makeEntries(goalprogress: String): LineData {
        var tempgoalprogress = goalprogress
        var tempgoalsalex = 0
        var tempgoalshendrik = 0
        var tempint = 0
        listgoalsalex.clear()
        listgoalshendrik.clear()
        while (tempgoalprogress.isNotEmpty()) {
            if (tempgoalprogress.first() == 'a') {
                tempgoalsalex++
                listgoalsalex.add(Entry(tempint.toFloat(), tempgoalsalex.toFloat()))
                listgoalshendrik.add(Entry(tempint.toFloat(), tempgoalshendrik.toFloat()))
                tempint++
                tempgoalprogress = tempgoalprogress.drop(1)
            } else if (tempgoalprogress.first() == 'h') {
                tempgoalshendrik++
                listgoalshendrik.add(Entry(tempint.toFloat(), tempgoalshendrik.toFloat()))
                listgoalsalex.add(Entry(tempint.toFloat(), tempgoalsalex.toFloat()))
                tempint++
                tempgoalprogress = tempgoalprogress.drop(1)
            }
        }
        var dataSetHendrik = LineDataSet(listgoalshendrik, "Tore ${goalQuotaViewModel.player2}")
        dataSetHendrik.color = ContextCompat.getColor(requireContext(), R.color.colorLine1)
        var dataSetAlex = LineDataSet(listgoalsalex, "Tore ${goalQuotaViewModel.player1}")
        dataSetAlex.color = ContextCompat.getColor(requireContext(), R.color.colorLine2)
        dataSet = ArrayList()
        dataSet.clear()
        data.clearValues()
        dataSet.add(dataSetAlex)
        dataSet.add(dataSetHendrik)
        data = LineData(dataSet)
        graph.xAxis.axisMaximum = (tempgoalsalex + tempgoalshendrik - 1).toFloat()
        if (tempgoalsalex > tempgoalshendrik) {
            graph.axisLeft.axisMaximum = tempgoalsalex.toFloat()
        } else {
            graph.axisLeft.axisMaximum = tempgoalshendrik.toFloat()
        }
        graph.legend.isEnabled = true
        return data
    }

    private fun displayEmptyChart() {
        var tempemptygoals = ArrayList<Entry>()
        tempemptygoals.add(Entry(0F, 0F))
        var dataSetEmpty = LineDataSet(tempemptygoals, "")
        dataSetEmpty.color = Color.TRANSPARENT
        var xAxis = graph.xAxis
        xAxis.axisMinimum = 0F
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        var yAxis = graph.axisRight
        yAxis.isEnabled = false
        var yAxisLeft = graph.axisLeft
        yAxisLeft.axisMinimum = 0F
        var dataSet = ArrayList<ILineDataSet>()
        dataSet.add(dataSetEmpty)
        var data = LineData(dataSet)
        graph.setPinchZoom(false)
        graph.isDragEnabled = false
        graph.data = data
        graph.isHighlightPerTapEnabled = false
        graph.description.isEnabled = false
        graph.legend.isEnabled = true
    }

    private fun displayChart() {
        var xAxis = graph.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        var yAxis = graph.axisRight
        yAxis.isEnabled = false

        var data = LineData(dataSet)
        graph.setPinchZoom(false)
        graph.isDragEnabled = false
        graph.data = data
        graph.isHighlightPerTapEnabled = false
        graph.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        graph.description.isEnabled = false
    }


}