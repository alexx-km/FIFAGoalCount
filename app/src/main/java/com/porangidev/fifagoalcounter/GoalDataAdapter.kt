package com.porangidev.fifagoalcounter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GoalDataAdapter : RecyclerView.Adapter<GoalDataAdapter.GoalDataHolder>() {

    private var goaldata = emptyList<GoalData>()

    fun setGoalData(goaldata: List<GoalData>){
        this.goaldata = goaldata
        notifyDataSetChanged()
    }

    fun getGoalDataAt (position: Int) : GoalData{
        return goaldata[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalDataHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.goaldata_item, parent, false)

        return GoalDataHolder(v)
    }

    override fun getItemCount(): Int {
        return goaldata.size
    }

    override fun onBindViewHolder(holder: GoalDataHolder, position: Int) {
        var currentGoalData : GoalData = goaldata[position]
        holder.textviewdate.text = DateFormat.format("dd/MM/yyyy", currentGoalData.playDate).toString()
        holder.textviewgamesplayed.text = currentGoalData.playedGames.toString()
        holder.textviewgoalsscored.text = currentGoalData.getGoals().toString()
        holder.textviewgoalalex.text = currentGoalData.goalsAlex.toString()
        holder.textviewgoalhendrik.text = currentGoalData.goalsHendrik.toString()
    }

    class GoalDataHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textviewdate = itemView.findViewById<TextView>(R.id.text_view_date)
        var textviewgamesplayed = itemView.findViewById<TextView>(R.id.text_view_gamesplayed)
        var textviewgoalsscored = itemView.findViewById<TextView>(R.id.text_view_goalsscored)
        var textviewgoalalex = itemView.findViewById<TextView>(R.id.text_view_goals_alex)
        var textviewgoalhendrik = itemView.findViewById<TextView>(R.id.text_view_goals_hendrik)
    }
}