package com.example.g2.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visitors")
class Visitors(
    val name: String?, val phoneNumber: String?,
    val gender: String?,
    val address: String?,
    val stateOfOrigin: String?,
    val nationality: String?,
    val occupation: String?,
    val arrivingFrom: String?,
    val purposeOfvisit: String?,
    val accompanyWith: String?,
    val date: String?,
    val time: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}

enum class Gender {
    Male,
    Female
}