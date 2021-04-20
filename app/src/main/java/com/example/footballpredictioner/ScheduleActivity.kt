package com.example.footballpredictioner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.footballpredictioner.adapters.ScheduleOuterAdapter
import com.example.footballpredictioner.api.TemporaryDataHolder
import com.example.footballpredictioner.models.ScheduleInnerModel
import com.example.footballpredictioner.models.ScheduleOuterModel
import com.squareup.picasso.Picasso

class ScheduleActivity : AppCompatActivity() {
    private lateinit var logo: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScheduleOuterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        logo = findViewById(R.id.scheduleImage)
        recyclerView = findViewById(R.id.scheduleRecyclerView)

        val logoUrl = intent.getStringExtra("logoUrl")
        Picasso.get().load(logoUrl).into(logo)

        adapter = ScheduleOuterAdapter(emptyList(), this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val currentSeason = intent.getStringExtra("currentSeason")

        adapter.dataSet = getOuterModels(currentSeason)
        adapter.notifyDataSetChanged()
    }

    private fun getInnerModels(currentSeason: String?, round: String) : List<ScheduleInnerModel> {
        val innerModels = mutableListOf<ScheduleInnerModel>()
        val matchString = TemporaryDataHolder.dataBaseHelper.getMatchesFromRound(round).dropLast(1)
        val splitMatches = matchString.split('\n')
        val mapOfUrls = TemporaryDataHolder.createMapToUrls(currentSeason)

        splitMatches.forEach { matchRecord ->
            val splitMatch = matchRecord.split(',')
            val homeUrl = mapOfUrls[splitMatch[0]]
            val awayUrl = mapOfUrls[splitMatch[1]]
            val innerModel = ScheduleInnerModel(splitMatch[2], splitMatch[3], homeUrl, awayUrl)

            innerModels.add(innerModel)
        }

        return innerModels
    }

    private fun getOuterModels(currentSeason: String?) : List<ScheduleOuterModel> {
        val outerModels = mutableListOf<ScheduleOuterModel>()
        val roundString = TemporaryDataHolder.dataBaseHelper.getRoundsFromSeason(currentSeason).dropLast(1)
        val splitRounds = roundString.split('\n')

        for (i in 0 until splitRounds.size - 1) {
            val innerModels = getInnerModels(currentSeason, splitRounds[i])
            val fixtureNumber = "Fixture no. %s".format((i+1).toString())
            val outerModel = ScheduleOuterModel(fixtureNumber, innerModels)

            outerModels.add(outerModel)
        }

        return outerModels
    }
}