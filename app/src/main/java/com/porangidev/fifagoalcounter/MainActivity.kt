package com.porangidev.fifagoalcounter

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.navigation.NavigationView
import com.porangidev.fifagoalcounter.ui.home.HomeFragment

class MainActivity : AppCompatActivity(), HomeFragment.OnFragmentHomeInteractionListener {
    //Shared Preferences
    private lateinit  var prefs: SharedPreferences
    private var pref_key = "com.porangidev.fifagoalcounter.prefs"
    //Saved Data
    var goalsHendrik = 0
    var goalsHendrikTemp = 0
    var goalsAlex = 0
    var goalsAlexTemp = 0
    var gamesPlayed = 0
    var elapsedGameTime = 0L
    var goalProgress = ""
    //Data keys
    var keyPlayer1 = "key_player_1"
    var keyPlayer2 = "key_player_2"
    var keyGoalsHendrik = "KEY_GOALS_HENDRIK_PREFS"
    var keyGoalsHendrikTemp = "KEY_GOALS_HENDRIK_TEMP_PREFS"
    var keyGoalsAlex = "KEY_GOALS_ALEX_PREFS"
    var keyGoalsAlexTemp = "KEY_GOALS_ALEX_TEMP_PREFS"
    var keyGamesPlayed = "KEY_GAMES_PLAYED_PREFS"
    var keyElapsedGameTime = "KEY_ELAPSED_GAME_TIME_PREFS"
    var keyGoalProgress = "KEY_GOAL_PROGRESS_PREFS"

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var repository: GoalRepository
    private lateinit var goalDataAdapter: GoalDataAdapter
    private lateinit var listtotalgoals: ArrayList<PieEntry>
    private var totalgames = 0
    private lateinit var listquotaplayer1: ArrayList<Entry>
    private lateinit var listquotaplayer2: ArrayList<Entry>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        //Load Data from Shared Preferences to local variables
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        //prefs = this.getSharedPreferences(pref_key, 0)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_stats, R.id.nav_data,
                R.id.nav_settings, R.id.nav_share
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        setUpDatabase()
        preparePieData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onFragmentHomeInteraction(
        mgoals_alex: Int,
        mgoals_alex_temp: Int,
        mgoals_hendrik: Int,
        mgoals_hendrik_temp: Int,
        mgames_played: Int,
        mgame_time: Long,
        mgoal_progress: String
    ) {
        goalsAlex = mgoals_alex
        goalsAlexTemp = mgoals_alex_temp
        goalsHendrik = mgoals_hendrik
        goalsHendrikTemp = mgoals_hendrik_temp
        gamesPlayed = mgames_played
        elapsedGameTime = mgame_time
        goalProgress = mgoal_progress
        val editor = prefs.edit()
        editor.putInt(keyGoalsHendrik, goalsHendrik)
        editor.putInt(keyGoalsHendrikTemp, goalsHendrikTemp)
        editor.putInt(keyGoalsAlex, goalsAlex)
        editor.putInt(keyGoalsAlexTemp, goalsAlexTemp)
        editor.putInt(keyGamesPlayed, gamesPlayed)
        editor.putLong(keyElapsedGameTime, elapsedGameTime)
        editor.putString(keyGoalProgress, goalProgress)
        editor.apply()
    }

    fun setUpDatabase(){
        listtotalgoals = ArrayList()
        val goalDao = GoalDatabase.getInstance(application).goalDao()
        repository = GoalRepository(goalDao)
        goalDataAdapter = GoalDataAdapter()

        listquotaplayer1 = ArrayList<Entry>()
        listquotaplayer2 = ArrayList<Entry>()
    }

    fun preparePieData(){
        val player1 = prefs.getString(keyPlayer1, "")
        val player2 = prefs.getString(keyPlayer2, "")
        val (tlistquotaplayer1, tlistquotaplayer2, ttotalgames, tlisttotalgoals) = DataHandler().preparePieData(repository, goalDataAdapter, player1, player2)
        listquotaplayer1 = tlistquotaplayer1
        listquotaplayer2 = tlistquotaplayer2
        totalgames = ttotalgames
        listtotalgoals = tlisttotalgoals
    }

    fun getPieData(): ArrayList<PieEntry> {
        return listtotalgoals
    }

    fun getTotalGames(): Int {
        return totalgames
    }

    fun getQuotaDataPlayer1(): ArrayList<Entry>{
        return listquotaplayer1
    }

    fun getQuotaDataPlayer2(): ArrayList<Entry>{
        return listquotaplayer2
    }
}
