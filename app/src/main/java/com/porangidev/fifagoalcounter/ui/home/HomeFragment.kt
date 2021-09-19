package com.porangidev.fifagoalcounter.ui.home

import android.app.AlertDialog
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.porangidev.fifagoalcounter.GoalData
import com.porangidev.fifagoalcounter.R
import com.porangidev.fifagoalcounter.ui.data.DataViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private var listener: OnFragmentHomeInteractionListener? = null
    //Shared Preferences
    private var prefs: SharedPreferences? = null
    private var keyGoalsPlayer2 = "KEY_GOALS_PLAYER2_PREFS"
    private var keyGoalsPlayer2Temp = "KEY_GOALS_PLAYER2_TEMP_PREFS"
    private var keyGoalsPlayer1 = "KEY_GOALS_PLAYER1_PREFS"
    private var keyGoalsPlayer1Temp = "KEY_GOALS_PLAYER1_TEMP_PREFS"
    private var keyGamesPlayed = "KEY_GAMES_PLAYED_PREFS"
    private var keyElapsedGameTime = "KEY_ELAPSED_GAME_TIME_PREFS"
    private var keyGoalProgress = "KEY_GOAL_PROGRESS_PREFS"
    private var keyGameDate = "KEY_GAME_DATE_PREFS"
    private var keyPlayer1 = "key_player_1"
    private var keyPlayer2 = "key_player_2"
    //Divider
    private lateinit var divider: View
    private lateinit var divider2: View
    //TextViews
    private lateinit var numberGoalsPlayer1: TextView
    private lateinit var numberGoalsPlayer2: TextView
    private lateinit var numberGamesPlayed: TextView
    private lateinit var textGoalProgress: TextView
    private lateinit var date: TextView
    private lateinit var textPlayer1: TextView
    private lateinit var textPlayer2: TextView
    //Buttons
    private lateinit var addGoalPlayer1: FloatingActionButton
    private lateinit var delGoalPlayer1: FloatingActionButton
    private lateinit var addGoalPlayer2: FloatingActionButton
    private lateinit var delGoalPlayer2: FloatingActionButton
    private lateinit var addGame: FloatingActionButton
    private lateinit var delGame: FloatingActionButton
    private lateinit var resetButton: FloatingActionButton
    private lateinit var saveButton: Button
    //Chronometer
    private lateinit var gameTime: Chronometer
    //Prefs
    private var showSpoiler: Boolean = false
    private var gameDate: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
        showSpoiler = prefs!!.getBoolean("key_show_spoiler", false)
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        homeViewModel.ellapsedGameTime = gameTime.base
        super.onDestroyView()
    }

    private fun init() {
        //TextViews
        numberGoalsPlayer1 = requireView().findViewById(R.id.number_goals_player1)
        numberGoalsPlayer2 = requireView().findViewById(R.id.number_goals_player2)
        numberGamesPlayed = requireView().findViewById(R.id.number_games_played)
        textGoalProgress = requireView().findViewById(R.id.text_last_goals)
        textPlayer1 = requireView().findViewById(R.id.text_player_1)
        textPlayer2 = requireView().findViewById(R.id.text_player_2)
        divider = requireView().findViewById(R.id.divider)
        divider2 = requireView().findViewById(R.id.divider2)
        var showprogress = prefs!!.getBoolean("key_show_progress_debug", true)
        if (showprogress) {
            textGoalProgress.visibility = View.VISIBLE
            divider.visibility = View.VISIBLE
            divider2.visibility = View.VISIBLE
        } else {
            textGoalProgress.visibility = View.INVISIBLE
            divider.visibility = View.INVISIBLE
            divider2.visibility = View.INVISIBLE
        }
        date = requireView().findViewById(R.id.date)
        //Buttons
        addGoalPlayer1 = requireView().findViewById(R.id.addGoalPlayer1)
        delGoalPlayer1 = requireView().findViewById(R.id.delGoalPlayer1)
        addGoalPlayer2 = requireView().findViewById(R.id.addGoalPlayer2)
        delGoalPlayer2 = requireView().findViewById(R.id.delGoalPlayer2)
        addGame = requireView().findViewById(R.id.addGame)
        delGame = requireView().findViewById(R.id.delGame)
        resetButton = requireView().findViewById(R.id.resetButton)
        saveButton = requireView().findViewById(R.id.saveButton)
        //Button OnClickListener
        addGoalPlayer1.setOnClickListener(this)
        delGoalPlayer1.setOnClickListener(this)
        addGoalPlayer2.setOnClickListener(this)
        delGoalPlayer2.setOnClickListener(this)
        addGame.setOnClickListener(this)
        delGame.setOnClickListener(this)
        resetButton.setOnClickListener(this)
        saveButton.setOnClickListener(this)
        //Load SharedPreferences in ViewModel
        homeViewModel.goalsPlayer1 = prefs!!.getInt(keyGoalsPlayer1, 0)
        homeViewModel.goalsPlayer1Temp = prefs!!.getInt(keyGoalsPlayer1Temp, 0)
        homeViewModel.goalsPlayer2 = prefs!!.getInt(keyGoalsPlayer2, 0)
        homeViewModel.goalsPlayer2Temp = prefs!!.getInt(keyGoalsPlayer2Temp, 0)
        homeViewModel.gamesPlayed = prefs!!.getInt(keyGamesPlayed, 0)
        homeViewModel.goalProgress = prefs!!.getString(keyGoalProgress, "")!!
        homeViewModel.ellapsedGameTime = prefs!!.getLong(keyElapsedGameTime, 0L)
        homeViewModel.player1 = prefs!!.getString(keyPlayer1, "")!!
        homeViewModel.player2 = prefs!!.getString(keyPlayer2, "")!!
        gameDate = prefs!!.getLong(keyGameDate, 0L)
        //Show Text from SharedPreferences
        if (showSpoiler) {
            numberGoalsPlayer1.text = homeViewModel.goalsPlayer1.toString()
            numberGoalsPlayer2.text = homeViewModel.goalsPlayer2.toString()
        } else {
            numberGoalsPlayer1.text = homeViewModel.goalsPlayer1Temp.toString()
            numberGoalsPlayer2.text = homeViewModel.goalsPlayer2Temp.toString()
        }
        numberGamesPlayed.text = homeViewModel.gamesPlayed.toString()
        textGoalProgress.text = homeViewModel.goalProgress
        //Chronometer
        gameTime = requireView().findViewById(R.id.gameTime)
        if (homeViewModel.ellapsedGameTime != 0L) {
            gameTime.base = homeViewModel.ellapsedGameTime
            gameTime.start()
        }
        //set Date
        val tdate = Calendar.getInstance().time
        val simpletimeformat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
        date.text = simpletimeformat.format(tdate).toString()
        textPlayer1.text = homeViewModel.player1
        textPlayer2.text = homeViewModel.player2
    }

    override fun onClick(v: View?) {
        when (v) {
            addGoalPlayer1 -> {
                handleClick(0)
            }
            delGoalPlayer1 -> {
                handleClick(1)
            }
            addGoalPlayer2 -> {
                handleClick(2)
            }
            delGoalPlayer2 -> {
                handleClick(3)
            }
            addGame -> {
                handleClick(4)
            }
            delGame -> {
                handleClick(5)
            }
            resetButton -> {
                resetMe()
            }
            saveButton -> {
                saveResetMe()
            }
        }
    }

    private fun saveResetMe() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Speichern und Zurücksetzen?")
        builder.setMessage("Bist du sicher?")
        builder.setPositiveButton("Ja") { _, _ -> saveMe() }
        builder.setNegativeButton("Abbrechen") { _, _ -> }
        builder.create().show()
    }

    private fun saveMe() {
        Snackbar.make(requireView(), "Gespeichert und Zurückgesetzt", Snackbar.LENGTH_SHORT).show()
        //val mdate = System.currentTimeMillis()
        val tversion = prefs!!.getString("version_preference", "FIFA 21")
        var version = "FIFA 20"
        when(tversion){
            "0" -> version = "FIFA 20"
            "1" -> version = "FIFA 21"
            "2" -> version = "FIFA 22"
        }
        val dataViewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)
        val goalData = GoalData(
            null,
            homeViewModel.goalsPlayer1,
            homeViewModel.goalsPlayer2,
            homeViewModel.gamesPlayed,
            homeViewModel.goalProgress,
            gameDate,
            version
        )
        dataViewModel.insert(goalData)
        resetMe()
    }

    private fun updateMe() {
        //if Spoiler enabled, show total goals
        TransitionManager.beginDelayedTransition(home_view, AutoTransition().setDuration(1500L))
        /*val anim = AnimationUtils.loadAnimation(context, R.anim.slide_up)
        numberGoalsPlayer1.startAnimation(anim)
        numberGoalsPlayer2.startAnimation(anim)*/
        if (showSpoiler) {
            numberGoalsPlayer1.text = homeViewModel.goalsPlayer1.toString()
            numberGoalsPlayer2.text = homeViewModel.goalsPlayer2.toString()
        } else {
            numberGoalsPlayer1.text = homeViewModel.goalsPlayer1Temp.toString()
            numberGoalsPlayer2.text = homeViewModel.goalsPlayer2Temp.toString()
        }
        numberGamesPlayed.text = homeViewModel.gamesPlayed.toString()

        textGoalProgress.text = homeViewModel.goalProgress

        val editor = prefs!!.edit()
        editor.putInt(keyGoalsPlayer2, homeViewModel.goalsPlayer2)
        editor.putInt(keyGoalsPlayer2Temp, homeViewModel.goalsPlayer2Temp)
        editor.putInt(keyGoalsPlayer1, homeViewModel.goalsPlayer1)
        editor.putInt(keyGoalsPlayer1Temp, homeViewModel.goalsPlayer1Temp)
        editor.putInt(keyGamesPlayed, homeViewModel.gamesPlayed)
        editor.putLong(keyElapsedGameTime, homeViewModel.ellapsedGameTime)
        editor.putLong(keyGameDate, gameDate)
        editor.putString(keyGoalProgress, homeViewModel.goalProgress)
        editor.apply()
        listener?.onFragmentHomeInteraction(
            homeViewModel.goalsPlayer1,
            homeViewModel.goalsPlayer1Temp,
            homeViewModel.goalsPlayer2,
            homeViewModel.goalsPlayer2Temp,
            homeViewModel.gamesPlayed,
            homeViewModel.ellapsedGameTime,
            homeViewModel.goalProgress
        )
    }

    private fun handleClick(case: Int) {
        if (case == 0) {
            homeViewModel.goalsPlayer1++; homeViewModel.goalsPlayer1Temp++; homeViewModel.goalProgress += "a"; konfettiMe(
                Color.RED,
                Color.BLACK
            )
        }
        if (case == 1 && ((showSpoiler && homeViewModel.goalsPlayer1 > 0) || (!showSpoiler && homeViewModel.goalsPlayer1Temp > 0))) {
            homeViewModel.goalsPlayer1--; if (homeViewModel.goalsPlayer1Temp > 0) homeViewModel.goalsPlayer1Temp--; homeViewModel.goalProgress =
                deleteLetter(homeViewModel.goalProgress, "a")
        }
        if (case == 2) {
            homeViewModel.goalsPlayer2++; homeViewModel.goalsPlayer2Temp++; homeViewModel.goalProgress += "h"; konfettiMe(
                Color.BLUE,
                Color.WHITE
            )
        }
        if (case == 3 && ((showSpoiler && homeViewModel.goalsPlayer2 > 0) || (!showSpoiler && homeViewModel.goalsPlayer2Temp > 0))) {
            homeViewModel.goalsPlayer2--; if (homeViewModel.goalsPlayer2Temp > 0) homeViewModel.goalsPlayer2Temp--; homeViewModel.goalProgress =
                deleteLetter(homeViewModel.goalProgress, "h")
        }
        if (case == 4) {
            if (homeViewModel.gamesPlayed < 1) {
                gameDate = Calendar.getInstance().timeInMillis
            }
            homeViewModel.gamesPlayed++; homeViewModel.goalsPlayer2Temp =
                0; homeViewModel.goalsPlayer1Temp = 0; gameTime.base =
                SystemClock.elapsedRealtime(); gameTime.start(); homeViewModel.ellapsedGameTime =
                gameTime.base
        }
        if (case == 5 && homeViewModel.gamesPlayed > 0) {
            homeViewModel.gamesPlayed--
        }
        //updateText
        updateMe()
    }

    private fun deleteLetter(goalprogress: String, letter: String): String {
        var tempstring = goalprogress
        var index: Int
        if (letter == "a") {
            index = tempstring.lastIndexOf("a")
            tempstring = StringBuilder(tempstring).deleteCharAt(index).toString()
        }
        if (letter == "h") {
            index = tempstring.lastIndexOf("h")
            tempstring = StringBuilder(tempstring).deleteCharAt(index).toString()
        }
        return tempstring
    }

    private fun resetMe() {
        homeViewModel.goalsPlayer2 = 0
        homeViewModel.goalsPlayer2Temp = 0
        homeViewModel.goalsPlayer1 = 0
        homeViewModel.goalsPlayer1Temp = 0
        homeViewModel.gamesPlayed = 0
        homeViewModel.ellapsedGameTime = 0L
        homeViewModel.goalProgress = ""
        gameDate = 0L
        resetButton.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate))
        gameTime.base = SystemClock.elapsedRealtime()
        gameTime.stop()
        updateMe()
    }

    private fun konfettiMe(color1: Int, color2: Int) {
        val enabled = prefs!!.getBoolean("key_confetti_enabled", true)
        val konfettiamount = prefs!!.getInt("key_konfetti_amount", 0) * 7
        if (enabled) {
            viewKonfetti.build().addColors(color1, color2).setDirection(0.0, 359.0)
                .setSpeed(1f, 5f).setFadeOutEnabled(true).setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(Size(8)).setPosition(-50f, viewKonfetti.width + 50f, -50f, -50f)
                .streamFor((konfettiamount), 3000L)
        }
    }

    interface OnFragmentHomeInteractionListener {
        fun onFragmentHomeInteraction(
            mgoals_player1: Int,
            mgoals_player1_temp: Int,
            mgoals_player2: Int,
            mgoals_player2_temp: Int,
            mgames_played: Int,
            mgame_time: Long,
            mgoal_progress: String
        )
    }
}