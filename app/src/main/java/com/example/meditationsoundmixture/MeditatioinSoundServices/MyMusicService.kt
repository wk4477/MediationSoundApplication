package com.project.meditationsoundmixture.MeditatioinSoundServices

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.media.AudioManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.project.meditationsoundmixture.Application.App
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.ViewModel.MainViewModel
import com.project.meditationsoundmixture.fragments.HomeFragment
import com.project.meditationsoundmixture.ui.MainActivity
import com.project.meditationsoundmixture.ui.MediationSound
import org.koin.android.ext.android.inject
import kotlin.system.exitProcess

class MyMusicService : Service() {

    val sharedViewModel: MainViewModel by inject()

    private lateinit var image: ImageView
    var mediaPlayerInstance: MediaPlayer? = null
    lateinit var mAudioManager: AudioManager

    /* var listner=Equilizer()*/
    var pos: Int? = null
    private lateinit var mediaSession: MediaSessionCompat
    override fun onCreate() {
        //newList = sharedViewModel.audioList.value!!
        mediaSession = MediaSessionCompat(baseContext, "MusicApp")

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val position = intent?.getIntExtra("position1", 0)
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 2, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )


        val pauseIntent = Intent(baseContext, Receiver::class.java).setAction(App().PAUSE)
        val pausePendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                baseContext, 0, pauseIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        } else {
            PendingIntent.getBroadcast(
                baseContext, 0, pauseIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        }

        val exitIntent =
            Intent(baseContext, Receiver::class.java).setAction(App().EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            exitIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val playIntent = Intent(baseContext, Receiver::class.java).setAction(App().PLAY)
        val playPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                baseContext,
                0,
                playIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        else {
            PendingIntent.getBroadcast(
                baseContext,
                0,
                playIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        val img = BitmapFactory.decodeResource(resources, R.drawable.service_icon)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val notification = NotificationCompat.Builder(baseContext, App().CHANNEL_ID)
                .setContentTitle("")
                .setContentIntent(pendingIntent)
                .setContentText("")
                .setSmallIcon(R.drawable.music_note_ic)
                .setLargeIcon(img)
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle().setShowCancelButton(true)
                )
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .setColorized(true)
                .addAction(
                    R.drawable.stop,
                    "Pause",
                    pausePendingIntent
                )
                .addAction(
                    R.drawable.icpausenew,
                    "play",
                    playPendingIntent
                )
                .addAction(
                    R.drawable.ic_launcher_background,
                    "exit",
                    exitPendingIntent
                )
                .build()
            startForeground(13, notification)

        }
        else {
            val notification = NotificationCompat.Builder(baseContext, App().CHANNEL_ID)
                .setContentTitle("")
                .setContentIntent(pendingIntent)
                .setContentText("")
                .setSmallIcon(R.drawable.music_note_ic)
                .setLargeIcon(img)
                .setColor(ContextCompat.getColor(baseContext, R.color.white))
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.sessionToken).setShowCancelButton(true)
                ).setPriority(NotificationCompat.PRIORITY_MIN)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .addAction(
                    R.drawable.icpausenew,
                    "Pause",
                    pausePendingIntent
                )

                .addAction(
                    R.drawable.stop,
                    "exit",
                    exitPendingIntent
                )


                .build()
            startForeground(13, notification)

        }
        return START_NOT_STICKY
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    class Receiver : BroadcastReceiver() {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(context: Context, intent: Intent) {

            when (intent.action) {
                App().PAUSE -> {
                    Log.d("TAG4", "onReceivePAUSE: ")
                    MyMusicService().sharedViewModel.optionItemClick?.invoke(12)
                    MyMusicService().sharedViewModel.optionItemClick3?.invoke(15)
                    if (MyMusicService().sharedViewModel.getInstancemediaplayerone().isPlaying || MyMusicService().sharedViewModel.getInstancemediaplayertwo().isPlaying || MyMusicService().sharedViewModel.getInstancemediaplayerthree().isPlaying || MyMusicService().sharedViewModel.getInstancemediaplayerfour().isPlaying || MyMusicService().sharedViewModel.getInstancemediaplayerfive().isPlaying || MyMusicService().sharedViewModel.getInstancemediaplayersix().isPlaying) {
                       // Toast.makeText(context, "helloharis123", Toast.LENGTH_SHORT).show()
                        MyMusicService().sharedViewModel.getInstancemediaplayerone().pause()
                        MyMusicService().sharedViewModel.getInstancemediaplayertwo().pause()
                        MyMusicService().sharedViewModel.getInstancemediaplayerthree().pause()
                        MyMusicService().sharedViewModel.getInstancemediaplayerfour().pause()
                        MyMusicService().sharedViewModel.getInstancemediaplayerfive().pause()
                        MyMusicService().sharedViewModel.getInstancemediaplayersix().pause()
                    }

                }
                App().PLAY -> {

                    Log.d("TAG4", "onReceiveSTART: ")
                    //Toast.makeText(context, "helloharis", Toast.LENGTH_SHORT).show()
                    MyMusicService().sharedViewModel.getInstancemediaplayerone().start()
                    MyMusicService().sharedViewModel.optionItemClick2?.invoke(13)
                    MyMusicService().sharedViewModel.optionItemClick3?.invoke(16)

                }
                App().EXIT -> {
                    if (NOTIFICATION_SERVICE != null) {
                        Log.d("_trim", "onReceive: ")
                        /*AudioPlayerf.audiobinding.btnPlayPause.setImageResource(R.drawable.ic_play)
                        mainbinding.playBtn.setImageResource(R.drawable.ic_play)
                        context.stopService(Intent(context, VolumeBoosterService::class.java))*/
                        context.stopService(Intent(context, MyMusicService::class.java))

                        /* MyMusicService().sharedViewModel.mediaPlayer?.stop()
                         MyMusicService().sharedViewModel.mediaPlayer = null*/
//                        MyMusicService().sharedViewModel.currentPosition.postValue(-1)
                        /*MyMusicService().pos=null*/

                    }


                }
            }
        }
    }

    fun stopService() {
        stopForeground(true)
        stopSelf()
    }


}
