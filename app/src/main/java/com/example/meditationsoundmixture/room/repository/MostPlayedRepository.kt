package com.example.meditationsoundmixture.room.repository

import androidx.lifecycle.LiveData
import com.example.meditationsoundmixture.room.dao.MostPlayedDao
import com.example.meditationsoundmixture.room.model.MostPlayedModel

class MostPlayedRepository (private val mostPlayedDao: MostPlayedDao){

    var position  = 0
    val mostPlayedModel :LiveData<MostPlayedModel> = mostPlayedDao.getMostPlayed()
    val getAllMostPlayed :LiveData<List<MostPlayedModel>> = mostPlayedDao.getAllMostPlayed()
    //val ifExist :LiveData<List<MostPlayedModel>> = mostPlayedDao.getIfExist(mostPlayedModel = MostPlayedModel())

    suspend fun insertMostPlayed(mostPlayedModel: MostPlayedModel){
        mostPlayedDao.insertMostPlayed(mostPlayedModel)
    }

    suspend fun updateMostPlayed(mostPlayedModel: MostPlayedModel){
        mostPlayedDao.updateMostPlayed(mostPlayedModel)
    }

    fun  isExist(position :String) = mostPlayedDao.isExist(position)
    //fun  getCounter(position :Int) = mostPlayedDao.getCounter(position)




}