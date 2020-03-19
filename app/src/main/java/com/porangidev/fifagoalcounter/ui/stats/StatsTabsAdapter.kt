package com.porangidev.fifagoalcounter.ui.stats

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class StatsTabsAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    var tabTitles = arrayOf("Tagesbericht", "Torverlauf", "Torquoten")

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                DailyReportFragment()
            }
            1 -> GoalsFragment()
            else -> {
                return GoalQuotaFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {

        return tabTitles[position]
    }
}