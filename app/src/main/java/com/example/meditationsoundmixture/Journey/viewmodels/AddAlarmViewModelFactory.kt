package com.project.meditationsoundmixture.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.meditationsoundmixture.database.AlarmDao

class AddAlarmViewModelFactory(private val application: Application,private val alarmDao: AlarmDao) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddAlarmViewModel::class.java)) {
            return AddAlarmViewModel(application,alarmDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}