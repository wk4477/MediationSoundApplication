package com.project.meditationsoundmixture.MeditatioinSoundServices


import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.project.meditationsoundmixture.Application.App
import com.project.meditationsoundmixture.Constants.isCloseService
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.ViewModel.MainViewModel
import com.project.meditationsoundmixture.fragments.Homeitemss
import com.project.meditationsoundmixture.ui.MainActivity
import com.project.meditationsoundmixture.ui.SplashScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MusicPlayerService : Service() {

    val sharedViewModel: MainViewModel by inject()
    private lateinit var mediaSession: MediaSessionCompat
    private var myBinder = MyBinder()
    var mediaPlayerInstance: MediaPlayer? = null
    lateinit var mAudioManager: AudioManager
    var title = ""
    var description = ""
    var imageSong = 0
    fun getInstance(): MediaPlayer {
        if (mediaPlayerInstance == null) {
            mediaPlayerInstance = MediaPlayer()
        }
        return mediaPlayerInstance as MediaPlayer
    }


    override fun onBind(p0: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext, "My Music")
        return myBinder
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayerInstance = getInstance()
        CoroutineScope(Dispatchers.Main).launch {
            delay(100)
            showServiceNotification(false)
        }

    }

    inner class MyBinder : Binder() {
        fun currentService(): MusicPlayerService {
            return this@MusicPlayerService
        }
    }

    fun showServiceNotification(playPauseButton: Boolean) {
        val drawble: Int
       // Toast.makeText(baseContext, "init", Toast.LENGTH_SHORT).show()
        drawble = if (playPauseButton) {
            R.drawable.pause_notification_icon
        } else {
            R.drawable.icpausenew
        }

        val intent = Intent(baseContext, SplashScreen::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val contentIntent =
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val deleteIntent = Intent(this, MusicPlayerService::class.java)

        val deletePendingIntent = PendingIntent.getService(
            this,
            0,
            deleteIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val playIntent =
            Intent(baseContext, MusicActionReceiver::class.java).setAction(App.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            playIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val previousSong = Intent(
            baseContext,
            MusicActionReceiver::class.java
        ).setAction(App.PREVIOUS)
        val previousSongPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            previousSong,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val nextSong =
            Intent(baseContext, MusicActionReceiver::class.java).setAction(App.NEXT)
        val nextSongPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            nextSong,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val closeService =
            Intent(baseContext, MusicActionReceiver::class.java).setAction(App.EXIT)

        val closeServicePendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            closeService,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val img = BitmapFactory.decodeResource(resources, R.drawable.service_icon)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val notification =
                NotificationCompat.Builder(baseContext, App().CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentIntent(contentIntent)
                    .setContentText(description)
                    .setSmallIcon(R.drawable.music_note_ic)
                    .setLargeIcon(img)
                    .setStyle(
                        androidx.media.app.NotificationCompat.MediaStyle().setShowCancelButton(true)
                            .setShowActionsInCompactView(0,1)
                    )
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setOnlyAlertOnce(true)
                    .setColorized(true)
                    .addAction(
                        R.drawable.ic_outline_close_24,
                        "exit",
                        closeServicePendingIntent
                    )
                    .addAction(drawble, "", playPendingIntent)
                    .setDeleteIntent(deletePendingIntent).build()
            startForeground(13, notification)
        }
        else {
            val notification =
                NotificationCompat.Builder(baseContext, App().CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentIntent(contentIntent)
                    .setContentText(description)
                    .setSmallIcon(R.drawable.music_note_ic)
                    .setLargeIcon(img)
                   .setColor(ContextCompat.getColor(baseContext, R.color.white))
                    .addAction(
                        R.drawable.ic_outline_close_24,
                        "exit",
                        closeServicePendingIntent
                    )
                    .setStyle(
                        androidx.media.app.NotificationCompat.MediaStyle()
                            .setMediaSession(mediaSession.sessionToken).setShowCancelButton(true)
                    ).setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setOnlyAlertOnce(true)
                    .addAction(drawble, "", playPendingIntent)
                    .setDeleteIntent(deletePendingIntent).build()
            startForeground(13, notification)

        }

    }

    override fun onStartCommand(intd: Intent?, flags: Int, startId: Int): Int {

        val intent = intd?.getParcelableExtra<Homeitemss>("modelList")
        Log.e("TAG", "onStartCommand: ${intent?.title}", )
        title = intent?.title.toString()
        Log.e("TAG", "onStartCommand: ${intent?.description}", )
        description = intent?.description.toString()
        Log.e("TAG", "onStartCommand: ${intent?.viewimage}", )
        imageSong = intent?.viewimage!!
        Log.e("TAG", "onStartCommand: ${flags}", )

        isCloseService = {
            if (it){
                stopForeground(true)
                isCloseService?.invoke(false)
            }
        }

        return START_NOT_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
    }


}



