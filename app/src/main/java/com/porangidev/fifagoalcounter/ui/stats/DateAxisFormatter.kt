package com.porangidev.fifagoalcounter.ui.stats

import android.text.format.DateFormat
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter

class DateAxisFormatter: IAxisValueFormatter {
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        var date = DateFormat.format("dd/MM/yyyy", value.toLong()).toString()
        return date
    }
}