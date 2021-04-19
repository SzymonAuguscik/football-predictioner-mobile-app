package com.example.footballpredictioner.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.footballpredictioner.R
import com.example.footballpredictioner.models.TeamModel
import com.squareup.picasso.Picasso

class TableAdapter(var dataSet: ArrayList<TeamModel>, var context: Context): RecyclerView.Adapter<TableAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val positionInLeague: TextView = view.findViewById(R.id.position_in_league)
        val teamLogo: ImageView = view.findViewById(R.id.logo_in_table)
        val teamName: TextView = view.findViewById(R.id.team_name)
        val basicTeamStats: TextView = view.findViewById(R.id.basic_team_stats)
        val teamPointsInLeague: TextView = view.findViewById(R.id.points_in_league)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.single_team, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val singleTeam = dataSet[position]
        println(position)
        Picasso.get().load(singleTeam.logoPath).into(viewHolder.teamLogo)
        viewHolder.positionInLeague.text = position.toString()
        viewHolder.teamName.text = singleTeam.name

        val singleTeamPlayedMatches = singleTeam.wins + singleTeam.draws + singleTeam.loses
        val statsString = "$singleTeamPlayedMatches • ${singleTeam.wins} • ${singleTeam.draws} • ${singleTeam.loses}"
        viewHolder.basicTeamStats.text = statsString

        viewHolder.teamPointsInLeague.text = (singleTeam.draws + (singleTeam.wins * 3)).toString()
    }

    override fun getItemCount() = dataSet.size
}