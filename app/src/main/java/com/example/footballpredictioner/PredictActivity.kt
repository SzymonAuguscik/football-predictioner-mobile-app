package com.example.footballpredictioner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PredictActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predict)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}