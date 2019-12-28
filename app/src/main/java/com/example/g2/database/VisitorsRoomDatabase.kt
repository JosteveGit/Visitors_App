package com.example.g2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Visitors::class], version = 1, exportSchema = false)

abstract class VisitorsRoomDatabase : RoomDatabase(){
    abstract  fun visitorsDao(): VisitorsDao

    companion object{
        @Volatile
        private var INSTANCE: VisitorsRoomDatabase? = null

        fun getDatabase(context: Context): VisitorsRoomDatabase {
            val tempInstance =
                INSTANCE
            if(tempInstance !=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VisitorsRoomDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}