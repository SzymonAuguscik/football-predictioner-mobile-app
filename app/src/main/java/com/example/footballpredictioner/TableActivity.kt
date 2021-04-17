package com.example.footballpredictioner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}