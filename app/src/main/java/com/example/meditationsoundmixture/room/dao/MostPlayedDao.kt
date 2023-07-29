package com.example.meditationsoundmixture.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.meditationsoundmixture.room.model.MostPlayedModel

@Dao
interface MostPlayedDao {

    @Insert
    suspend fun insertMostPlayed(mostPlayedModel: MostPlayedModel)

    @Update
    suspend fun updateMostPlayed(mostPlayedModel: MostPlayedModel)

    @Query("SELECT EXISTS(select * from db_played where  title =:position )")
    fun  isExist(position :String) :Boolean

//    @Query("select counterPosition from db_played where title =:position ")
//    fun getCounter(position: Int) :LiveData<MostPlayedModel>

    @Query("select * from db_played where counterPosition = (SELECT MAX(counterPosition) FROM db_played) ")
    fun  getMostPlayed() :LiveData<MostPlayedModel>

    @Query("select * from db_played")
    fun  getAllMostPlayed() :LiveData<List<MostPlayedModel>>
}