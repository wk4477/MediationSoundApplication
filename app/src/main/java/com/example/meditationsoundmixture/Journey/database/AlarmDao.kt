package com.project.meditationsoundmixture.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.project.meditationsoundmixture.models.Alarm
import retrofit2.http.DELETE


@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alarm: Alarm)


    @Query("delete from alarms")
    suspend fun delete()

    @Update
    suspend fun update(alarm: Alarm)

    @Query("SELECT * FROM alarms")
    fun getAlarms(): LiveData<List<Alarm>>

    @Query("SELECT * FROM alarms")
    fun getDaysAlarms(): LiveData<Alarm>


    @Query("SELECT * FROM alarms WHERE dbId=:id LIMIT 1")
     fun getAlarm(id:String):Alarm
}