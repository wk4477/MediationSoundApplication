package com.project.meditationsoundmixture.MeditatioinSoundServices

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.project.meditationsoundmixture.Application.App
import com.project.meditationsoundmixture.ViewModel.MainViewModel
import org.koin.java.KoinJavaComponent.inject
import kotlin.system.exitProcess


class NotificationReceiver: BroadcastReceiver() {
    private val sharedViewModel: MainViewModel by inject(MainViewModel::class.java)
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            //only play next or prev song, when music list contains more than one song
            App().PLAY -> {
                if (sharedViewModel.getInstancemediaplayerone()?.isPlaying==false)
                {
                    sharedViewModel.getInstancemediaplayerone().start()

                }
            }
            App().PAUSE -> {
                if (sharedViewModel.getInstancemediaplayerone().isPlaying==true)
                {
                    sharedViewModel.getInstancemediaplayerone().pause()
                }
            }
            App().EXIT ->{
                exitProcess(0)
            }
        }
    }
    private fun playMusic(){

    }

    private fun pauseMusic(){

    }

}