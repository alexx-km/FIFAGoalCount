package com.porangidev.fifagoalcounter.ui.data

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.porangidev.fifagoalcounter.GoalData
import com.porangidev.fifagoalcounter.R
import java.util.*


class AddDataFragment : Fragment() {

    private val keyGoalsAlex = "com.porangidev.fifagoalcounter.ui.data.EXTRA_GOALS_ALEX"
    private val keyGoalsHendrik = "com.porangidev.fifagoalcounter.ui.data.EXTRA_GOALS_HENDRIK"
    private val keyGamesPlayed = "com.porangidev.fifagoalcounter.ui.data.EXTRA_GAMES_PLAYED"
    private val keyGoalsProgress = "com.porangidev.fifagoalcounter.ui.data.EXTRA_GOALS_PROGRESS"
    private val keyDate = "com.porangidev.fifagoalcounter.ui.data.EXTRA_DATE"
    private val keyId = "com.porangidev.fifagoalcounter.ui.data.EXTRA_ID"
    private val keyVersion = "com.porangidev.fifagoalcounter.ui.data.EXTRA_VERSION"

    private lateinit var dataViewModel: DataViewModel

    private lateinit var datepicker: EditText
    private lateinit var nmbgoalsHendrik: NumberPicker
    private lateinit var nmbgoalsAlex: NumberPicker
    private lateinit var nmbgamesPlayed: NumberPicker
    private lateinit var goalprogress: EditText
    private lateinit var btn_group_version: RadioGroup
    private lateinit var btn_version_20: RadioButton
    private lateinit var btn_version_21: RadioButton
    private lateinit var btn_version_22: RadioButton

    private var fifaVersion: String = ""
    private var fifaVersionToSave: String = ""
    private var goalProgress: String = ""
    private var date: String = ""
    private var gamesPlayed: Int = 0
    private var goalsAlex: Int = 0
    private var goalsHendrik: Int = 0
    private var mid = 0
    private var isUpdate = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_add_data, container, false)
        datepicker = root.findViewById(R.id.edit_date)
        nmbgoalsHendrik = root.findViewById(R.id.mgoals_hendrik)
        nmbgoalsAlex = root.findViewById(R.id.mgoals_alex)
        nmbgamesPlayed = root.findViewById(R.id.mgames_played)
        goalprogress = root.findViewById(R.id.mgoal_progress)
        btn_group_version = root.findViewById(R.id.button_group_version)
        btn_version_20 = root.findViewById(R.id.button_version_20)
        btn_version_21 = root.findViewById(R.id.button_version_21)
        btn_version_22 = root.findViewById(R.id.button_version_22)
        val datepickerfragment = DatePickerFragment(datepicker)
        val tempcalendar = Calendar.getInstance()

        dataViewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)

        nmbgamesPlayed.minValue = 1
        nmbgoalsHendrik.minValue = 0
        nmbgoalsAlex.minValue = 0
        nmbgamesPlayed.maxValue = 50
        nmbgoalsHendrik.maxValue = 50
        nmbgoalsAlex.maxValue = 50

        if (arguments != null) {
            isUpdate = true
            mid = requireArguments().getInt(keyId)
            datepicker.setText(
                DateFormat.format(
                    "dd.MM.yyyy",
                    requireArguments().getLong(keyDate)
                ).toString()
            )
            fifaVersion = requireArguments().getString(keyVersion)!!
            nmbgoalsAlex.value = requireArguments().getInt(keyGoalsAlex)
            nmbgoalsHendrik.value = requireArguments().getInt(keyGoalsHendrik)
            nmbgamesPlayed.value = requireArguments().getInt(keyGamesPlayed)
            goalprogress.setText(requireArguments().getString(keyGoalsProgress))
        } else {
            datepicker.setText(DateFormat.format("dd.MM.yyyy", tempcalendar).toString())
        }

        datepicker.setOnClickListener { datepickerfragment.show(
            requireFragmentManager(),
            "datepicker"
        ) }

        when(fifaVersion){
            "FIFA 20" -> btn_group_version.check(btn_version_20.id);
            "FIFA 21" -> btn_group_version.check(btn_version_21.id)
        }

        fifaVersionToSave = fifaVersion

        btn_group_version.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.button_version_20 -> {
                    fifaVersionToSave = "FIFA 20"
                }
                R.id.button_version_21-> {
                    fifaVersionToSave = "FIFA 21"
                }
                R.id.button_version_22 -> {
                    fifaVersionToSave = "FIFA 22"
                }
            }
        }

        setHasOptionsMenu(true)

        return root
    }

    private fun save() {
        date = datepicker.text.toString()
        goalsHendrik = nmbgoalsHendrik.value
        goalsAlex = nmbgoalsAlex.value
        gamesPlayed = nmbgamesPlayed.value
        goalProgress = goalprogress.text.toString()

        val simpledateformat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
        val simpledate = simpledateformat.parse(date)
        val mdate: Long = simpledate.time

        val goalProgressNew = checkString(goalProgress, gamesPlayed, goalsAlex, goalsHendrik)

        val goalData = GoalData(
            null,
            goalsAlex,
            goalsHendrik,
            gamesPlayed,
            goalProgressNew,
            mdate,
            fifaVersionToSave
        )
        dataViewModel.insert(goalData)


        view?.findNavController()?.navigate(R.id.action_nav_adddata_to_data)
    }

    private fun update() {
        date = datepicker.text.toString()
        goalsHendrik = nmbgoalsHendrik.value
        goalsAlex = nmbgoalsAlex.value
        gamesPlayed = nmbgamesPlayed.value
        goalProgress = goalprogress.text.toString()

        val simpledateformat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
        val simpledate = simpledateformat.parse(date)
        val mdate: Long = simpledate.time

        val goalProgressNew = checkString(goalProgress, gamesPlayed, goalsAlex, goalsHendrik)

        val goalData = GoalData(
            null,
            goalsAlex,
            goalsHendrik,
            gamesPlayed,
            goalProgressNew,
            mdate,
            fifaVersionToSave
        )
        goalData.id = mid
        dataViewModel.update(goalData)

        isUpdate = false


        view?.findNavController()?.navigate(R.id.action_nav_adddata_to_data)
    }

    private fun checkString(string: String, games: Int, galex: Int, ghendrik: Int): String {
        val newstring: String
        if (string.length != galex+ghendrik) {
            newstring = "a".repeat(galex) + "h".repeat(ghendrik)
            Toast.makeText(context, "String wegen Fehler neu generiert", Toast.LENGTH_LONG).show()
            return newstring
        }
        return string
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.adddatamenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                if (isUpdate) {
                    update()
                } else {
                    save()
                }; return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}