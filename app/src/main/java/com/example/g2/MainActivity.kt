package com.example.g2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Handler().apply{
            postDelayed({
                startActivity(Intent(this@MainActivity,TheRealMainActivity::class.java))
                finish()

            },2000)
        }
    }
}
