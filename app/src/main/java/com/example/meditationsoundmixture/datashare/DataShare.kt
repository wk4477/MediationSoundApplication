package com.project.meditationsoundmixture.datashare

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.createDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataShare(context: Context) {

    companion object {

        private var instance: DataShare? = null
      //  val PREF_LETTER = preferencesKey<Boolean>("Letter")

        fun getInstance(context: Context): DataShare {
            return instance ?: synchronized(this) {
                instance ?: DataShare(context).also {
                    instance = it
                }
            }
        }
    }

    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore("splash")

    suspend fun<T> setAllValue(data : T, key :Preferences.Key<T>){
        dataStore.edit {
            it[key] = data
        }
    }

    fun<T> getAllValue(key : Preferences.Key<T>,default :T) : Flow<T> {
        return dataStore.data.map {
            it[key] ?: default
        }
    }


}
