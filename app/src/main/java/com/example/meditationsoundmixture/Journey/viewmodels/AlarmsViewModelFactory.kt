package com.project.meditationsoundmixture.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.meditationsoundmixture.database.AlarmDao


class AlarmsViewModelFactory (private val application: Application, private val alarmDao: AlarmDao)
    : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmsViewModel::class.java)) {
            return AlarmsViewModel(application,alarmDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}