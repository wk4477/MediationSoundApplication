package com.example.meditationsoundmixture.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.meditationsoundmixture.room.dao.MostPlayedDao
import com.example.meditationsoundmixture.room.model.MostPlayedModel

@Database(entities = [MostPlayedModel::class], version = 1, exportSchema = false)
abstract class MostPlayedDatabase  :RoomDatabase(){

    abstract fun mostPlayedDao(): MostPlayedDao

    companion object {
        private var instance: MostPlayedDatabase? = null
        @Synchronized
        fun getInstance(ctx: Context): MostPlayedDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, MostPlayedDatabase::class.java,
                    "note_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            return instance!!

        }
    }
}