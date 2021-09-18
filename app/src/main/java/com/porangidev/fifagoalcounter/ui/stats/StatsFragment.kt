package com.porangidev.fifagoalcounter.ui.stats

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.tabs.TabLayout
import com.porangidev.fifagoalcounter.*


class StatsFragment : Fragment() {

    private var prefs : SharedPreferences? = null

    private var keyPlayer1 = "key_player_1"
    private var keyPlayer2 = "key_player_2"

    private lateinit var statsViewModel: StatsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //prepare view
        statsViewModel = ViewModelProviders.of(this).get(StatsViewModel::class.java)
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
        statsViewModel.player1 = prefs!!.getString(keyPlayer1, "")!!
        statsViewModel.player2 = prefs!!.getString(keyPlayer2, "")!!
        //init lists
        val root = inflater.inflate(R.layout.fragment_stats, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tabLayout = view.findViewById<TabLayout>(R.id.tabs_main)
        val viewpager = view.findViewById<ViewPager>(R.id.viewpager_main)
        val adapter = StatsTabsAdapter(childFragmentManager)
        tabLayout.setupWithViewPager(viewpager)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        viewpager.adapter = adapter
        viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        viewpager.offscreenPageLimit = 2
    }

}