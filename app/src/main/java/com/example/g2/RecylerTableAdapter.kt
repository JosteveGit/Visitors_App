package com.example.g2

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.g2.database.Visitors
import java.time.LocalDate
import java.time.LocalTime


class DateTimeComparison(val fromDate: String, val toDate: String, val fromTime: String, val toTime: String)

@RequiresApi(Build.VERSION_CODES.O)
class RecylerTableAdapter(
    private val items: List<Visitors>,
    private val dateTimeComparison: DateTimeComparison,
    private val context: Context
) : RecyclerView.Adapter<RecylerTableAdapter.Viewholder>() {

    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = null
        var phone: TextView? = null
        var time: TextView? = null

        init {
            name = itemView.findViewById(R.id.names)
            phone = itemView.findViewById(R.id.phones)
            time = itemView.findViewById(R.id.time)
        }
    }

    var newList = mutableListOf<Visitors>()

    init {
        val startDate = extractDate(dateTimeComparison.fromDate)
        val endDate = extractDate(dateTimeComparison.toDate)
        val startTime = extractTime(dateTimeComparison.fromTime)
        val endTime = extractTime(dateTimeComparison.toTime)
        items.forEach {
            val date = extractDate(it.date!!)
            val time = extractTime(it.time!!)
            if ((date.isAfter(startDate) || date.isEqual(startDate)) && (date.isBefore(endDate) || date.isEqual(
                    endDate
                ))
            ) {

                if ((time!!.isAfter(startTime) || (time.isEqual(startTime!!))) && (time.isBefore(endTime) || time.isEqual(
                        endTime!!
                    ))
                ){
                    newList.add(it)
                }
            }
        }
    }

    fun setNewDateTimeComparison(value: DateTimeComparison){
        newList.clear()
        val startDate = extractDate(value.fromDate)
        val endDate = extractDate(value.toDate)
        val startTime = extractTime(value.fromTime)
        val endTime = extractTime(value.toTime)
        items.forEach {
            val date = extractDate(it.date!!)
            val time = extractTime(it.time!!)
            if ((date.isAfter(startDate) || date.isEqual(startDate)) && (date.isBefore(endDate) || date.isEqual(
                    endDate
                ))
            ) {

                if ((time!!.isAfter(startTime) || (time.isEqual(startTime!!))) && (time.isBefore(endTime) || time.isEqual(
                        endTime!!
                    ))
                ){
                    newList.add(it)
                }
            }
        }
    }

    fun LocalTime.isEqual(value: LocalTime):Boolean{
        return (this.minute==value.minute&&this.hour==value.hour)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.adapter, parent, false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int = newList.size

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.name?.text = newList.get(position).name
        holder.phone?.text = newList.get(position).phoneNumber
        holder.time?.text = newList.get(position).time
    }

    @SuppressLint("NewApi")
    fun extractDate(value: String): LocalDate {
        val arrayOfData: List<String> = value.split(" ")
        val day = arrayOfData[0].toInt()
        val month = getMonthValue(arrayOfData[1])
        val year = arrayOfData[2].toInt()
        return LocalDate.of(year, month, day)
    }

    fun extractTime(value: String): LocalTime?{
        val arrayOfData = value.split(":"," ")
        val hour = arrayOfData[0]
        val minute = arrayOfData[1]
        val type = arrayOfData[2]
        var newHour: Int?= null
        if(hour=="12" && type =="AM"){
            newHour = 0
        }else if(hour=="12" && type =="PM"){
            newHour = 12
        }else if(type=="PM"){
            newHour = hour.toInt() + 12
        }else{
            newHour = hour.toInt()
        }
        return LocalTime.of(newHour,minute.toInt())
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
}