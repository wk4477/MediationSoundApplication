package com.project.meditationsoundmixture.viewmodels

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.database.AlarmDao
import com.project.meditationsoundmixture.models.Alarm
import com.project.meditationsoundmixture.receivers.AlarmReceiver
import com.project.meditationsoundmixture.ui.MainActivity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class AddAlarmViewModel(private val application: Application, private val alarmDao: AlarmDao) :
    ViewModel() {
    private val _isAM = MutableLiveData<Boolean>()
    val isAM: LiveData<Boolean>
        get() = _isAM
    var days = ArrayList<String>()
    var minute: Number
    var hour: Number
    val sd = ArrayList<String>()
    private var alarmSounds: Map<String, Int>

    private val alarmSound = MutableLiveData<Int>()
    val alarmName = MutableLiveData<String>()
    var alarmVolume = 0.5F

    var mediaPlayer: MediaPlayer? = null
    var mediaPlaying = MutableLiveData<Boolean>()

    var addVibration = MutableLiveData<Boolean>()
    private var vibrator: Vibrator
    //var alarms: LiveData<List<Alarm>> = alarmDao.getAlarms()
    var alarmTimer: LiveData<Alarm> = alarmDao.getDaysAlarms()

    init {
        _isAM.value = true
        mediaPlaying.value = false
        addVibration.value = false

        Calendar.getInstance().apply {
            minute = this.get(Calendar.MINUTE)
            hour = this.get(Calendar.HOUR)
            if (hour.toInt() > 12) {
                hour = hour.toInt() - 12
            }
        }

        alarmSounds =
            mapOf(
                application.getString(R.string.default_sound) to R.raw.buzzer,
                application.getString(R.string.clock_sound) to R.raw.clock,
                application.getString(R.string.rooster_sound) to R.raw.rooster
            )

        alarmName.value = application.getString(R.string.default_sound)
        alarmSound.value = alarmSounds[alarmName.value]

        vibrator = application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

//    fun deleteAlarm(){
//        viewModelScope.launch {
//            alarmDao.delete()
//        }
//    }

    fun setAM(value: Boolean) {
        _isAM.value = value
    }

    fun addToSelectedDays(day: String) {
        days.add(day)
        val modified = days.distinct()
        days = arrayListOf()
        days.addAll(modified)
        Log.e("TAG", "addToSelectedDays: ${day}")
        Log.e("TAG", "addToSelectedDays: ${days.size}")
    }

    fun removeFromSelectedDays(day: String) {
        days.remove(day)
    }

    fun setAlarmSound(sound: String) {
        alarmName.value = sound
        alarmSound.value = alarmSounds[sound]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun playSelectedAlarmSound() {
        stopAlarmSound()
        if (addVibration.value == true) {
            vibrator.vibrate(
                VibrationEffect.createWaveform(
                    longArrayOf(600, 400, 600, 400),
                    intArrayOf(
                        VibrationEffect.DEFAULT_AMPLITUDE,
                        0,
                        VibrationEffect.DEFAULT_AMPLITUDE,
                        0
                    ), 0
                )
            )
        }
        mediaPlayer =
            MediaPlayer.create(application.applicationContext, alarmSound.value!!)
        mediaPlayer?.apply {
            setVolume(alarmVolume, alarmVolume)
            start()
            mediaPlaying.value = true
            setOnCompletionListener {
                it.release()
                mediaPlaying.value = false
                vibrator.cancel()
            }
        }
    }

    fun changeAlarmVolume(volume: Float) {
        alarmVolume = volume
        try {
            mediaPlayer?.setVolume(alarmVolume, alarmVolume)
        } catch (e: Exception) {
        }
    }

    fun stopAlarmSound() {
        mediaPlayer?.apply {
            try {
                vibrator.cancel()
                this.stop()
                this.release()
                mediaPlaying.value = false
            } catch (e: Exception) {
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    suspend fun saveAlarm(name: String): Boolean {
        try {
            val alarmId = UUID.randomUUID().toString()

            if (days.size ==0){
                val sdf = SimpleDateFormat("EEE")
                val d = Date()
                val dayOfTheWeek: String = sdf.format(d)
               // val currentDay = LocalDate.now().dayOfWeek.name
                days.add(dayOfTheWeek)
            }
            Log.e("TAG", "saveAlarm: $days")
            val alarm = Alarm(
                alarmId,
                name,
                alarmSound.value!!,
                addVibration.value!!,
                alarmVolume,
                days,
                minute.toInt(),
                hour.toInt(),
                isAM.value!!,
                false
            )
            alarmDao.insert(alarm)

            val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val current = System.currentTimeMillis()
            Log.e("AlarmManager", "saveAlarm: ${hour.toInt()}")
            Log.e("AlarmManager", "saveAlarm: ${minute.toInt()}")

            val calendar: Calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR, if (hour.toInt() == 12) 0 else hour.toInt())
                set(Calendar.MINUTE, minute.toInt())
                set(Calendar.MILLISECOND, 0)
                set(Calendar.AM_PM, if (isAM.value!!) Calendar.AM else Calendar.PM)
            }
            Log.e("AlarmManager", "saveAlarm: ${current}")
            Log.e("AlarmManager", "saveAlarm: ${calendar.timeInMillis}")

            val alarmIntent = Intent(application, AlarmReceiver::class.java).let {
                it.action = "com.abumuhab.alarm.action.START_ALARM"
                it.putExtra("id",alarmId)
                PendingIntent.getBroadcast(application, 0, it, PendingIntent.FLAG_CANCEL_CURRENT)
            }

            val intent2 = Intent(application, MainActivity::class.java).let {
                PendingIntent.getActivity(application, 1, it, 0)
            }

            if(calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1)
            }
            Log.e("AlarmManager", "after: ${calendar.timeInMillis}")
          //  alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_HALF_DAY, alarmIntent)
            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(calendar.timeInMillis, intent2),
                alarmIntent
            )
//            if (calendar.timeInMillis <= System.currentTimeMillis()) {
//                application.applicationContext.shortToast("select time again")
//            } else {
//                alarmManager.setAlarmClock(
//                    AlarmManager.AlarmClockInfo(calendar.timeInMillis, intent2),
//                    alarmIntent
//                )
//            }


            return true
        } catch (e: Exception) {
            return false
        }
    }
}