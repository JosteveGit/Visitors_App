package com.example.g2.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class VisitorsViewModel(application: Application)  : AndroidViewModel(application){
    private val repository: VisitorsRepository
    val allWords: LiveData<List<Visitors>>

    init {
        val wordsDao = VisitorsRoomDatabase.getDatabase(application.applicationContext).visitorsDao()
        repository = VisitorsRepository(wordDao = wordsDao)
        allWords = repository.allWords
    }

    fun insert(visitor: Visitors) = viewModelScope.launch {
        repository.insert(visitor)
    }
}