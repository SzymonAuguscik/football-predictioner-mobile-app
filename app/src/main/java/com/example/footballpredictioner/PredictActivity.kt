package com.example.footballpredictioner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.footballpredictioner.adapters.PredictionAdapter
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

        val logoUrl = intent.getStringExtra("logoUrl")
        Picasso.get().load(logoUrl).into(predictionLogo)
    }
}