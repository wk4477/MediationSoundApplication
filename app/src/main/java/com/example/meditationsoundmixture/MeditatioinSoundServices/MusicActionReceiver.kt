package com.project.meditationsoundmixture.MeditatioinSoundServices

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.ServiceCompat.stopForeground
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.project.meditationsoundmixture.Application.App
import com.project.meditationsoundmixture.Constants.isCloseService
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.ui.MainActivity
import com.project.meditationsoundmixture.ui.MediationSound


class MusicActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            App.PLAY -> {
                Log.e("PLAY", "onReceive: enter", )
                if (MyMusicService().sharedViewModel.isPlaying())
                {
                    MyMusicService().sharedViewModel.pausePlay()
                    MyMusicService().sharedViewModel.getInstancemediaplayerone().pause()
                    MediationSound.binding.addVol.setImageResource(R.drawable.ic_playbtn)
                }
                else
                {
                    MyMusicService().sharedViewModel.pausePlay()
                    MyMusicService().sharedViewModel.getInstancemediaplayerone().start()
                    MediationSound.binding.addVol.setImageResource(R.drawable.icpausenew)
                }

            }
            App.EXIT -> {
                Log.e("TAG", "onReceive: enter", )
              //  stopForeground(MusicPlayerService(),0)
                if (MyMusicService().sharedViewModel.isPlaying()) {
                    MyMusicService().sharedViewModel.pausePlay()
                    MyMusicService().sharedViewModel.getInstancemediaplayerone().pause()
                    MediationSound.binding.addVol.setImageResource(R.drawable.ic_playbtn)
                }
                isCloseService?.invoke(true)
//                context?.stopService(intent)
               // context?.stopService(Intent(context,MusicPlayerService::class.java))

            }
        }
    }
}