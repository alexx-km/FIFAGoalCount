package com.porangidev.fifagoalcounter.ui.stats

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.porangidev.fifagoalcounter.R

class DailyReportFragment : Fragment() {

    var prefs: SharedPreferences? = null


    private lateinit var textplayer1: TextView
    private lateinit var textplayer2: TextView
    //textviews for goal quota
    private var rowsecondsecond: TextView? = null
    private var rowthirdsecond: TextView? = null
    private var rowfourthsecond: TextView? = null
    //textviews for goal data
    private var rowsecondthird: TextView? = null
    private var rowthirdthird: TextView? = null
    private var rowfourththird: TextView? = null
    private lateinit var piechart: PieChart

    private var keyPlayer1 = "key_player_1"
    private var keyPlayer2 = "key_player_2"

    private lateinit var statsViewModel: StatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //prepare view
        statsViewModel = ViewModelProviders.of(this).get(StatsViewModel::class.java)
        prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        val root = inflater.inflate(R.layout.tab_daily_report, container, false)
        //Views
        textplayer2 = root.findViewById(R.id.rowSecondFirst)
        rowsecondsecond = root.findViewById(R.id.rowSecondSecond)
        rowsecondthird = root.findViewById(R.id.rowSecondThird)
        textplayer1 = root.findViewById(R.id.rowThirdFirst)
        rowthirdsecond = root.findViewById(R.id.rowThirdSecond)
        rowthirdthird = root.findViewById(R.id.rowThirdThird)
        rowfourthsecond = root.findViewById(R.id.rowFourthSecond)
        rowfourththird = root.findViewById(R.id.rowFourthThird)
        piechart = root.findViewById(R.id.pie_chart)
        piechart.visibility = View.INVISIBLE
        //write data to viewmodel
        statsViewModel.goalsPlayer1 = prefs!!.getInt("KEY_GOALS_PLAYER1_PREFS", 0)
        statsViewModel.goalsPlayer2 = prefs!!.getInt("KEY_GOALS_PLAYER2_PREFS", 0)
        statsViewModel.gamesPlayed = prefs!!.getInt("KEY_GAMES_PLAYED_PREFS", 0)
        statsViewModel.player1 = prefs!!.getString(keyPlayer1, "")!!
        statsViewModel.player2 = prefs!!.getString(keyPlayer2, "")!!
        //change player text
        textplayer1.text = statsViewModel.player1
        textplayer2.text = statsViewModel.player2
        //
        updateRatio()
        updateChart()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateRatio()
    }

    private fun updateChart() {
        var entries = ArrayList<PieEntry>()
        entries.add(PieEntry(statsViewModel.goalsPlayer2.toFloat(), statsViewModel.player2))
        entries.add(PieEntry(statsViewModel.goalsPlayer1.toFloat(), statsViewModel.player1))
        var set = PieDataSet(entries, "")
        set.valueTextSize = 0F

        var colors = ArrayList<Int>()
        if (statsViewModel.goalsPlayer1 < statsViewModel.goalsPlayer2) {
            colors.add(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            colors.add(ContextCompat.getColor(requireContext(), R.color.colorLine2))
        } else {
            colors.add(ContextCompat.getColor(requireContext(), R.color.colorLine2))
            colors.add(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        }

        set.colors = colors

        var newdata = PieData(set)
        var font = Typeface.SANS_SERIF
        if (piechart.data == null)
            piechart.data = newdata
        piechart.visibility = View.VISIBLE
        piechart.legend.textSize = 15f
        piechart.legend.typeface = font
        piechart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        piechart.description.isEnabled = false
        val totalgoals = statsViewModel.goalsPlayer1 + statsViewModel.goalsPlayer2
        val goals = if (totalgoals > 1 || totalgoals == 0) {
            "Toren"
        } else "Tor"
        val centerText = "Verteilung von $totalgoals $goals"
        piechart.centerText = centerText
        piechart.setCenterTextSize(15F)
        piechart.setCenterTextColor(ContextCompat.getColor(requireContext(), R.color.amoledBlack))
        piechart.description!!.typeface = font
        piechart.isDrawHoleEnabled = true
        piechart.isHighlightPerTapEnabled = false
        piechart.isRotationEnabled = false
        piechart.animateXY(1000, 1000, Easing.EaseInOutQuad)
    }

    fun updateRatio() {
        if (statsViewModel.gamesPlayed == 0) {
            return
        } else {
            //Calculate Goals/Game Rate
            val gpgPlayer2 = ((statsViewModel.goalsPlayer2.toDouble()) / statsViewModel.gamesPlayed)
            val gpgPlayer1 = ((statsViewModel.goalsPlayer1.toDouble()) / statsViewModel.gamesPlayed)
            val gpgTogether =
                (((statsViewModel.goalsPlayer1 + statsViewModel.goalsPlayer2).toDouble()) / statsViewModel.gamesPlayed)
            rowsecondsecond?.text = String.format("%.2f", gpgPlayer2)
            rowthirdsecond?.text = String.format("%.2f", gpgPlayer1)
            rowfourthsecond?.text = String.format("%.2f", gpgTogether)
            rowsecondthird?.text = statsViewModel.goalsPlayer2.toString()
            rowthirdthird?.text = statsViewModel.goalsPlayer1.toString()
            rowfourththird?.text = (statsViewModel.goalsPlayer1 + statsViewModel.goalsPlayer2).toString()
        }
    }
}