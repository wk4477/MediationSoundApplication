package com.example.meditationsoundmixture.room.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.meditationsoundmixture.room.db.MostPlayedDatabase
import com.example.meditationsoundmixture.room.model.MostPlayedModel
import com.example.meditationsoundmixture.room.repository.MostPlayedRepository
import kotlinx.coroutines.launch

class MostPlayedViewModel(application: Application) :AndroidViewModel(application) {

    val mostPlayedRepository : MostPlayedRepository
    val mostPlayedModel :LiveData<MostPlayedModel>
    val getAllmostPlayed :LiveData<List<MostPlayedModel>>

    init {
        val dao = MostPlayedDatabase.getInstance(application).mostPlayedDao()
        mostPlayedRepository = MostPlayedRepository(dao)
        mostPlayedModel = mostPlayedRepository.mostPlayedModel
        getAllmostPlayed = mostPlayedRepository.getAllMostPlayed
    }

    fun insertMostPlayed(mostPlayedModel: MostPlayedModel){
        viewModelScope.launch {
            mostPlayedRepository.insertMostPlayed(mostPlayedModel)
        }
    }

    fun updateMostPlayed(mostPlayedModel: MostPlayedModel){
        viewModelScope.launch {
            mostPlayedRepository.updateMostPlayed(mostPlayedModel)
        }
    }

    fun  isExist(position :String) =
//        viewModelScope.launch {
            mostPlayedRepository.isExist(position)

            //fun  getCounter(position :Int) = mostPlayedRepository.getCounter(position)
//        }


}