package com.example.footballpredictioner.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.footballpredictioner.R
import com.example.footballpredictioner.api.TemporaryDataHolder
import com.example.footballpredictioner.models.MatchModel

@Suppress("NAME_SHADOWING")
class LastFiveAdapter(private val chosenTeamLast5Matches:ArrayList<MatchModel>, private val chosenTeamName:String? ,val context: Context) : BaseAdapter() {
    override fun getCount(): Int {
        return chosenTeamLast5Matches.size
    }

    override fun getItem(position: Int): MatchModel {
        return chosenTeamLast5Matches[position]
    }

    override fun getItemId(position: Int): Long {
        return chosenTeamLast5Matches[position].id
    }

    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val convertView = inflater.inflate(R.layout.match_row,null)
        val localTeam = convertView.findViewById<TextView>(R.id.local_team)
        val matchResult = convertView.findViewById<TextView>(R.id.match_result)
        val visitorTeam = convertView.findViewById<TextView>(R.id.visitor_team)

        localTeam.text = TemporaryDataHolder.dataBaseHelper.getTeamNameById(chosenTeamLast5Matches[position].localTeamId)
        matchResult.text = "${chosenTeamLast5Matches[position].localTeamScore}:${chosenTeamLast5Matches[position].visitorTeamScore}"
        visitorTeam.text = TemporaryDataHolder.dataBaseHelper.getTeamNameById(chosenTeamLast5Matches[position].visitorTeamId)


        if(chosenTeamName == localTeam.text)
            visitorTeam.setTextColor(Color.parseColor("#333333"))
        else
            localTeam.setTextColor(Color.parseColor("#333333"))

        return convertView
    }
}