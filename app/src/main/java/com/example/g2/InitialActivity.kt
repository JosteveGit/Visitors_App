package com.example.g2

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.g2.database.Visitors
import com.example.g2.database.VisitorsViewModel
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.android.synthetic.main.activity_initial.*
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class InitialActivity : AppCompatActivity() {

    private lateinit var visitorsViewModel: VisitorsViewModel

    var rec: RecylerTableAdapter? = null
    private val STORAGE_CODE: Int = 100;
    private var listOfVisitors = mutableListOf<Visitors>()


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        recyclerTable.setHasFixedSize(true)
        recyclerTable.layoutManager = LinearLayoutManager(this)


        visitorsViewModel = ViewModelProvider(this).get(VisitorsViewModel::class.java)
        visitorsViewModel.allWords.observe(this, Observer { words ->
            words?.let {
                listOfVisitors = it.toMutableList()
                myVal.text = "${filterList().size}\nVisitors"
                rec = RecylerTableAdapter(
                    it, DateTimeComparison(
                        dateFrom.text.toString(),
                        dateTo.text.toString(),
                        timeFrom.text.toString(),
                        timeTo.text.toString()
                    ), applicationContext
                )

                recyclerTable.adapter = rec
            }
        })

        //set initials
        setInitials()
        //listeners
        datePickerFrom.setOnClickListener {
            displayDatePicker(dateFrom)
        }
        datePickerTo.setOnClickListener {
            displayDatePicker(dateTo)

        }
        timePickerTo.setOnClickListener { displayTimePicker(timeTo) }
        timePickerFrom.setOnClickListener { displayTimePicker(timeFrom) }
        back.setOnClickListener { super.onBackPressed() }

        generateReport.setOnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    //permission not granted, request it
                    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, STORAGE_CODE)
                } else {
                    savePDF()
                }
            } else {
                savePDF()
            }
        }
    }


    private fun savePDF() {
        val document = Document()
        val fileName = "King Riverside Report" + SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(System.currentTimeMillis())
        val filePath =
            Environment.getExternalStorageDirectory().toString() + "/" + fileName + ".pdf"
        try {
            PdfWriter.getInstance(document, FileOutputStream(filePath))
            document.open()

            val newList = filterList()
            //Everything will be here
            val header = "King Riverside Visitors Report"
            document.add(Paragraph(header))
            val dateRange = "Date Range: From ${dateFrom.text} To ${dateTo.text}"
            document.add(Paragraph(dateRange))
            val timeRange = "Time Range: From ${timeFrom.text} To ${timeTo.text}"
            document.add(Paragraph(timeRange + "\n"))


            var counter = 0
            filterList().forEach {
                counter++
                document.add(Paragraph("$counter) Name: ${it.name}      Phone Number: ${it.phoneNumber}     Address: ${it.address}      Purpose:${it.purposeOfvisit}"))
            }


            document.addAuthor("King Riverside")
            document.close()

            Toast.makeText(this, "$fileName.pdf\nis saved to\n$filePath", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    savePDF()
                } else {
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun setInitials() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val time = "12:00 AM"
        val endTime = "11:59 PM"
        val dateStartAndEnd = "$day ${getMonth(month)} $year"
        dateFrom.text = dateStartAndEnd
        dateTo.text = dateStartAndEnd
        timeFrom.text = time
        timeTo.text = endTime
    }

    @SuppressLint("NewApi")
    fun filterList(): List<Visitors> {
        val dateTimeComparison = DateTimeComparison(
            dateFrom.text.toString(),
            dateTo.text.toString(),
            timeFrom.text.toString(),
            timeTo.text.toString()
        )
        val startDate = extractDate(dateTimeComparison.fromDate)
        val endDate = extractDate(dateTimeComparison.toDate)
        val startTime = extractTime(dateTimeComparison.fromTime)
        val endTime = extractTime(dateTimeComparison.toTime)
        val newList: MutableList<Visitors> = mutableListOf()
        listOfVisitors.forEach {
            val date = extractDate(it.date!!)
            val time = extractTime(it.time!!)
            if ((date.isAfter(startDate) || date.isEqual(startDate)) && (date.isBefore(endDate) || date.isEqual(
                    endDate
                ))
            ) {

                if ((time!!.isAfter(startTime) || (time.isEqual(startTime!!))) && (time.isBefore(
                        endTime
                    ) || time.isEqual(
                        endTime!!
                    ))
                ) {
                    newList.add(it)
                }
            }
        }
        return newList
    }

    @SuppressLint("NewApi")
    fun LocalTime.isEqual(value: LocalTime): Boolean {
        return (this.minute == value.minute && this.hour == value.hour)
    }

    @SuppressLint("NewApi")
    fun extractTime(value: String): LocalTime? {
        val arrayOfData = value.split(":", " ")
        val hour = arrayOfData[0]
        val minute = arrayOfData[1]
        val type = arrayOfData[2]
        var newHour: Int? = null
        if (hour == "12" && type == "AM") {
            newHour = 0
        } else if (hour == "12" && type == "PM") {
            newHour = 12
        } else if (type == "PM") {
            newHour = hour.toInt() + 12
        } else {
            newHour = hour.toInt()
        }
        return LocalTime.of(newHour, minute.toInt())
    }

    @SuppressLint("NewApi")
    fun extractDate(value: String): LocalDate {
        val arrayOfData: List<String> = value.split(" ")
        val day = arrayOfData[0].toInt()
        val month = getMonthValue(arrayOfData[1])
        val year = arrayOfData[2].toInt()
        return LocalDate.of(year, month, day)
    }

    private fun getMonthValue(value: String): Int = when (value) {
        "Jan" -> 1
        "Feb" -> 2
        "Mar" -> 3
        "Apr" -> 4
        "May" -> 5
        "Jun" -> 6
        "Jul" -> 7
        "Aug" -> 8
        "Sep" -> 9
        "Oct" -> 10
        "Nov" -> 11
        "Dec" -> 12
        else -> 0
    }

    @SuppressLint("NewApi")
    fun displayDatePicker(setter: TextView) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, yearOfTheDecade, monthOfTheYear, dayOfTheMonth ->
                setter.text = "$dayOfTheMonth ${getMonth(monthOfTheYear)} $yearOfTheDecade"
                myVal.text = "${filterList().size}\nVisitors"
                rec?.setNewDateTimeComparison(
                    DateTimeComparison(
                        dateFrom.text.toString(),
                        dateTo.text.toString(),
                        timeFrom.text.toString(),
                        timeTo.text.toString()
                    )
                )
            }, year, month, day
        ).apply {
            show()
        }
    }

    @SuppressLint("NewApi")
    fun displayTimePicker(setter: TextView) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            setter.text = SimpleDateFormat("hh:mm a").format(cal.time)
            myVal.text = "${filterList().size}\nVisitors"

            rec?.setNewDateTimeComparison(
                DateTimeComparison(
                    dateFrom.text.toString(),
                    dateTo.text.toString(),
                    timeFrom.text.toString(),
                    timeTo.text.toString()
                )
            )
        }
        TimePickerDialog(
            this,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            false
        ).show()
    }

    val getMonth = fun(value: Int): String = when (value) {
        0 -> "Jan"
        1 -> "Feb"
        2 -> "Mar"
        3 -> "Apr"
        4 -> "May"
        5 -> "Jun"
        6 -> "Jul"
        7 -> "Aug"
        8 -> "Sep"
        9 -> "Oct"
        10 -> "Nov"
        11 -> "Dec"
        else -> "Error"
    }
}


//@RequiresApi(Build.VERSION_CODES.O)
//@SuppressLint("NewApi")
//fun compareTime(){
//    val startDate = LocalDate.of(2016,10,31)
//    val endDate = LocalDate.of(2018,10,23)
//
//    val inputDates = listOf(
//        LocalDate.of(2017,3,2),
//        LocalDate.now()
//    ).forEach{
//        if((it.isAfter(startDate)||it.equals(startDate))&&(it.equals(endDate)||it.isBefore(endDate))){
//            //display it
//        }
//    }
//
//
//}