package com.porangidev.fifagoalcounter.ui.stats

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class StatsTabsAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    var tabTitles = arrayOf("Tagesbericht", "Torverlauf", "Torquoten", "Gesamt")

    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                DailyReportFragment()
            }
            1 -> GoalsFragment()
            2 -> GoalQuotaFragment()
            else -> {
                return GoalTotalFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {

        return tabTitles[position]
    }
}