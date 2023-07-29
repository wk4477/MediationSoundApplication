package com.project.meditationsoundmixture.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.meditationsoundmixture.database.AlarmDao
import com.project.meditationsoundmixture.models.Alarm
import kotlinx.coroutines.launch


class AlarmsViewModel(private val application: Application, private val alarmDao: AlarmDao) :
    ViewModel() {

    var alarms: LiveData<List<Alarm>> = alarmDao.getAlarms()
    var alarmdays: LiveData<Alarm> = alarmDao.getDaysAlarms()
//
    fun deleteAlarm(){
        viewModelScope.launch {
            alarmDao.delete()
        }
    }
//
//    fun insertAlarm(alarm: Alarm){
//        viewModelScope.launch {
//            alarmDao.insert(alarm)
//        }
//    }
}