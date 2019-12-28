package com.example.g2

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_change.*
import java.util.*

class ChangeActivity : AppCompatActivity() {

    fun dis(auto: AutoCompleteTextView){
        if(Build.VERSION.SDK_INT>=16){
            auto.setRawInputType(InputType.TYPE_NULL)
            auto.setTextIsSelectable(true)
        }else{
            auto.setRawInputType(InputType.TYPE_NULL)
            auto.isFocusable = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change)

        //push to the capture page to generate ID

        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, listOf("Male","Female"))
        gender.setAdapter(adapter)

        val timer = object : TimerTask() {
            override fun run() {
                dis(gender)
            }
        }



        Timer().schedule(timer,0,1000L)

        proceedButton.setOnClickListener {
            if (anyEmpty(
                    name.text.toString(),
                    gender.text.toString(),
                    phoneNumber.text.toString(),
                    address.text.toString(),
                    stateOfOrigin.text.toString(),
                    nationality.text.toString(),
                    occupation.text.toString(),
                    arrivingFrom.text.toString(),
                    purposeOfVisit.text.toString(),
                    accompanyWith.text.toString()
                )
            ) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_LONG).show()
            } else {
                startActivity(Intent(this@ChangeActivity, ImageCaptureActivity::class.java).apply {
                    putExtra("name", name.text.toString())
                    putExtra("nationality", nationality.text.toString())
                    putExtra("phoneNumber", phoneNumber.text.toString())
                    putExtra("address", address.text.toString())
                    putExtra("purpose",purposeOfVisit.text.toString())
                })
                finish()
            }
        }

    }

    private fun anyEmpty(vararg values: String): Boolean {
        return values.any { it.trim() == "" };
    }
}
