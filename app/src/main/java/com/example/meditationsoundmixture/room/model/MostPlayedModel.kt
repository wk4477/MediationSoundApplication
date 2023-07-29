package com.example.meditationsoundmixture.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "db_played")
data class MostPlayedModel(

    @PrimaryKey(autoGenerate = true)
    var id :Int = 0,
    val title: String ,
    val counterPosition: Int
)
