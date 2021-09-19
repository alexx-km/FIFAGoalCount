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

    private val keyGoalsPlayer1 = "com.porangidev.fifagoalcounter.ui.data.EXTRA_GOALS_PLAYER1"
    private val keyGoalsPlayer2 = "com.porangidev.fifagoalcounter.ui.data.EXTRA_GOALS_PLAYER2"
    private val keyGamesPlayed = "com.porangidev.fifagoalcounter.ui.data.EXTRA_GAMES_PLAYED"
    private val keyGoalsProgress = "com.porangidev.fifagoalcounter.ui.data.EXTRA_GOALS_PROGRESS"
    private val keyDate = "com.porangidev.fifagoalcounter.ui.data.EXTRA_DATE"
    private val keyId = "com.porangidev.fifagoalcounter.ui.data.EXTRA_ID"
    private val keyVersion = "com.porangidev.fifagoalcounter.ui.data.EXTRA_VERSION"

    private lateinit var dataViewModel: DataViewModel

    private lateinit var datepicker: EditText
    private lateinit var nmbgoalsPlayer2: NumberPicker
    private lateinit var nmbgoalsPlayer1: NumberPicker
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
    private var goalsPlayer1: Int = 0
    private var goalsPlayer2: Int = 0
    private var mid = 0
    private var isUpdate = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_add_data, container, false)
        datepicker = root.findViewById(R.id.edit_date)
        nmbgoalsPlayer2 = root.findViewById(R.id.mgoals_player2)
        nmbgoalsPlayer1 = root.findViewById(R.id.mgoals_player1)
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
        nmbgoalsPlayer2.minValue = 0
        nmbgoalsPlayer1.minValue = 0
        nmbgamesPlayed.maxValue = 50
        nmbgoalsPlayer2.maxValue = 50
        nmbgoalsPlayer1.maxValue = 50

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
            nmbgoalsPlayer1.value = requireArguments().getInt(keyGoalsPlayer1)
            nmbgoalsPlayer2.value = requireArguments().getInt(keyGoalsPlayer2)
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
            "FIFA 20" -> btn_group_version.check(btn_version_20.id)
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
        goalsPlayer2 = nmbgoalsPlayer2.value
        goalsPlayer1 = nmbgoalsPlayer1.value
        gamesPlayed = nmbgamesPlayed.value
        goalProgress = goalprogress.text.toString()

        val simpledateformat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
        val simpledate = simpledateformat.parse(date)
        val mdate: Long = simpledate.time

        val goalProgressNew = checkString(goalProgress, gamesPlayed, goalsPlayer1, goalsPlayer2)

        val goalData = GoalData(
            null,
            goalsPlayer1,
            goalsPlayer2,
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
        goalsPlayer2 = nmbgoalsPlayer2.value
        goalsPlayer1 = nmbgoalsPlayer1.value
        gamesPlayed = nmbgamesPlayed.value
        goalProgress = goalprogress.text.toString()

        val simpledateformat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
        val simpledate = simpledateformat.parse(date)
        val mdate: Long = simpledate.time

        val goalProgressNew = checkString(goalProgress, gamesPlayed, goalsPlayer1, goalsPlayer2)

        val goalData = GoalData(
            null,
            goalsPlayer1,
            goalsPlayer2,
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

    private fun checkString(string: String, games: Int, gplayer1: Int, gplayer2: Int): String {
        val newstring: String
        if (string.length != gplayer1+gplayer2) {
            newstring = "a".repeat(gplayer1) + "h".repeat(gplayer2)
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