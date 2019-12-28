package com.example.g2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_fill_data.*

class FillDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_data)

        val phone: String? = intent.getStringExtra("Phone Number")

        nextToImageCaputeButton.setOnClickListener{
            val name: String = fullName.text.toString().trim()
            val address: String = contactAddress.text.toString().trim()
            val carRegNum: String = carRegNumer.text.toString().trim()
            val whoToSee: String = whoToSee.text.toString().trim()
            val dept: String = unitOrDept.text.toString().trim()
            val details: List<String> = mutableListOf(name,address,whoToSee,dept)

            val anyEmpty = details.any { it.isEmpty() }
            if(anyEmpty){
                Toast.makeText(this@FillDataActivity,"Fill all required fields",Toast.LENGTH_LONG).show()
            }else{
                val intent: Intent = Intent(this@FillDataActivity,ImageCaptureActivity::class.java).apply {
                    putExtra("Phone Number",phone)
                    putExtra("Full Name",name)
                    putExtra("Contact Address",address)
                    putExtra("Who To See",whoToSee)
                    putExtra("Car Reg Number",if(carRegNum.isEmpty())"None" else carRegNum)
                    putExtra("dept",dept)
                }
                startActivity(intent)
            }

        }
    }
}
