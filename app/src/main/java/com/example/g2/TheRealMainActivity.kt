package com.example.g2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_the_real_main.*

class TheRealMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_the_real_main)

        generateReport.setOnClickListener {
            startActivity(Intent(this,InitialActivity::class.java))
        }

        addVisitors.setOnClickListener {
            startActivity(Intent(this,ChangeActivity::class.java))
        }
    }
}
