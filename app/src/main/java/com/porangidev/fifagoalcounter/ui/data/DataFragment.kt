package com.porangidev.fifagoalcounter.ui.data

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.porangidev.fifagoalcounter.GoalDataAdapter
import com.porangidev.fifagoalcounter.R
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import com.porangidev.fifagoalcounter.goToFileIntent
import java.io.File
import java.util.*


class DataFragment : Fragment() {

    val keyGoalsAlex = "com.porangidev.fifagoalcounter.ui.data.EXTRA_GOALS_ALEX"
    val keyGoalsHendrik = "com.porangidev.fifagoalcounter.ui.data.EXTRA_GOALS_HENDRIK"
    val keyGamesPlayed = "com.porangidev.fifagoalcounter.ui.data.EXTRA_GAMES_PLAYED"
    val keyGoalsProgress = "com.porangidev.fifagoalcounter.ui.data.EXTRA_GOALS_PROGRESS"
    val keyDate = "com.porangidev.fifagoalcounter.ui.data.EXTRA_DATE"
    val keyId = "com.porangidev.fifagoalcounter.ui.data.EXTRA_ID"
    val keyVersion = "com.porangidev.fifagoalcounter.ui.data.EXTRA_VERSION"


    private var keyPlayer1 = "key_player_1"
    private var keyPlayer2 = "key_player_2"

    private lateinit var textalex: TextView
    private lateinit var texttextplayer1: TextView
    private lateinit var texthendrik: TextView
    private lateinit var texttextplayer2: TextView

    private lateinit var dataViewModel: DataViewModel
    private lateinit var adapter: GoalDataAdapter

    private var prefs: SharedPreferences? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_data, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.data_view)
        adapter = GoalDataAdapter()
        val fabAddGoalData: FloatingActionButton = root.findViewById(R.id.fab_addGoalData)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.addOnItemTouchListener(
            RecyclerItemListener(
                requireContext(),
                recyclerView,
                object : RecyclerItemListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        prefs = PreferenceManager.getDefaultSharedPreferences(context)
                        texttextplayer1 = view.findViewById(R.id.text_view_text_player1)
                        texttextplayer2 = view.findViewById(R.id.text_view_text_player2)
                        val player1 = "Tore " + prefs!!.getString(keyPlayer1, "")
                        val player2 = "Tore " + prefs!!.getString(keyPlayer2, "")
                        texttextplayer1.text = player1
                        texttextplayer2.text = player2
                        textalex = view.findViewById(R.id.text_view_goals_alex)
                        texthendrik = view.findViewById(R.id.text_view_goals_hendrik)
                        if (textalex.visibility == View.GONE) {
                            TransitionManager.beginDelayedTransition(
                                recyclerView,
                                AutoTransition().setDuration(130L)
                            )
                            textalex.visibility = View.VISIBLE
                            texttextplayer1.visibility = View.VISIBLE
                            texthendrik.visibility = View.VISIBLE
                            texttextplayer2.visibility = View.VISIBLE
                        } else {
                            TransitionManager.beginDelayedTransition(
                                recyclerView,
                                AutoTransition().setDuration(130L)
                            )
                            textalex.visibility = View.GONE
                            texttextplayer1.visibility = View.GONE
                            texthendrik.visibility = View.GONE
                            texttextplayer2.visibility = View.GONE
                        }
                    }

                    override fun onItemLongClick(view: View?, position: Int) {
                        val currentGoalData = adapter.getGoalDataAt(position)
                        val bundle = Bundle()
                        bundle.putInt(keyId, currentGoalData.id!!)
                        bundle.putLong(keyDate, currentGoalData.playDate)
                        bundle.putInt(keyGamesPlayed, currentGoalData.playedGames)
                        bundle.putInt(keyGoalsAlex, currentGoalData.goalsAlex)
                        bundle.putInt(keyGoalsHendrik, currentGoalData.goalsHendrik)
                        bundle.putString(keyGoalsProgress, currentGoalData.goalsProgress)
                        bundle.putString(keyVersion, currentGoalData.fifa_version)
                        view!!.findNavController().navigate(R.id.action_nav_data_to_adddata, bundle)
                    }
                })
        )
        fabAddGoalData.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_nav_data_to_adddata)
        }
        dataViewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)
        dataViewModel.getAllSessions().observe(viewLifecycleOwner, Observer { GoalData ->
            GoalData?.let { adapter.setGoalData(it) }
        })

        val mIth = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT /*or ItemTouchHelper.RIGHT*/
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: ViewHolder,
                    target: ViewHolder
                ): Boolean {
                    //val fromPos = viewHolder.adapterPosition
                    //val toPos = target.adapterPosition
                    // move item in `fromPos` to `toPos` in adapter.
                    return false// true if moved, false otherwise
                }

                override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                    if (direction == ItemTouchHelper.LEFT) {
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("Daten löschen?")
                        builder.setMessage("Bist du sicher?")
                        builder.setPositiveButton("Ja") { _, _ -> deleteEntry(viewHolder.adapterPosition) }
                        builder.setNegativeButton("Abbrechen") { _, _ -> adapter.notifyDataSetChanged()}
                        builder.create().show()
                    }
                }
            })
        mIth.attachToRecyclerView(recyclerView)
        setHasOptionsMenu(true)
        return root
    }

    private fun deleteEntry(adapterPosition : Int){
        // remove from adapter
        dataViewModel.delete(adapter.getGoalDataAt(adapterPosition))
        Toast.makeText(context, "Eintrag gelöscht", Toast.LENGTH_SHORT).show()
    }

    private fun deleteAll() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Daten löschen?")
        builder.setMessage("Bist du sicher?")
        builder.setPositiveButton("Ja") { _, _ -> dataViewModel.deleteAllEntries(); Snackbar.make(requireView(), "Alle Einträge gelöscht", Snackbar.LENGTH_SHORT)
            .setAction("Action", null).show()}
        builder.setNegativeButton("Abbrechen") { _, _ -> adapter.notifyDataSetChanged()}
        builder.create().show()
    }

    private fun exportDB() {
        val backupDate = Calendar.getInstance().timeInMillis
        val csvFileName = "db_Backup_$backupDate.csv"
        val csvFile = File(requireContext().filesDir, csvFileName)
        csvFile.createNewFile()
        if(csvFile.exists()){
            val intent = goToFileIntent(requireContext(), csvFile)
            startActivity(intent)
        }
        else{
            Toast.makeText(requireContext(), "Export fehlgeschlagen", Toast.LENGTH_LONG).show()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.datamenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_all -> {
                deleteAll(); return true
            }
            R.id.action_export -> {
                exportDB(); return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}