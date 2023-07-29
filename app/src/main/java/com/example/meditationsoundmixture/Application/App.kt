package com.project.meditationsoundmixture.Application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.lifecycle.LifecycleObserver
import com.project.meditationsoundmixture.ViewModel.MainViewModel
import com.example.meditationsoundmixture.room.repository.MostPlayedRepository
import com.example.meditationsoundmixture.room.viewmodel.MostPlayedViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class App : Application() ,LifecycleObserver{

    var CHANNEL_ID = "channel1"
    var PLAY = "play"
    var PAUSE = "Pause"
    var PREVIOUS = "previous"
    var EXIT = "exit"
    var counter = 0

    companion object {

        const val CHANNEL_ID = "channel1"
        const val PLAY = "play"
        const val NEXT = "next"
        const val PREVIOUS = "previous"
        const val EXIT = "exit"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        counter++
        startKoin {
            androidContext(this@App)
            androidLogger(Level.NONE)
            loadKoinModules(listOf(viewModelModule))
        }
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)

        }
    }

    private val viewModelModule = module {
        single {
            MainViewModel(get())
        }

        single {
            MostPlayedRepository(get())
        }
        viewModel {
            MostPlayedViewModel(get())
        }

    }

}