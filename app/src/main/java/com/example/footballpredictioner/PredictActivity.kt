package com.example.footballpredictioner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.footballpredictioner.adapters.PredictionAdapter
import com.example.footballpredictioner.api.TemporaryDataHolder
import com.example.footballpredictioner.models.SinglePredictionRowModel
import com.squareup.picasso.Picasso

class PredictActivity : AppCompatActivity() {
    private lateinit var predictionLogo: ImageView
    private lateinit var predictionButton: Button
    private lateinit var predictionRecyclerView: RecyclerView
    private lateinit var adapter: PredictionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predict)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        predictionLogo = findViewById(R.id.predictLogo)
        predictionButton = findViewById(R.id.predictionDetailsButton)
        predictionRecyclerView = findViewById(R.id.predictionRecyclerView)

        adapter = PredictionAdapter(arrayOf(), this)
        predictionRecyclerView.adapter = adapter
        predictionRecyclerView.layoutManager = LinearLayoutManager(this)

        val logoUrl = intent.getStringExtra("logoUrl")
        Picasso.get().load(logoUrl).into(predictionLogo)

        val currentSeason = intent.getStringExtra("currentSeason")
        val matches = intent.getStringExtra("matches")
        val predictions = intent.getStringExtra("predictions")

        adapter.dataSet = mapMatches(currentSeason, matches, predictions)
        adapter.notifyDataSetChanged()

        predictionButton.setOnClickListener {
            val intent = Intent(this, PredictingDetailsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun mapMatches(currentSeason: String?, matchesString: String?, predictionsString: String?) : Array<SinglePredictionRowModel> {
        val teamsMap = TemporaryDataHolder.createMapToMatchesTable(currentSeason)
        val predictionRecords = mutableListOf<SinglePredictionRowModel>()
        val predictionLabels = mapOf("0" to "home team wins", "1" to "draw", "2" to "away team wins")

        val splitMatches = matchesString?.split('\n')
        val splitPredictions = predictionsString?.split('\n')

        if (splitMatches != null) {
            for (i in 0 until splitMatches.size - 1) {
                val teams = splitMatches[i].split(',')
                val predictions = splitPredictions?.get(i)?.split(',')
                val date = TemporaryDataHolder.dataBaseHelper.getDateOfMatch(teams[0], teams[1]).dropLast(1)

                val firstPrediction = "First prediction: %s".format(predictionLabels[predictions?.get(0)])
                val secondPrediction = "Second prediction: %s".format(predictionLabels[predictions?.get(1)])
                val thirdPrediction = "Third prediction: %s".format(predictionLabels[predictions?.get(2)])

                val homeTeamUrl = TemporaryDataHolder.dataBaseHelper.getLogoById(teams[0]).dropLast(1)
                val awayTeamUrl = TemporaryDataHolder.dataBaseHelper.getLogoById(teams[1]).dropLast(1)

                if (teams[0] in teamsMap) {
                    val singlePrediction = SinglePredictionRowModel(date, teamsMap[teams[0]], teamsMap[teams[1]],
                        firstPrediction, secondPrediction, thirdPrediction, homeTeamUrl, awayTeamUrl)

                    predictionRecords.add(singlePrediction)
                }
            }

            return predictionRecords.toTypedArray()
        }

        return emptyArray()
    }
}