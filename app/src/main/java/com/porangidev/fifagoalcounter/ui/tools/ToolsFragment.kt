package com.porangidev.fifagoalcounter.ui.tools

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.porangidev.fifagoalcounter.R

class ToolsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val colorValues = arrayOf("1", "2")
        val colorNames = arrayOf("1", "2")
        val colorPicker = ListPreference(context).apply {
            key = "key_color"
            title = "Wähle die Design Farbe"
            entries = arrayOf("Schwarz", "Rot", "Grün")
            entryValues = arrayOf(
                R.color.pickerBlack.toString(),
                R.color.pickerRed.toString(),
                R.color.pickerGreen.toString()
            )
        }
        setPreferencesFromResource(R.xml.layout_settings, rootKey)
        val player1: EditTextPreference? = findPreference("key_player_1")
        val player2: EditTextPreference? = findPreference("key_player_2")

    }

    private lateinit var toolsViewModel: ToolsViewModel

    /*override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        toolsViewModel = ViewModelProviders.of(this).get(ToolsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_tools, container, false)
        val textView: TextView = root.findViewById(R.id.text_tools)
        toolsViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }*/

    /*player1?.summaryProvider = Preference.SummaryProvider<EditTextPreference> { preference ->
            val text = preference.text
            if (TextUtils.isEmpty(text)){
                "Nicht festgelegt"
            }
            else {
                text
            }
        }
        player2?.summaryProvider = Preference.SummaryProvider<EditTextPreference> { preference ->
            val text = preference.text
            if (TextUtils.isEmpty(text)){
                "Nicht festgelegt"
            }
            else {
                text
            }
        }*/
}