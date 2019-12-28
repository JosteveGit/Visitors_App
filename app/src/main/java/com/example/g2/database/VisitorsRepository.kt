package com.example.g2.database

import androidx.lifecycle.LiveData

class VisitorsRepository (private val wordDao: VisitorsDao){
    val allWords: LiveData<List<Visitors>> = wordDao.getAllWords()
    suspend fun insert(word: Visitors){
        wordDao.insert(vistior = word)
    }
}