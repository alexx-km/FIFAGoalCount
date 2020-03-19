package com.porangidev.fifagoalcounter.ui.data

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment(temp: EditText) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var c = Calendar.getInstance()
    private var edittext = temp

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(activity, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        var tempday = day.toString()
        var tempmonth = ""
        if(day<10){
            tempday = "0$day"
        }
        var mmonth = month + 1
        if(mmonth<10){
            tempmonth = "0$mmonth"
        }else tempmonth = mmonth.toString()
        c.set(year, month, day)
        var mString = "$tempday.$tempmonth.$year"
        edittext.setText(mString)
    }
}