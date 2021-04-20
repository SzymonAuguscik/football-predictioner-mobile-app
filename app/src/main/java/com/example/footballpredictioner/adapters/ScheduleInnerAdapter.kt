package com.example.footballpredictioner.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.footballpredictioner.R
import com.example.footballpredictioner.models.ScheduleInnerModel
import com.squareup.picasso.Picasso

class ScheduleInnerAdapter(var dataSet: List<ScheduleInnerModel>, var context: Context): RecyclerView.Adapter<ScheduleInnerAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val homeTeamLogo: ImageView
        val awayTeamLogo: ImageView
        val scoreTextView: TextView

        init {
            homeTeamLogo = view.findViewById(R.id.scheduleInnerHomeLogo)
            awayTeamLogo = view.findViewById(R.id.scheduleInnerAwayLogo)
            scoreTextView = view.findViewById(R.id.scheduleInnerScore)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.schedule_inner_view, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val innerModel = dataSet[position]

        Picasso.get().load(innerModel.url1).into(viewHolder.homeTeamLogo)
        Picasso.get().load(innerModel.url2).into(viewHolder.awayTeamLogo)
        viewHolder.scoreTextView.text = "%s : %s".format(innerModel.score1, innerModel.score2)
    }

    override fun getItemCount() = dataSet.size
}
