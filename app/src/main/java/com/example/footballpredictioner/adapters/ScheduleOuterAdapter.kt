package com.example.footballpredictioner.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.footballpredictioner.R
import com.example.footballpredictioner.models.ScheduleOuterModel

class ScheduleOuterAdapter(var dataSet: List<ScheduleOuterModel>, var context: Context): RecyclerView.Adapter<ScheduleOuterAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView
        val recyclerView: RecyclerView

        init {
            dateTextView = view.findViewById(R.id.scheduleFixtureTextView)
            recyclerView = view.findViewById(R.id.scheduleNestedRecyclerView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.schedule_outer_view, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val outerModel = dataSet[position]
        val innerAdapter = ScheduleInnerAdapter(outerModel.innerList, context)
        val linearLayoutManager = LinearLayoutManager(context)

        viewHolder.recyclerView.layoutManager = linearLayoutManager
        viewHolder.recyclerView.adapter = innerAdapter
        viewHolder.dateTextView.text = outerModel.fixtureNumber
    }

    override fun getItemCount() = dataSet.size
}
